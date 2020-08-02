package com.sgz.TodoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

    private final UUID id = UUID.fromString("08b2a786-39f8-4752-b4c8-d0c26c01de32");

    private String testUUIDStr = "11a9c792-45c5-4220-ab7c-fb832b282911";

    final String expected = "{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"name\":\"Walk Dog\",\"description\":\"Finished walking baxter\",\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false,\"user\":{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"password\":\"@amBam20\",\"username\":\"Sam\",\"roles\":[{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"authority\":\"USER\"}]}}";

    private final User testUser = new User(this.id, "@amBam20", "Sam", Sets.newHashSet(new Role(this.id, "USER")));

    private final Todo testTodo = new Todo("Walk Dog", LocalDate.of(2020, 07, 31));

    private final Todo expectedTodo = new Todo(this.id, "Walk Dog", "Finished walking baxter", LocalDate.of(2020, 07, 31), null, false, this.testUser);

    @Test
    @WithMockUser
    void getAllTodos() throws Exception {
        final String expected = "[{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"name\":\"Walk Dog\",\"description\":\"Finished walking baxter\",\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false,\"user\":{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"password\":\"@amBam20\",\"username\":\"Sam\",\"roles\":[{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"authority\":\"USER\"}]}}]";

        when(authService.getUserId()).thenReturn(id);
        when(todoService.getAllTodos(any(UUID.class))).thenReturn(Arrays.asList(expectedTodo));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void getAllTodosItems() throws Exception {
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
        when(authService.getUserId()).thenReturn(id);
        when(todoService.getTodoById(any(UUID.class), any(UUID.class))).thenReturn(expectedTodo);

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void getByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(authService.getUserId()).thenReturn(id);
        when(todoService.getTodoById(any(UUID.class), any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void deleteById() throws Exception {
        when(authService.getUserId()).thenReturn(id);

        MvcResult mvcResult = mockMvc.perform(delete(baseURL + "/" + testUUIDStr))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("\"11a9c792-45c5-4220-ab7c-fb832b282911\"", content);
    }

    @Test
    @WithMockUser
    void deleteByIdInvalidId() throws Exception {
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
    @WithMockUser
    void addTodo() throws Exception {
        when(authService.getUserId()).thenReturn(id);
        when(todoService.createTodo(any(Todo.class), any(UUID.class))).thenReturn(expectedTodo);

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
    void addTodoInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";
        final String toEditStr = "{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"name\":\"Walk Dog\",\"description\":\"Finished walking baxter\",\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false}";

        when(authService.getUserId()).thenReturn(id);
        when(todoService.createTodo(any(Todo.class), any(UUID.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(toEditStr)
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
    void updateTodo() throws Exception {
        when(authService.getUserId()).thenReturn(id);
        when(todoService.editTodo(any(Todo.class), any(UUID.class))).thenReturn(expectedTodo);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
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
    void updateTodoInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";
        final String toEditStr = "{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"name\":\"Walk Dog\",\"description\":\"Finished walking baxter\",\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false}";

        when(authService.getUserId()).thenReturn(id);
        when(todoService.editTodo(any(Todo.class), any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(toEditStr)
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
    void updateTodoInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";
        final String toEditStr = "{\"id\":\"08b2a786-39f8-4752-b4c8-d0c26c01de32\",\"name\":\"Walk Dog\",\"description\":\"Finished walking baxter\",\"startDate\":\"2020-07-31\",\"endDate\":null,\"finished\":false}";

        when(authService.getUserId()).thenReturn(id);
        when(todoService.editTodo(any(Todo.class), any(UUID.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURL + "/" + testUUIDStr)
                        .content(toEditStr)
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
    void updateTodoException() throws Exception {
        final String expected = "{\"message\":\"Server Exception\",\"name\":\"Exception\",\"errors\":null,\"timestamp\":";

        when(mockMvc.perform(delete(baseURL + "/" + testUUIDStr))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(put(baseURL + "/" + testUUIDStr))
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
        mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteByIdForbidden() throws Exception {
        mockMvc.perform(delete(baseURL + "/" + testUUIDStr))
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
                put(baseURL + "/" + testUUIDStr)
                        .content(objectMapper.writeValueAsString(testTodo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}