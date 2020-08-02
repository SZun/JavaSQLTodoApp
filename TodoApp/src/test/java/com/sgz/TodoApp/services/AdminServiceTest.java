package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.repos.RoleRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService toTest;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    private final UUID id = new UUID(36,36);

    private final User testUser = new User(id, "@amBam20", "Sam");

    private final Role testRole = new Role(id, "USER");

    private final Role expectedRole = new Role(id, "USER", Arrays.asList(this.testUser));

    private final User expectedUser = new User(id, "@amBam20", "Sam", Sets.newHashSet(this.testRole));

    private final String testLongString = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    @Test
    void updateRoleUsers() throws InvalidEntityException {
        when(roleRepo.save(any(Role.class))).thenReturn(expectedRole);

        Role fromService = toTest.updateRoleUsers(expectedRole);

        assertEquals(expectedRole, fromService);
    }

    @Test
    void updateRoleUsersNullRole() {
        assertThrows(InvalidEntityException.class, () -> toTest.updateRoleUsers(null));
    }

    @Test
    void updateRoleUsersBlankAuthority() {
        final Role toEdit = new Role(id, "  ", Arrays.asList(this.testUser));
        assertThrows(InvalidEntityException.class, () -> toTest.updateRoleUsers(toEdit));
    }

    @Test
    void updateRoleUsersEmptyAuthority() {
        final Role toEdit = new Role(id, "", Arrays.asList(this.testUser));
        assertThrows(InvalidEntityException.class, () -> toTest.updateRoleUsers(toEdit));
    }

    @Test
    void updateRoleUsersTooLongAuthority() {
        final Role toEdit = new Role(id, testLongString, Arrays.asList(this.testUser));
        assertThrows(InvalidEntityException.class, () -> toTest.updateRoleUsers(toEdit));
    }

    @Test
    void updateRoleUsersNullUsers() {
        final Role toEdit = new Role(id, "USER", null);
        assertThrows(InvalidEntityException.class, () -> toTest.updateRoleUsers(toEdit));
    }

    @Test
    void updateRoleUsersEmptyUsers() {
        final Role toEdit = new Role(id, "USER", new ArrayList<>());
        assertThrows(InvalidEntityException.class, () -> toTest.updateRoleUsers(toEdit));
    }

    @Test
    void updateUserRoles() throws InvalidEntityException {
        when(userRepo.save(any(User.class))).thenReturn(expectedUser);

        User fromService = toTest.updateUserRoles(expectedUser);

        assertEquals(expectedUser, fromService);
    }

    @Test
    void updateUserRolesNullUser() {
        assertThrows(InvalidEntityException.class, () -> toTest.updateUserRoles(null));
    }

    @Test
    void updateUserRolesBlankName() {
        final User toEdit = new User(id, "@amBam20", "  ", Sets.newHashSet(this.testRole));
        assertThrows(InvalidEntityException.class, () -> toTest.updateUserRoles(toEdit));
    }

    @Test
    void updateUserRolesEmptyName() {
        final User toEdit = new User(id, "@amBam20", "", Sets.newHashSet(this.testRole));
        assertThrows(InvalidEntityException.class, () -> toTest.updateUserRoles(toEdit));
    }

    @Test
    void updateUserRolesTooLongName() {
        final User toEdit = new User(id, "@amBam20", testLongString, Sets.newHashSet(this.testRole));
        assertThrows(InvalidEntityException.class, () -> toTest.updateUserRoles(toEdit));
    }

    @Test
    void updateUserRolesBlankPassword() {
        final User toEdit = new User(id, "  ", "Sam", Sets.newHashSet(this.testRole));
        assertThrows(InvalidEntityException.class, () -> toTest.updateUserRoles(toEdit));
    }

    @Test
    void updateUserRolesEmptyPassword() {
        final User toEdit = new User(id, "", "Sam", Sets.newHashSet(this.testRole));
        assertThrows(InvalidEntityException.class, () -> toTest.updateUserRoles(toEdit));
    }

    @Test
    void updateUserRolesNullRoles() {
        final User toEdit = new User(id, "@amBam20", "Sam", null);
        assertThrows(InvalidEntityException.class, () -> toTest.updateUserRoles(toEdit));
    }

    @Test
    void updateUserRolesEmpty() {
        final User toEdit = new User(id, "@amBam20", "Sam", Sets.newHashSet());
        assertThrows(InvalidEntityException.class, () -> toTest.updateUserRoles(toEdit));
    }

}