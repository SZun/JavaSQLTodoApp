package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidAuthorityException;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.RoleRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService toTest;

    @Mock
    private RoleRepo roleRepo;

    private final User testUser = new User(1, "@amBam20", "Sam");

    private final Role testRole = new Role(1, "USER");

    private final Role expectedRole = new Role(1, "USER", Arrays.asList(this.testUser));

    private final String testLongString = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

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

        when(roleRepo.save(any())).thenReturn(testRole);

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