package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.TestAppConfig;
import com.sgz.TodoApp.entities.ApplicationRole;
import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.InvalidNameException;
import com.sgz.TodoApp.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestAppConfig.class)
class AdminServiceTest {

    @Autowired
    private AdminService toTest;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRepo repo;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Set<ApplicationRole> testRoles = Sets.newHashSet(new ApplicationRole(1, "USER"));

    private final String testLongString = "C39V2iGLMtU1xN8tctQQVPnr7Y41mgIqCCPKookK7yrKP9xweAp6Oo7NGOBp6wkWIP1cQZvxW2n40ZK0vUUHWxQzhjUCRnUXFx1uSSKXYP37nlsLcMnmaxpnGY7JGmKap7Q4e1mdtVg3aZ829B3IeMCzxTs2Ex5IOrbgu55cwUKh3z7GBFssVQL4mzr1eHqfOv67prPQgcCQCDIRSEZH1tt0h5yxVgVt2prBdgUWBmo6sg6UPS6k1quBYGDoFBIk";

    @BeforeEach
    void setUp() throws InvalidEntityException, InvalidNameException {
        repo.deleteAll();
        jdbc.update("DELETE FROM Roles");
        jdbc.update("ALTER TABLE Users auto_increment = 1");
        jdbc.update("ALTER TABLE Roles auto_increment = 1");
        jdbc.update("INSERT INTO Roles(Authority) VALUES('USER'),('ADMIN')");
        userService.createUser(new ApplicationUser(testRoles, "@amBam20","Sam"));
    }

    @Test
    void updateUserRole() throws InvalidIdException, InvalidEntityException {
        Set<ApplicationRole> newRoles = Sets.newHashSet(new ApplicationRole(1, "USER"), new ApplicationRole(2, "ADMIN"));

        ApplicationUser original = new ApplicationUser(1, testRoles, "@amBam20", "Sam");

        ApplicationUser fromService = userService.getUserById(1);

        assertEquals(original.getId(), fromService.getId());
        assertEquals(original.getAuthorities(), fromService.getAuthorities());
        assertTrue(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertEquals(original.getUsername(), fromService.getUsername());

        ApplicationUser expected = new ApplicationUser(1, newRoles, fromService.getPassword(), "Sam");

        fromService = toTest.updateUserRole(expected);
        assertEquals(fromService, expected);

        assertEquals(original.getId(), fromService.getId());
        assertNotEquals(original.getAuthorities(), fromService.getAuthorities());
        assertTrue(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertEquals(original.getUsername(), fromService.getUsername());

        fromService = userService.getUserById(1);
        assertEquals(fromService, expected);
    }

    @Test
    void updateUserRoleInvalidId() throws InvalidEntityException {
        ApplicationUser testUser = new ApplicationUser(-1, testRoles, "@amBam20", "Sam");
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex) {}
    }

    @Test
    void updateUserRoleNullUser() throws InvalidIdException {
        try {
            toTest.updateUserRole(null);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRoleEmptyAuthorities() throws InvalidIdException {
        ApplicationUser testUser = new ApplicationUser(1, new HashSet<>(), "@amBam20", "Sam");
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRoleNullAuthorities() throws InvalidIdException {
        ApplicationUser testUser = new ApplicationUser(1, null, "@amBam20", "Sam");
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRoleNameDiff() throws InvalidIdException {
        ApplicationUser testUser = new ApplicationUser(1, testRoles, "@amBam20", "Sammy");
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRolePasswordDiff() throws InvalidIdException {
        ApplicationUser testUser = new ApplicationUser(1, testRoles, "@amBam22", "Sam");
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }


}