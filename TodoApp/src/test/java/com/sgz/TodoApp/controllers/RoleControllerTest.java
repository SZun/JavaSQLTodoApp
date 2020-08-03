package com.sgz.TodoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.exceptions.InvalidAuthorityException;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.jwt.JwtConfig;
import com.sgz.TodoApp.jwt.JwtSecretKey;
import com.sgz.TodoApp.services.RoleService;
import com.sgz.TodoApp.services.UserDetailsServiceImpl;
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
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    private final String baseURL = "/api/v1/roles";

    private final UUID id = new UUID(36, 36);

    private String testUUIDStr = "11a9c792-45c5-4220-ab7c-fb832b282911";

    private final Role testRole = new Role(id, "USER");

    private final Role expectedRole = new Role(id, "USER");

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void getAllRoles() throws Exception {
        final String expected = "[{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"USER\"},{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"ADMIN\"},{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"GUEST\"}]";

        when(roleService.getAllRoles()).thenReturn(
                Arrays.asList(expectedRole, new Role(id, "ADMIN"), new Role(id, "GUEST")
                ));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, content);
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void getAllRolesNoItems() throws Exception {
        final String expectedMsg = "\"message\":\"No Items\",";
        final String expectedName = "\"name\":\"NoItemsException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(mockMvc.perform(get(baseURL))).thenThrow(new NoItemsException("No Items"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getAllRolesForbidden() throws Exception {
        mockMvc.perform(get(baseURL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void getRoleById() throws Exception {
        when(roleService.getRoleById(any(UUID.class))).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"USER\"}", content);
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void getRoleByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(mockMvc.perform(get(baseURL + "/" + testUUIDStr))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getRoleByIdForbidden() throws Exception {
        mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void createRole() throws Exception {
        when(roleService.createRole(any(Role.class))).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"USER\"}", content);
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void createRoleInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(roleService.createRole(any(Role.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testRole))
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
    void createRoleInvalidAuthority() throws Exception {
        final String expectedMsg = "\"message\":\"Authority entered is invalid\",";
        final String expectedName = "\"name\":\"InvalidAuthorityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(roleService.createRole(any(Role.class))).thenThrow(new InvalidAuthorityException("Invalid Authority"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void createRoleForbidden() throws Exception {
        mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void editRole() throws Exception {
        when(roleService.editRole(any(Role.class))).thenReturn(expectedRole);
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + id.toString())
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"USER\"}", content);
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void editRoleInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(roleService.editRole(any(Role.class))).thenThrow(new InvalidIdException("Invalid Id"));
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + id.toString())
                        .content(objectMapper.writeValueAsString(testRole))
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
    void editRoleInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(roleService.editRole(any(Role.class))).thenThrow(new InvalidEntityException("Invalid Entity"));
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + id.toString())
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void editRoleForbidden() throws Exception {
        mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void deleteRoleById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(baseURL + "/" + testUUIDStr))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("\"11a9c792-45c5-4220-ab7c-fb832b282911\"", content);
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void deleteRoleByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";


        when(mockMvc.perform(delete(baseURL + "/" + testUUIDStr))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(delete(baseURL + "/" + testUUIDStr))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void deleteRoleByIdForbidden() throws Exception {
        mockMvc.perform(delete(baseURL + "/" + testUUIDStr))
                .andExpect(status().isForbidden());
    }
}