package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.TestAppConfig;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.*;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestAppConfig.class)
class UserServiceTest {

    @Autowired
    private UserService toTest;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final User expected = new User(1, new HashSet<>(), "", "Sam");

    private final Set<Role> testRoles = Sets.newHashSet(new Role(1, "USER", null));

    private final String testLongString = "C39V2iGLMtU1xN8tctQQVPnr7Y41mgIqCCPKookK7yrKP9xweAp6Oo7NGOBp6wkWIP1cQZvxW2n40ZK0vUUHWxQzhjUCRnUXFx1uSSKXYP37nlsLcMnmaxpnGY7JGmKap7Q4e1mdtVg3aZ829B3IeMCzxTs2Ex5IOrbgu55cwUKh3z7GBFssVQL4mzr1eHqfOv67prPQgcCQCDIRSEZH1tt0h5yxVgVt2prBdgUWBmo6sg6UPS6k1quBYGDoFBIk";

    @BeforeEach
    void setUp() throws InvalidEntityException, InvalidNameException, InvalidAuthorityException {
        repo.deleteAll();
        jdbc.update("DELETE FROM Roles");
        jdbc.update("ALTER TABLE Users auto_increment = 1");
        jdbc.update("ALTER TABLE Roles auto_increment = 1");
        jdbc.update("INSERT INTO Roles(Authority) VALUES('USER')");
        toTest.createUser(new User(testRoles, "@amBam20", "Sam"));
        toTest.createUser(new User(testRoles, "@amBam20", "Sam2"));
        toTest.createUser(new User(testRoles, "@amBam20", "Sam3"));
    }

    @Test
    void getAll() throws NoItemsException {
        User expected2 = new User(2, new HashSet<>(), "", "Sam2");
        User expected3 = new User(3, new HashSet<>(), "", "Sam3");

        List<User> fromService = toTest.getAll();

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expected));
        assertTrue(fromService.contains(expected2));
        assertTrue(fromService.contains(expected3));
    }

    @Test
    void getAllNoItems() {
        repo.deleteAll();
        try {
            toTest.getAll();
            fail("should hit NoItemsException");
        } catch (NoItemsException ex) {
        }
    }

    @Test
    void getUserByName() throws InvalidNameException, InvalidEntityException {

        User fromService = toTest.getUserByName("Sam");

        assertEquals(expected, fromService);
    }

    @Test
    void getUserByNameNullName() throws InvalidNameException {
        try {
            toTest.getUserByName(null);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void getUserByNameEmptyName() throws InvalidNameException {
        try {
            toTest.getUserByName("");
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void getUserByNameBlankName() throws InvalidNameException {
        try {
            toTest.getUserByName("  ");
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void getUserByNameInvalidName() throws InvalidEntityException {
        try {
            toTest.getUserByName("Non Existent Name");
            fail("should hit InvalidNameException");
        } catch (InvalidNameException ex) {
        }
    }

    @Test
    void createUser() throws InvalidEntityException, InvalidNameException, InvalidAuthorityException {
        User toAdd = new User(testRoles, "@amBam20", "Sammy");

        User fromService = toTest.createUser(toAdd);
        assertEquals(toAdd, fromService);
    }

    @Test
    void createUserInvalidName() throws InvalidEntityException, InvalidAuthorityException {
        User toAdd = new User(testRoles, "@amBam20", "Sam");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidNameException");
        } catch (InvalidNameException ex) {
        }
    }

    @Test
    void createUserEmptyPassword() throws InvalidNameException, InvalidAuthorityException {
        User toAdd = new User(testRoles, "", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void createUserEmptyName() throws InvalidNameException, InvalidAuthorityException {
        User toAdd = new User(testRoles, "@amBam20", "");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void createUserBlankPassword() throws InvalidNameException, InvalidAuthorityException {
        User toAdd = new User(testRoles, "   ", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void createUserBlankName() throws InvalidNameException, InvalidAuthorityException {
        User toAdd = new User(testRoles, "@amBam20", "   ");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void createUserTooLongPassword() throws InvalidNameException, InvalidAuthorityException {
        User toAdd = new User(testRoles, testLongString, "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void createUserTooLongName() throws InvalidNameException, InvalidAuthorityException {
        User toAdd = new User(testRoles, "@amBam20", testLongString);
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void createUserInvalidPassword() throws InvalidNameException, InvalidAuthorityException {
        User toAdd = new User(testRoles, "password", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void editUser() throws InvalidIdException, InvalidEntityException, InvalidAuthorityException {

        User fromService = toTest.getUserById(1);
        assertEquals(expected, fromService);

        User toEdit = new User(1, testRoles, "@amBam21", "Sammy");
        fromService = toTest.editUser(toEdit);

        assertEquals(toEdit, fromService);

        assertEquals(expected.getId(), fromService.getId());
        assertEquals(testRoles, fromService.getRoles());
        assertFalse(passwordEncoder.matches(expected.getPassword(), fromService.getPassword()));
        assertNotEquals(expected.getUsername(), fromService.getUsername());

        fromService = toTest.getUserById(1);
        toEdit.setRoles(new HashSet<>());
        toEdit.setPassword("");
        assertEquals(toEdit, fromService);
        assertNotEquals(expected, fromService);
    }


    @Test
    void editUserEmptyPassword() throws InvalidIdException, InvalidAuthorityException {
        User toEdit = new User(1, testRoles, "", "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void editUserEmptyName() throws InvalidIdException, InvalidAuthorityException {
        User toEdit = new User(1, testRoles, "@amBam20", "");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void editUserTooLongPassword() throws InvalidIdException, InvalidAuthorityException {
        User toEdit = new User(1, testRoles, testLongString, "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void editUserTooLongName() throws InvalidIdException, InvalidAuthorityException {
        User toEdit = new User(1, testRoles, "@amBam20", testLongString);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void editUserBlankPassword() throws InvalidIdException, InvalidAuthorityException {
        User toEdit = new User(1, testRoles, "  ", "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void editUserBlankName() throws InvalidIdException, InvalidAuthorityException {
        User toEdit = new User(1, testRoles, "@amBam20", "  ");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void editUserInvalidPassword() throws InvalidIdException, InvalidAuthorityException {
        User toEdit = new User(1, testRoles, "password", "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {
        }
    }

    @Test
    void editUserInvalidId() throws InvalidEntityException, InvalidAuthorityException {
        User toEdit = new User(-1, testRoles, "@amBam20", "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex) {
        }
    }

    @Test
    void getUserById() throws InvalidIdException {
        User fromService = toTest.getUserById(1);

        assertEquals(expected, fromService);
    }

    @Test
    void getUserByIdInvalidId() {
        try {
            toTest.getUserById(-1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex) {
        }
    }

    @Test
    void deleteUserById() throws InvalidIdException {

        User fromService = toTest.getUserById(1);
        assertEquals(expected, fromService);

        try {
            toTest.getUserById(-1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex) {
        }
    }

    @Test
    void deleteUserByIdInvalidId() {
        try {
            toTest.deleteUserById(-1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex) {
        }
    }
}