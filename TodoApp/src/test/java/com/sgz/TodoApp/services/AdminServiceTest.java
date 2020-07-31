package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.repos.RoleRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    private final User testUser = new User(1, "@amBam20", "Sam");

    private final Role testRole = new Role(1, "USER");

    private final Role expectedRole = new Role(1, "USER", Arrays.asList(this.testUser));

    private final User expectedUser = new User(1, "@amBam20", "Sam", Sets.newHashSet(this.testRole));

    private final String testLongString = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    @Test
    void updateRoleUsers() throws InvalidIdException, InvalidEntityException {
        when(roleRepo.save(any(Role.class))).thenReturn(expectedRole);

        Role fromService = toTest.updateRoleUsers(expectedRole);

        assertEquals(expectedRole, fromService);
    }

    @Test
    void updateUserRoles() throws InvalidIdException, InvalidEntityException {
        when(userRepo.save(any(User.class))).thenReturn(expectedUser);

        User fromService = toTest.updateUserRoles(expectedUser);

        assertEquals(expectedUser, fromService);

    }

}