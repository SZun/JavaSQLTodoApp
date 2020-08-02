package com.sgz.TodoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidAuthorityException;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
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
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private final User testUser = new User(id, "@amBam20", "Sam");

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

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void getAllRoles() throws Exception {
        final String expected = "[{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"USER\"},{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"ADMIN\"},{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"GUEST\"}]";

        when(roleService.getAllRoles()).thenReturn(
                Arrays.asList(expectedRole, new Role(id, "ADMIN"), new Role(id, "GUEST")
                ));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/roles"))
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

        when(mockMvc.perform(get(baseURL + "/roles"))).thenThrow(new NoItemsException("No Items"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/roles"))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getAllRolesForbidden() throws Exception {
        mockMvc.perform(get(baseURL + "/roles"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void getRoleById() throws Exception {
        when(roleService.getRoleById(any(UUID.class))).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/roles/" + testUUIDStr))
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

        when(mockMvc.perform(get(baseURL + "/roles/" + testUUIDStr))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/roles/" + testUUIDStr))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getAllRoleByIdForbidden() throws Exception {
        mockMvc.perform(get(baseURL + "/roles/" + testUUIDStr))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void createRole() throws Exception {
        when(roleService.createRole(any(Role.class))).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL + "/roles")
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
                post(baseURL + "/roles")
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
                post(baseURL + "/roles")
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
                post(baseURL + "/roles")
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
                put(baseURL + "/roles/" + id.toString())
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
                put(baseURL + "/roles/" + id.toString())
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
                put(baseURL + "/roles/" + id.toString())
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
                put(baseURL + "/roles/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void deleteRoleById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(baseURL + "/roles/" + testUUIDStr))
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


        when(mockMvc.perform(delete(baseURL + "/roles/" + testUUIDStr))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(delete(baseURL + "/roles/" + testUUIDStr))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void deleteRoleByIdForbidden() throws Exception {
        mockMvc.perform(delete(baseURL + "/roles/" + testUUIDStr))
                .andExpect(status().isForbidden());
    }
}