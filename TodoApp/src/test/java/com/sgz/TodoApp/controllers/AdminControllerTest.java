package com.sgz.TodoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.jwt.JwtConfig;
import com.sgz.TodoApp.jwt.JwtSecretKey;
import com.sgz.TodoApp.services.AdminService;
import com.sgz.TodoApp.services.RoleService;
import com.sgz.TodoApp.services.UserDetailsServiceImpl;
import com.sgz.TodoApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.crypto.SecretKey;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private JwtSecretKey jwtSecretKey;

    @MockBean
    private SecretKey secretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseURL = "/api/v1/admin";

    private final UUID id = new UUID(36, 36);

    private String testUUIDStr = "11a9c792-45c5-4220-ab7c-fb832b282911";

    private final Role testRole = new Role(id, "USER");

    private final Role expectedRole = new Role(id, "USER");

    private final User expectedUser = new User(id, "@amBam20", "Sam", Sets.newHashSet(testRole));

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void editUserRoles() throws Exception {
        final String expected = "{\"id\":\"00000000-0000-0024-0000-000000000024\",\"password\":\"@amBam20\",\"username\":\"Sam\",\"roles\":[{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"USER\"}]}";

        when(userService.getUserById(any(UUID.class))).thenReturn(expectedUser);
        when(roleService.getRoleById(any(UUID.class))).thenReturn(expectedRole);
        when(adminService.updateUserRoles(any(User.class))).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/users/"+ testUUIDStr +"/roles")
                        .content(objectMapper.writeValueAsString(Sets.newHashSet(id, id, id)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void editUserRolesInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserById(any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/users/"+ testUUIDStr +"/roles")
                        .content(objectMapper.writeValueAsString(Sets.newHashSet(id, id, id)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void editUserRolesInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserById(any(UUID.class))).thenReturn(expectedUser);
        when(roleService.getRoleById(any(UUID.class))).thenReturn(expectedRole);
        when(adminService.updateUserRoles(any(User.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/users/"+ testUUIDStr +"/roles")
                        .content(objectMapper.writeValueAsString(Sets.newHashSet(id, id, id)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void editUserRolesForbidden() throws Exception {
        mockMvc.perform(
                put(baseURL + "users/" + testUUIDStr + "/roles")
                        .content(objectMapper.writeValueAsString(Sets.newHashSet(id, id, id)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}