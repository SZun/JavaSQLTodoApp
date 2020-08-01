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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService toTest;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final UUID id = new UUID(36,36);

    private final User testUser = new User(id, "@amBam20", "Sam");

    private final Set<Role>  testRoles = Sets.newHashSet(Sets.newHashSet(new Role(id, "USER")));

    private final User expectedUser = new User(id, "@amBam20", "Sam", this.testRoles);

    private final String testLongString = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    @Test
    void createUser() throws InvalidEntityException, InvalidNameException {
        final User toCreate = new User("@amBam20", "Sam", testRoles);

        when(passwordEncoder.encode(anyString())).thenReturn(toCreate.getPassword());
        when(userRepo.save(any())).thenReturn(testUser);

        User fromService = toTest.createUser(toCreate);

        assertEquals(testUser, fromService);
    }

    @Test
    void createUserNullUser() {
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(null));
    }

    @Test
    void createUserEmptyUsername() {
        final User toCreate = new User("@amBam20", "", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserBlankUsername() {
        final User toCreate = new User("@amBam20", "   ", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserTooLongUsername() {
        final User toCreate = new User("@amBam20", testLongString, testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserInvalidUsername() {
        final User toCreate = new User("@amBam20", "Sam", testRoles);

        when(userRepo.existsByUsername(anyString())).thenReturn(true);

        assertThrows(InvalidNameException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserEmptyPassword() {
        final User toCreate = new User("", "Sam", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserBlankPassword() {
        final User toCreate = new User("  ", "Sam", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserTooLongPassword() {
        final User toCreate = new User(testLongString, "Sam", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserInvalidPassword() {
        final User toCreate = new User("password", "Sam", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserNullRoles() {
        final User toCreate = new User("password", "Sam", null);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserEmptyRoles() {
        final User toCreate = new User("password", "Sam", Sets.newHashSet());
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void getAllUsers() throws NoItemsException {
        final User expected2 = new User(id, "@amBam22", "Sam2", testRoles);
        final User expected3 = new User(id, "@amBam23", "Sam3", testRoles);

        when(userRepo.findAll()).thenReturn(Arrays.asList(expectedUser, expected2, expected3));

        List<User> fromService = toTest.getAllUsers();

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expectedUser));
        assertTrue(fromService.contains(expected2));
        assertTrue(fromService.contains(expected3));
    }

    @Test
    void getAllUsersNoItems() {
        assertThrows(NoItemsException.class, () -> toTest.getAllUsers());
    }

    @Test
    void getUserByName() throws InvalidEntityException, InvalidNameException {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(expectedUser));

        User fromService = toTest.getUserByName("Sam");

        assertEquals(expectedUser, fromService);
    }

    @Test
    void getUserNullName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getUserByName(null));
    }

    @Test
    void getUserEmptyName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getUserByName(""));
    }

    @Test
    void getUserBlankName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getUserByName("  "));
    }

    @Test
    void getUserTooLongName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getUserByName(testLongString));
    }

    @Test
    void getUserInvalidName() {
        assertThrows(InvalidNameException.class, () -> toTest.getUserByName("username"));
    }

    @Test
    void editUser() throws InvalidEntityException, InvalidIdException, AccessDeniedException {
        final User toEdit = new User(id, "@amBam25", "Test_User", testRoles);

        when(userRepo.existsById(any(UUID.class))).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn(toEdit.getPassword());
        when(userRepo.save(any())).thenReturn(testUser);

        User fromService = toTest.editUser(toEdit, id);

        assertEquals(testUser, fromService);
    }

    @Test
    void editUserNullUser() {
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(null,id));
    }

    @Test
    void editUserEmptyUsername() {
        final User toEdit = new User(id, "@amBam25", "", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserBlankUsername() {
        final User toEdit = new User(id, "@amBam25", "   ", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserTooLongUsername() {
        final User toEdit = new User(id, "@amBam25", testLongString, testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserEmptyPassword() {
        final User toEdit = new User(id, "", "Sam", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserBlankPassword() {
        final User toEdit = new User(id, "  ", "Sam", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserTooLongPassword() {
        final User toEdit = new User(id, testLongString, "Sam", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserInvalidPassword() {
        final User toEdit = new User(id, "password", "Sam", testRoles);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserInvalidId() {
        final User toEdit = new User(id, "@amBam20", "Sam", testRoles);
        assertThrows(InvalidIdException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserNullRoles() {
        final User toEdit = new User(id, "@amBam20", "Sam", null);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserEmptyRoles() {
        final User toEdit = new User(id, "@amBam20", "Sam", Sets.newHashSet());
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit, id));
    }

    @Test
    void editUserAccessDenied() {
        final User toEdit = new User(id, "@amBam20", "Sam", testRoles);
        assertThrows(AccessDeniedException.class, () -> toTest.editUser(toEdit, UUID.randomUUID()));
    }

    @Test
    void getUserById() throws InvalidIdException {
        when(userRepo.findById(any(UUID.class))).thenReturn(Optional.of(expectedUser));

        User fromService = toTest.getUserById(id);

        assertEquals(expectedUser, fromService);
    }

    @Test
    void getUserByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.getUserById(id));
    }

    @Test
    void deleteUserById() throws InvalidIdException, AccessDeniedException {
        when(userRepo.existsById(any(UUID.class))).thenReturn(true);

        toTest.deleteUserById(id,id);
    }

    @Test
    void deleteUserByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.deleteUserById(id,id));
    }

    @Test
    void deleteUserByIdAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> toTest.deleteUserById(id, UUID.randomUUID()));
    }
}