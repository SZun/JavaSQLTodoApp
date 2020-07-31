package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

//import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
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

    private final String testLongString = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    @Test
    void getAllTodos() throws NoItemsException {

    }

    @Test
    void getAllTodosNoItems() {
        assertThrows(NoItemsException.class, () -> toTest.getAllTodos(1));
    }

    @Test
    void getTodoById() throws InvalidIdException {
    }

    @Test
    void getTodoByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.getTodoById(1,1));
    }

    @Test
    void createTodo() throws InvalidEntityException {
    }

    @Test
    void createTodoNullTodo() {
        assertThrows(InvalidEntityException.class, () -> toTest.createTodo(null, 1));
    }

    @Test
    void createTodoBlankName() {
        final Todo toCreate = new Todo("   ", LocalDate.now());
        assertThrows(InvalidEntityException.class, () -> toTest.createTodo(toCreate, 1));
    }

    @Test
    void createTodoEmptyName() {
        final Todo toCreate = new Todo("", LocalDate.now());
        assertThrows(InvalidEntityException.class, () -> toTest.createTodo(toCreate, 1));
    }

    @Test
    void createTodoTooLongName() {
        final Todo toCreate = new Todo(testLongString, LocalDate.now());
        assertThrows(InvalidEntityException.class, () -> toTest.createTodo(toCreate, 1));
    }

    @Test
    void createTodoTooLongDescription() {
        final Todo toCreate = new Todo("Walk Dog", testLongString, LocalDate.now(), LocalDate.now());
        assertThrows(InvalidEntityException.class, () -> toTest.createTodo(toCreate, 1));
    }

    @Test
    void createTodoEndDateBeforeStartDate() {
        final Todo toCreate = new Todo("Walk Dog", "Walk the dog", LocalDate.of(2021,01,01), LocalDate.now());
        assertThrows(InvalidEntityException.class, () -> toTest.createTodo(toCreate, 1));
    }

    @Test
    void createTodoEndDateFuture() {
        final Todo toCreate = new Todo("Walk Dog", "Walk the dog", LocalDate.now(), LocalDate.of(2021,01,01));
        assertThrows(InvalidEntityException.class, () -> toTest.createTodo(toCreate, 1));
    }

    @Test
    void editTodo() throws InvalidIdException, InvalidEntityException {
    }

    @Test
    void editTodoNullTodo() {
        assertThrows(InvalidEntityException.class, () -> toTest.editTodo(null, 1));
    }

    @Test
    void editTodoBlankName() {
        final Todo toEdit = new Todo(1,  "  ", "Finished walking baxter", LocalDate.now(), LocalDate.now(), true, this.testUser);
        assertThrows(InvalidEntityException.class, () -> toTest.editTodo(toEdit, 1));
    }

    @Test
    void editTodoEmptyName() {
        final Todo toEdit = new Todo(1,  "", "Finished walking baxter", LocalDate.now(), LocalDate.now(), true, this.testUser);
        assertThrows(InvalidEntityException.class, () -> toTest.editTodo(toEdit, 1));
    }

    @Test
    void editTodoTooLongName() {
        final Todo toEdit = new Todo(1,  testLongString, "Finished walking baxter", LocalDate.now(), LocalDate.now(), true, this.testUser);
        assertThrows(InvalidEntityException.class, () -> toTest.editTodo(toEdit, 1));
    }

    @Test
    void editTodoTooLongDescription() {
        final Todo toEdit = new Todo(1,  "Walk Dog", testLongString, LocalDate.now(), LocalDate.now(), true, this.testUser);
        assertThrows(InvalidEntityException.class, () -> toTest.editTodo(toEdit, 1));
    }

    @Test
    void editTodoEndDateBeforeStartDate() {
        final Todo toEdit = new Todo(1,  "Walk Dog", testLongString, LocalDate.of(2021,01,01), LocalDate.now(), true, this.testUser);
        assertThrows(InvalidEntityException.class, () -> toTest.editTodo(toEdit, 1));
    }

    @Test
    void editTodoEndDateFuture() {
        final Todo toEdit = new Todo(1,  "Walk Dog", testLongString,  LocalDate.now(), LocalDate.of(2021,01,01), true, this.testUser);
        assertThrows(InvalidEntityException.class, () -> toTest.editTodo(toEdit, 1));
    }

    @Test
    void editTodoInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.editTodo(expectedTodo, 1));
    }

    @Test
    void deleteTodoById() throws InvalidIdException {
    }

    @Test
    void deleteTodoByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.deleteTodoById(1, 1));
    }
}