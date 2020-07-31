package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.TestAppConfig;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.TodoRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest(classes = TestAppConfig.class)
class TodoServiceTest {

    @InjectMocks
    private TodoService toTest;

    @Mock
    private TodoRepo todoRepo;

    @Mock
    private UserRepo userRepo;

    private final User testUser = new User(1, "@amBam20", "Sam", Sets.newHashSet(new Role(1, "USER")));

    private final Todo testTodo = new Todo(  "Walk Dog", LocalDate.now());

    private final Todo expectedTodo = new Todo(1,  "Walk Dog", "Finished walking baxter", LocalDate.now(), LocalDate.now(), true, this.testUser);

    @Test
    @WithMockUser("Sam")
    void getAllTodos() throws NoItemsException {
////        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));
//        when(todoRepo.findAllByUser_Id(anyInt())).thenReturn(Arrays.asList(expectedTodo));
//
//        toTest.getAllTodos();
    }

    @Test
    void getAllTodosNoItems() {
    }

    @Test
    void getTodoById() throws InvalidIdException {
    }

    @Test
    void getTodoByIdInvalidId() {
    }

    @Test
    void createTodo() throws InvalidEntityException {
    }

    @Test
    void createTodoNullTodo() {
    }

    @Test
    void createTodoBlankName() {
    }

    @Test
    void createTodoEmptyName() {
    }

    @Test
    void createTodoTooLongName() {
    }

    @Test
    void createTodoTooLongDescription() {
    }

    @Test
    void createTodoEndDateBeforeStartDate() {
    }

    @Test
    void createTodoEndDateFuture() {
    }

    @Test
    void editTodo() throws InvalidIdException, InvalidEntityException {
    }

    @Test
    void editTodoNullTodo() {
    }

    @Test
    void editTodoBlankName() {
    }

    @Test
    void editTodoEmptyName() {
    }

    @Test
    void editTodoTooLongName() {
    }

    @Test
    void editTodoTooLongDescription() {
    }

    @Test
    void editTodoEndDateBeforeStartDate() {
    }

    @Test
    void editTodoEndDateFuture() {
    }

    @Test
    void editTodoInvalidId() {
    }

    @Test
    void deleteTodoById() throws InvalidIdException {
    }

    @Test
    void deleteTodoByIdInvalidId() {
    }
}