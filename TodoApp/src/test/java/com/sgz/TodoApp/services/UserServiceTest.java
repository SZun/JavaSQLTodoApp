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
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    private final Set<ApplicationRole> testRoles = Sets.newHashSet(new ApplicationRole(1, "USER"));

    private final String testLongString = "C39V2iGLMtU1xN8tctQQVPnr7Y41mgIqCCPKookK7yrKP9xweAp6Oo7NGOBp6wkWIP1cQZvxW2n40ZK0vUUHWxQzhjUCRnUXFx1uSSKXYP37nlsLcMnmaxpnGY7JGmKap7Q4e1mdtVg3aZ829B3IeMCzxTs2Ex5IOrbgu55cwUKh3z7GBFssVQL4mzr1eHqfOv67prPQgcCQCDIRSEZH1tt0h5yxVgVt2prBdgUWBmo6sg6UPS6k1quBYGDoFBIk";

    @BeforeEach
    void setUp() {
        repo.deleteAll();
        jdbc.update("ALTER TABLE Users auto_increment = 1");
        jdbc.update("ALTER TABLE Roles auto_increment = 1");
        jdbc.update("INSERT INTO Roles(Authority) VALUES('USER')");
        repo.save(new ApplicationUser(testRoles, "@amBam20","Sam"));
    }

    @Test
    void createUser() throws InvalidEntityException, InvalidNameException {
//        ApplicationUser toAdd = new ApplicationUser("password", "Sammy");
//        ApplicationUser expected = new ApplicationUser(2, testRoles, testPass, "Sammy");
//
//        ApplicationUser fromService = toTest.createUser(toAdd);
//        assertEquals(expected, fromService);
    }



    @Test
    void createUserNullUser() {

    }

    @Test
    void createUserInvalidName() {

    }

    @Test
    void createUserNullName() {

    }

    @Test
    void createUserNullPassword() {

    }

    @Test
    void createUserEmptyPassword() {

    }

    @Test
    void createUserEmptyName() {

    }

    @Test
    void createUserBlankPassword() {

    }

    @Test
    void createUserBlankName() {

    }

    @Test
    void editUser() {
    }

    @Test
    void editUserNullUser() throws InvalidIdException {

    }

    @Test
    void editUserNullName() throws InvalidIdException {

    }

    @Test
    void editUserNullPassword() throws InvalidIdException {

    }

    @Test
    void editUserEmptyPassword() throws InvalidIdException {

    }

    @Test
    void editUserEmptyName() throws InvalidIdException {

    }

    @Test
    void editUserBlankPassword() throws InvalidIdException {

    }

    @Test
    void editeUserBlankName() throws InvalidIdException {

    }

    @Test
    void editUserInvalidId() throws InvalidEntityException {

    }

    @Test
    void getUserById() throws InvalidIdException {
    }

    @Test
    void getUserByIdInvalidId() {
    }

    @Test
    void deleteUserById() throws InvalidIdException {
    }

    @Test
    void deleteUserByIdInvalidId() {
    }
}