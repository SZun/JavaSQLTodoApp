package com.sgz.TodoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.jwt.JwtConfig;
import com.sgz.TodoApp.jwt.JwtSecretKey;
import com.sgz.TodoApp.services.AdminService;
import com.sgz.TodoApp.services.AuthService;
import com.sgz.TodoApp.services.TodoService;
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
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private TodoService todoService;

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

    private final String baseURL = "/api/v1/todos/";

    private final User testUser = new User(1, "@amBam20", "Sam", Sets.newHashSet(new Role(1, "USER")));

    private final Todo testTodo = new Todo("Walk Dog", LocalDate.of(2020, 07, 31));

    private final Todo expectedTodo = new Todo(1, "Walk Dog", "Finished walking baxter", LocalDate.of(2020, 07, 31), null, false, this.testUser);

    @Test
    @WithMockUser
    void getAll() throws Exception {
        final String expected = "[{\"id\":0,\"name\":\"Walk Dog\",\"description\":null,\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false}]";

        when(authService.getUserId()).thenReturn(1);
        when(todoService.getAllTodos(anyInt())).thenReturn(Arrays.asList(testTodo));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void getAllNoItems() throws Exception {
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
    void getById() throws Exception {
        final String expected = "{\"id\":0,\"name\":\"Walk Dog\",\"description\":null,\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false}";

        when(authService.getUserId()).thenReturn(1);
        when(todoService.getTodoById(anyInt(), anyInt())).thenReturn(testTodo);

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "1"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void deleteById() throws Exception {
        when(authService.getUserId()).thenReturn(1);

        MvcResult mvcResult = mockMvc.perform(delete(baseURL + "1"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("1", content);
    }

    @Test
    @WithMockUser
    void deleteByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(mockMvc.perform(delete(baseURL + "1"))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(delete(baseURL + "1"))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void addTodo() throws Exception {
        final String expected = "{\"id\":0,\"name\":\"Walk Dog\",\"description\":null,\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false}";

        when(authService.getUserId()).thenReturn(1);
        when(todoService.createTodo(any(Todo.class), anyInt())).thenReturn(testTodo);

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(expected)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void updateTodo() throws Exception {
        final String expected = "{\"id\":1,\"name\":\"Walk Dog\",\"description\":\"Finished walking baxter\",\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false}";

        when(authService.getUserId()).thenReturn(1);
        when(todoService.editTodo(any(Todo.class), anyInt())).thenReturn(expectedTodo);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "1")
                        .content(expected)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void updateTodoException() throws Exception {
        final String expected = "{\"message\":\"Server Exception\",\"name\":\"Exception\",\"errors\":null,\"timestamp\":";

        when(mockMvc.perform(delete(baseURL + "1"))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(put(baseURL + "1"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expected));
    }

    @Test
    void getAllForbidden() throws Exception {
        mockMvc.perform(get(baseURL))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByIdForbidden() throws Exception {
        mockMvc.perform(get(baseURL + 1))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteByIdForbidden() throws Exception {
        mockMvc.perform(delete(baseURL + 1))
                .andExpect(status().isForbidden());
    }

    @Test
    void addTodoForbidden() throws Exception {
        mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testTodo))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void updateTodoForbidden() throws Exception {
        mockMvc.perform(
                put(baseURL + "1")
                        .content(objectMapper.writeValueAsString(testTodo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}