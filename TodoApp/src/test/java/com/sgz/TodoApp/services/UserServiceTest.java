package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.InvalidNameException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService toTest;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final User testUser = new User(1, "@amBam20", "Sam");

    private final Role testRole = new Role(1, "USER");

    private final User expectedUser = new User(1, "@amBam20", "Sam", Sets.newHashSet(this.testRole));

    private final String testLongString = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    @Test
    void createUser() throws InvalidEntityException, InvalidNameException {
    }

    @Test
    void createUserInvalidName() {
    }

    @Test
    void createUserNullUser() {
    }

    @Test
    void createUserBlankName() {
    }

    @Test
    void createUserEmptyName() {
    }

    @Test
    void createUserTooLongName() {
    }

    @Test
    void createUserBlankPassword() {
    }

    @Test
    void createUserEmptyPassword() {
    }

    @Test
    void createUserTooLongPassword() {
    }

    @Test
    void createUserInvalidPassword() {
    }

    @Test
    void createUserNullRole() {
    }

    @Test
    void createUserEmptyRoles() {
    }

    @Test
    void getAllUsers() throws NoItemsException {
    }

    @Test
    void getAllUsersNoItems() {
    }

    @Test
    void getUserByName() throws InvalidEntityException, InvalidNameException {
    }

    @Test
    void getUserByNameNullName() {
    }

    @Test
    void getUserByNameBlankName() {
    }

    @Test
    void getUserByNameEmptyName() {
    }

    @Test
    void getUserByNameInvalidName() {
    }

    @Test
    void editUser() throws InvalidEntityException, InvalidIdException, InvalidNameException, AccessDeniedException {
    }

    @Test
    void editUserInvalidName() {
    }

    @Test
    void editUserNullUser() {
    }

    @Test
    void editUserBlankName() {
    }

    @Test
    void editUserEmptyName() {
    }

    @Test
    void editUserTooLongName() {
    }

    @Test
    void editUserBlankPassword() {
    }

    @Test
    void editUserEmptyPassword() {
    }

    @Test
    void editUserTooLongPassword() {
    }

    @Test
    void editUserInvalidPassword() {
    }

    @Test
    void editUserNullRole() {
    }

    @Test
    void editUserEmptyRoles() {
    }

    @Test
    void editUserUnauthorized() {
    }

    @Test
    void editUserNonExistent() {
    }

    @Test
    void getUserById() throws InvalidIdException {
    }

    @Test
    void getUserByIdInvalidId() {
    }

    @Test
    void deleteUserById() throws InvalidIdException, AccessDeniedException {
    }

    @Test
    void deleteUserByIdInvalidId() {

    }

    @Test
    void deleteUserByIdUnauthorized() {
    }
}