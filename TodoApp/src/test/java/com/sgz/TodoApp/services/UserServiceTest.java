package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.ApplicationRole;
import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Set;

class UserServiceTest {

    @Autowired
    private UserService toTest;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRepo repo;

    private final Set<ApplicationRole> testRoles = Sets.newHashSet(new ApplicationRole(1, "USER"));

    private final String testPass = "$2a$10$S8nFUMB8YIEioeWyap24/ucX.dC6v9tXCbpHjJVQUkrXlrH1VLaAS";

    @BeforeEach
    void setUp() {
        repo.deleteAll();
        jdbc.update("ALTER TABLE Users auto_increment = 1");
        jdbc.update("ALTER TABLE Users Roles = 1");
        jdbc.update("INSERT INTO Roles(Authority) VALUES('USER')");
        repo.save(new ApplicationUser(testRoles,testPass,"Sam"));
        repo.save(new ApplicationUser(testRoles,testPass,"Sam"));
        repo.save(new ApplicationUser(testRoles,testPass,"Sam"));
    }

    @Test
    void createUser() {
    }

    @Test
    void editUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void deleteUserById() {
    }
}