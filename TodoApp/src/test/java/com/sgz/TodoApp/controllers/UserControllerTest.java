package com.sgz.TodoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
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
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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

    private ObjectMapper objectMapper = new ObjectMapper();

    private final String baseURL = "/api/v1/users/";

    private final User testUser = new User(1, "@amBam20", "Sam");

    private final Role testRole = new Role(1, "USER");

    private final Role expectedRole = new Role(1, "USER", Arrays.asList(this.testUser));

    private final User expectedUser = new User(1, "@amBam20", "Sam", Sets.newHashSet(testRole));

    @Test
    void createUser() throws Exception {
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.createUser(any(User.class))).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(
                post(
                        baseURL + "create"
                )
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"password\":\"@amBam20\",\"username\":\"Sam\"}", content);
    }

    @Test
    @WithMockUser
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(expectedUser));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{\"id\":1,\"password\":\"@amBam20\",\"username\":\"Sam\"}]", content);
    }

    @Test
    @WithMockUser
    void getUserById() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "1"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"password\":\"@amBam20\",\"username\":\"Sam\"}", content);
    }

    @Test
    @WithMockUser
    void updateUserById() throws Exception {
        when(userService.getUserByName(anyString())).thenReturn(expectedUser);
        when(authService.getUserId()).thenReturn(1);
        when(roleService.getRoleByAuthority(anyString())).thenReturn(expectedRole);
        when(userService.editUser(any(User.class), anyInt())).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "1")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"password\":\"@amBam20\",\"username\":\"Sam\"}", content);
    }

    @Test
    @WithMockUser
    void deleteUserById() throws Exception {
        when(authService.getUserId()).thenReturn(1);

        mockMvc.perform(delete(baseURL + "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsersForbidden() throws Exception {
        mockMvc.perform(get(baseURL))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserByIdForbidden() throws Exception {
        mockMvc.perform(get(baseURL + "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUserByIdForbidden() throws Exception {
        mockMvc.perform(
                put(baseURL + "1")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserByIdForbidden() throws Exception {
        mockMvc.perform(
                delete(baseURL + "1")
        )
                .andExpect(status().isForbidden());
    }
}