package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.*;
import com.sgz.TodoApp.repos.RoleRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService toTest;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final User testUser = new User(1, "@amBam20", "Sam");

    private final Role testRole = new Role(1, "USER");

    private final Role expectedRole = new Role(1, "USER", Arrays.asList(this.testUser));

    private final User expectedUser = new User(1, "@amBam20", "Sam", Sets.newHashSet(this.testRole));

    private final String testLongString = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    @Test
    void updateRoleUsers() throws InvalidIdException {
        when(roleRepo.findById(anyInt())).thenReturn(Optional.of(testRole));
        when(userRepo.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(roleRepo.save(expectedRole)).thenReturn(expectedRole);

        Role fromService = toTest.updateRoleUsers(1, Arrays.asList(1));

        assertEquals(expectedRole, fromService);
    }

    @Test
    void updateRoleUsersInvalidRole() {
        when(userRepo.findById(anyInt())).thenReturn(Optional.of(testUser));
        assertThrows(InvalidIdException.class, () -> toTest.updateRoleUsers(1, Arrays.asList(1)));
    }

    @Test
    void updateRoleUsersInvalidUser() {
        assertThrows(InvalidIdException.class, () -> toTest.updateRoleUsers(1, Arrays.asList(1)));
    }

    @Test
    void updateUserRoles() throws InvalidIdException {
        when(roleRepo.findById(anyInt())).thenReturn(Optional.of(testRole));
        when(userRepo.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(userRepo.save(expectedUser)).thenReturn(expectedUser);

        User fromService = toTest.updateUserRoles(1, Sets.newHashSet(1));

        assertEquals(expectedUser, fromService);

    }

    @Test
    void updateUserRolesInvalidRole() {
        assertThrows(InvalidIdException.class, () -> toTest.updateUserRoles(1, Sets.newHashSet(1)));
    }

    @Test
    void updateUserRolesInvalidUsers() {
        when(roleRepo.findById(anyInt())).thenReturn(Optional.of(testRole));
        assertThrows(InvalidIdException.class, () -> toTest.updateUserRoles(1, Sets.newHashSet(1)));
    }

    @Test
    void createUser() throws InvalidEntityException, InvalidNameException {
        final User toCreate = new User("@amBam20", "Sam");

        when(passwordEncoder.encode(anyString())).thenReturn(toCreate.getPassword());
        when(userRepo.save(toCreate)).thenReturn(testUser);

        User fromService = toTest.createUser(toCreate);

        assertEquals(testUser, fromService);
    }

    @Test
    void createUserNullUser() {
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(null));
    }

    @Test
    void createUserEmptyUsername() {
        final User toCreate = new User("@amBam20", "");
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserBlankUsername() {
        final User toCreate = new User("@amBam20", "   ");
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserTooLongUsername() {
        final User toCreate = new User("@amBam20", testLongString);
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserInvalidUsername() {
        final User toCreate = new User("@amBam20", "Sam");

        when(userRepo.existsByUsername(anyString())).thenReturn(true);

        assertThrows(InvalidNameException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserEmptyPassword() {
        final User toCreate = new User("", "Sam");
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserBlankPassword() {
        final User toCreate = new User("  ", "Sam");
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserTooLongPassword() {
        final User toCreate = new User(testLongString, "Sam");
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void createUserInvalidPassword() {
        final User toCreate = new User("password", "Sam");
        assertThrows(InvalidEntityException.class, () -> toTest.createUser(toCreate));
    }

    @Test
    void getAllUsers() throws NoItemsException {
        final User expected2 = new User(1, "@amBam22", "Sam2", Sets.newHashSet(testRole));
        final User expected3 = new User(1, "@amBam23", "Sam3", Sets.newHashSet(testRole));

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
    void editUser() throws InvalidEntityException, InvalidIdException {
        final User toEdit = new User(1, "@amBam25", "Test_User");

        when(userRepo.existsById(anyInt())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn(toEdit.getPassword());
        when(userRepo.save(toEdit)).thenReturn(testUser);

        User fromService = toTest.editUser(toEdit);

        assertEquals(testUser, fromService);
    }

    @Test
    void editUserNullUser() {
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(null));
    }

    @Test
    void editUserEmptyUsername() {
        final User toEdit = new User(1, "@amBam25", "");
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit));
    }

    @Test
    void editUserBlankUsername() {
        final User toEdit = new User(1, "@amBam25", "   ");
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit));
    }

    @Test
    void editUserTooLongUsername() {
        final User toEdit = new User(1, "@amBam25", testLongString);
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit));
    }

    @Test
    void editUserEmptyPassword() {
        final User toEdit = new User(1, "", "Sam");
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit));
    }

    @Test
    void editUserBlankPassword() {
        final User toEdit = new User(1, "  ", "Sam");
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit));
    }

    @Test
    void editUserTooLongPassword() {
        final User toEdit = new User(1, testLongString, "Sam");
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit));
    }

    @Test
    void editUserInvalidPassword() {
        final User toEdit = new User(1, "password", "Sam");
        assertThrows(InvalidEntityException.class, () -> toTest.editUser(toEdit));
    }

    @Test
    void editUserInvalidId() {
        final User toEdit = new User(1, "@amBam20", "Sam");
        assertThrows(InvalidIdException.class, () -> toTest.editUser(toEdit));
    }

    @Test
    void getUserById() throws InvalidIdException {
        when(userRepo.findById(anyInt())).thenReturn(Optional.of(expectedUser));

        User fromService = toTest.getUserById(1);

        assertEquals(expectedUser, fromService);
    }

    @Test
    void getUserByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.getUserById(1));
    }

    @Test
    void deleteUserById() throws InvalidIdException {
        when(userRepo.existsById(anyInt())).thenReturn(true);

        toTest.deleteUserById(1);
    }

    @Test
    void deleteUserByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.deleteUserById(1));
    }

    @Test
    void getAllRoles() throws NoItemsException {
        final Role expected2 = new Role(2, "ADMIN", Arrays.asList(testUser));
        final Role expected3 = new Role(3, "GUEST", Arrays.asList(testUser));

        when(roleRepo.findAll()).thenReturn(Arrays.asList(expectedRole, expected2, expected3));

        List<Role> fromService = toTest.getAllRoles();

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expectedRole));
        assertTrue(fromService.contains(expected2));
        assertTrue(fromService.contains(expected3));
    }

    @Test
    void getAllRolesNoItems() {
        assertThrows(NoItemsException.class, () -> toTest.getAllRoles());
    }

    @Test
    void getRoleById() throws InvalidIdException {
        when(roleRepo.findById(anyInt())).thenReturn(Optional.of(expectedRole));

        Role fromService = toTest.getRoleById(1);

        assertEquals(expectedRole, fromService);
    }

    @Test
    void getRoleByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.getRoleById(1));
    }

    @Test
    void getRoleByAuthority() throws InvalidAuthorityException, InvalidEntityException {
        when(roleRepo.findByAuthority(anyString())).thenReturn(Optional.of(expectedRole));

        Role fromService = toTest.getRoleByAuthority("USER");

        assertEquals(expectedRole, fromService);
    }

    @Test
    void getRoleByAuthorityNullAuthority() {
        assertThrows(InvalidEntityException.class, () -> toTest.getRoleByAuthority(null));
    }

    @Test
    void getRoleByAuthorityBlankAuthority() {
        assertThrows(InvalidEntityException.class, () -> toTest.getRoleByAuthority("  "));
    }

    @Test
    void getRoleByAuthorityEmptyAuthority() {
        assertThrows(InvalidEntityException.class, () -> toTest.getRoleByAuthority(""));
    }

    @Test
    void getRoleByAuthorityTooLongAuthority() {
        assertThrows(InvalidEntityException.class, () -> toTest.getRoleByAuthority(testLongString));
    }

    @Test
    void getRoleByAuthorityInvalidAuthority() {
        assertThrows(InvalidAuthorityException.class, () -> toTest.getRoleByAuthority("USER"));
    }

    @Test
    void createRole() throws InvalidEntityException, InvalidAuthorityException {
        final Role toCreate = new Role("USER");

        when(roleRepo.save(toCreate)).thenReturn(testRole);

        Role fromService = toTest.createRole(toCreate);

        assertEquals(testRole, fromService);
    }

    @Test
    void createRoleNullRole() {
        assertThrows(InvalidEntityException.class, () -> toTest.createRole(null));
    }

    @Test
    void createRoleEmptyAuthority() {
        final Role toCreate = new Role("");
        assertThrows(InvalidEntityException.class, () -> toTest.createRole(toCreate));
    }

    @Test
    void createRoleBlankAuthority()  {
        final Role toCreate = new Role("  ");
        assertThrows(InvalidEntityException.class, () -> toTest.createRole(toCreate));
    }

    @Test
    void createRoleTooLongAuthority()  {
        final Role toCreate = new Role(testLongString);
        assertThrows(InvalidEntityException.class, () -> toTest.createRole(toCreate));
    }

    @Test
    void createRoleInvalidAuthority() throws InvalidEntityException {
        final Role toCreate = new Role("USER");

        when(roleRepo.existsByAuthority(anyString())).thenReturn(true);

        assertThrows(InvalidAuthorityException.class, () -> toTest.createRole(toCreate));
    }

    @Test
    void editRole() throws InvalidEntityException, InvalidIdException {
        when(roleRepo.existsById(anyInt())).thenReturn(true);
        when(roleRepo.save(testRole)).thenReturn(testRole);

        Role fromService = toTest.editRole(testRole);

        assertEquals(testRole, fromService);
    }

    @Test
    void editRoleNullRole()  {
        assertThrows(InvalidEntityException.class, () -> toTest.editRole(null));
    }

    @Test
    void editRoleEmptyAuthority()  {
        final Role toEdit = new Role(1, "");
        assertThrows(InvalidEntityException.class, () -> toTest.editRole(toEdit));
    }

    @Test
    void editRoleBlankAuthority()  {
        final Role toEdit = new Role(1, "  ");
        assertThrows(InvalidEntityException.class, () -> toTest.editRole(toEdit));
    }

    @Test
    void editRoleTooLongAuthority()  {
        final Role toEdit = new Role(1, testLongString);
        assertThrows(InvalidEntityException.class, () -> toTest.editRole(toEdit));
    }

    @Test
    void editRoleInvalidId() {
        final Role toEdit = new Role(1, "USER");
        assertThrows(InvalidIdException.class, () -> toTest.editRole(toEdit));
    }

    @Test
    void deleteRoleById() throws InvalidIdException {
        when(roleRepo.existsById(anyInt())).thenReturn(true);
        toTest.deleteRoleById(1);
    }

    @Test
    void deleteRoleByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.deleteRoleById(1));
    }
}