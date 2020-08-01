package com.sgz.TodoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private final String baseURL = "/api/v1/admin/";

    private final User testUser = new User(1, "@amBam20", "Sam");

    private final Role testRole = new Role(1, "USER");

    private final Role expectedRole = new Role(1, "USER", Arrays.asList(this.testUser));

    private final User expectedUser = new User(1, "@amBam20", "Sam", Sets.newHashSet(testRole));

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void editUserRoles() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(expectedUser);
        when(roleService.getRoleById(anyInt())).thenReturn(expectedRole);
        when(adminService.updateUserRoles(any(User.class))).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "users/1/roles")
                        .content(objectMapper.writeValueAsString(Sets.newHashSet(1,1,1)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"password\":\"@amBam20\",\"username\":\"Sam\"}", content);
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void updateRolesUsers() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(expectedUser);
        when(roleService.getRoleById(anyInt())).thenReturn(expectedRole);
        when(adminService.updateRoleUsers(any(Role.class))).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "roles/1/users")
                        .content(objectMapper.writeValueAsString(Arrays.asList(1,1,1)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"authority\":\"USER\"}", content);
    }

    @Test
    void editUserRolesForbidden() throws Exception {
        mockMvc.perform(
                put(baseURL + "users/1/roles")
                        .content(objectMapper.writeValueAsString(Sets.newHashSet(1,1,1)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRolesUsersForbidden() throws Exception {
        mockMvc.perform(
                put(baseURL + "roles/1/users")
                        .content(objectMapper.writeValueAsString(Arrays.asList(1,1,1)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void getAllRoles() throws Exception {
        final String expected = "[{\"id\":1,\"authority\":\"USER\"},{\"id\":2,\"authority\":\"ADMIN\"},{\"id\":3,\"authority\":\"GUEST\"}]";

        when(roleService.getAllRoles()).thenReturn(
                Arrays.asList(expectedRole, new Role(2, "ADMIN"), new Role(3, "GUEST")
                ));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "roles"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, content);
    }

    @Test
    void getAllRolesForbidden() throws Exception {
        mockMvc.perform(get(baseURL + "roles"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void getAllRoleById() throws Exception {
        when(roleService.getRoleById(anyInt())).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "roles/1"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"id\":1,\"authority\":\"USER\"}", content);
    }

    @Test
    void getAllRoleByIdForbidden() throws Exception {
         mockMvc.perform(get(baseURL + "roles/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void createRole() throws Exception {
        when(roleService.createRole(any(Role.class))).thenReturn(expectedRole);

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL + "roles")
                    .content(objectMapper.writeValueAsString(testRole))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"authority\":\"USER\"}", content);
    }

    @Test
    void createRoleForbidden() throws Exception {
        mockMvc.perform(
                post(baseURL + "roles")
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
                put(baseURL + "roles/1")
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"authority\":\"USER\"}", content);
    }

    @Test
    void editRoleForbidden() throws Exception {
        mockMvc.perform(
                put(baseURL + "roles/1")
                        .content(objectMapper.writeValueAsString(testRole))
                        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "Sam", roles = {"ADMIN"})
    void deleteRoleById() throws Exception {
        mockMvc.perform(delete(baseURL + "roles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRoleByIdForbiddent() throws Exception {
        mockMvc.perform(delete(baseURL + "roles/1"))
                .andExpect(status().isForbidden());
    }
}