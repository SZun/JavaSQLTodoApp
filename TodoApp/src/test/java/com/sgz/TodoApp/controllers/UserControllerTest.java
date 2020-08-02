package com.sgz.TodoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.*;
import com.sgz.TodoApp.jwt.JwtConfig;
import com.sgz.TodoApp.jwt.JwtSecretKey;
import com.sgz.TodoApp.services.*;
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
import java.nio.file.AccessDeniedException;
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
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private AuthService authService;

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

    private final String baseURL = "/api/v1/users/";

    private final UUID id = new UUID(36, 36);

    private String testUUIDStr = "11a9c792-45c5-4220-ab7c-fb832b282911";

    private final User testUser = new User(this.id, "@amBam20", "Sam");

    private final Role testRole = new Role(this.id, "USER");

    private final Role expectedRole = new Role(this.id, "USER", Arrays.asList(this.testUser));

    private final User expectedUser = new User(this.id, "@amBam20", "Sam", Sets.newHashSet(testRole));

    @Test
    void createUser() throws Exception {
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.createUser(any(User.class))).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL + "create")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":\"00000000-0000-0024-0000-000000000024\",\"password\":\"@amBam20\",\"username\":\"Sam\"}", content);
    }

    @Test
    void createUserInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.createUser(any(User.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL + "create")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void createUserInvalidName() throws Exception {
        final String expectedMsg = "\"message\":\"Name entered is invalid\",";
        final String expectedName = "\"name\":\"InvalidNameException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.createUser(any(User.class))).thenThrow(new InvalidNameException("Invalid Name"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL + "create")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void createUserInvalidAuthority() throws Exception {
        final String expectedMsg = "\"message\":\"Authority entered is invalid\",";
        final String expectedName = "\"name\":\"InvalidAuthorityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(roleService.getRoleByAuthority(anyString())).thenThrow(new InvalidAuthorityException("Invalid Authority"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL + "create")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(expectedUser));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{\"id\":\"00000000-0000-0024-0000-000000000024\",\"password\":\"@amBam20\",\"username\":\"Sam\"}]", content);
    }

    @Test
    @WithMockUser
    void getAllUsersNoItems() throws Exception {
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
    @WithMockUser
    void getUserById() throws Exception {
        when(userService.getUserById(any(UUID.class))).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":\"00000000-0000-0024-0000-000000000024\",\"password\":\"@amBam20\",\"username\":\"Sam\"}", content);
    }

    @Test
    @WithMockUser
    void getUserByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserById(any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void updateUserById() throws Exception {
        when(userService.getUserByName(anyString())).thenReturn(expectedUser);
        when(authService.getUserId()).thenReturn(id);
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.editUser(any(User.class), any(UUID.class))).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":\"00000000-0000-0024-0000-000000000024\",\"password\":\"@amBam20\",\"username\":\"Sam\"}", content);
    }

    @Test
    @WithMockUser
    void updateUserByIdInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserByName(anyString())).thenReturn(expectedUser);
        when(authService.getUserId()).thenReturn(id);
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.editUser(any(User.class), any(UUID.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void updateUserByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserByName(anyString())).thenReturn(expectedUser);
        when(authService.getUserId()).thenReturn(id);
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.editUser(any(User.class), any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void updateUserByIdInvalidAuthority() throws Exception {
        final String expectedMsg = "\"message\":\"Authority entered is invalid\",";
        final String expectedName = "\"name\":\"InvalidAuthorityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserByName(anyString())).thenReturn(expectedUser);
        when(authService.getUserId()).thenReturn(id);
        when(roleService.getRoleByAuthority(anyString())).thenThrow(new InvalidAuthorityException("Invalid Authority"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void updateUserByIdAccessDenied() throws Exception {
        final String expectedMsg = "\"message\":\"Access to the requested resource was denied\",";
        final String expectedName = "\"name\":\"AccessDenied\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserByName(anyString())).thenReturn(expectedUser);
        when(authService.getUserId()).thenReturn(id);
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.editUser(any(User.class), any(UUID.class))).thenThrow(new AccessDeniedException("Access Denied"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void deleteUserById() throws Exception {
        when(authService.getUserId()).thenReturn(id);

        MvcResult mvcResult = mockMvc.perform(delete(baseURL + "/" + testUUIDStr))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("\"11a9c792-45c5-4220-ab7c-fb832b282911\"", content);
    }

    @Test
    @WithMockUser
    void deleteUserByIdInvalidId() throws Exception {
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
    void getAllUsersForbidden() throws Exception {
        mockMvc.perform(get(baseURL))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserByIdForbidden() throws Exception {
        mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUserByIdForbidden() throws Exception {
        mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserByIdForbidden() throws Exception {
        mockMvc.perform(
                delete(baseURL + "/" + testUUIDStr)
        )
                .andExpect(status().isForbidden());
    }
}