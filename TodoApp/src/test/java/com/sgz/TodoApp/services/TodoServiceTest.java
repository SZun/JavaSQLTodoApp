/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.services;

import com.sgz.TodoApp.TestAppConfig;
import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.TodoRepo;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author samg.zun
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestAppConfig.class)
public class TodoServiceTest {

    @Autowired
    TodoService toTest;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    TodoRepo repo;

    Todo expected = new Todo(1, "Laundry", null, LocalDate.now(), null, false);

    public TodoServiceTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        repo.deleteAll();
        jdbc.update("ALTER TABLE Todos auto_increment = 1");
        repo.save(new Todo("Laundry", LocalDate.now(), false));
        repo.save(new Todo("Dishes", LocalDate.now(), false));
        repo.save(new Todo("Clean Up After Dog", LocalDate.now(), false));
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getAll method, of class TodoService.
     */
    @Test
    public void testGetAll() throws NoItemsException {
        Todo expected2 = new Todo(2, "Dishes", null, LocalDate.now(), null, false);
        Todo expected3 = new Todo(3, "Clean Up After Dog", null, LocalDate.now(), null, false);

        List<Todo> fromService = toTest.getAll();

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expected));
        assertTrue(fromService.contains(expected2));
        assertTrue(fromService.contains(expected3));
    }
    
    @Test
    public void testGetAllNoItems() {
        repo.deleteAll();
        
        try {
            toTest.getAll();
        } catch(NoItemsException ex){}
    }

    /**
     * Test of getById method, of class TodoService.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        
        Todo fromService = toTest.getById(1);
        
        assertEquals(expected, fromService);
        
    }

    @Test
    public void testGetByIdInvalidId() {
        try {
            toTest.getById(-1);
            fail("should hit InvalidIdException when Id is invalid");
        } catch(InvalidIdException ex){}
    }

    /**
     * Test of createTodo method, of class TodoService.
     */
    @Test
    public void testCreateTodo() throws InvalidEntityException {
    }

    @Test
    public void testCreateTodoNullEntity() {
    }

    @Test
    public void testCreateTodoNullName() {
    }

    @Test
    public void testCreateTodoNullStartDate() {
    }

    @Test
    public void testCreateTodoEmptyName() {
    }

    @Test
    public void testCreateTodoBlankName() {
    }

    @Test
    public void testCreateTodoTooLongName() {
    }

    @Test
    public void testCreateTodoTooLongDescription() {
    }

    @Test
    public void testCreateTodoPastStartDate() {
    }

    @Test
    public void testCreateTodoEndBeforeStartDate() {
    }

    @Test
    public void testCreateTodoFutureEndDate() {
    }

    /**
     * Test of editTodo method, of class TodoService.
     */
    @Test
    public void testEditTodo() throws InvalidIdException, InvalidEntityException {
    }

    @Test
    public void testEditTodoNullEntity() throws InvalidIdException {
    }

    @Test
    public void testEditTodoNullName() throws InvalidIdException {
    }

    @Test
    public void testEditTodoNullStartDate() throws InvalidIdException {
    }

    @Test
    public void testEditTodoEmptyName() throws InvalidIdException {
    }

    @Test
    public void testEditTodoBlankName() throws InvalidIdException {
    }

    @Test
    public void testEditTodoTooLongName() throws InvalidIdException {
    }

    @Test
    public void testEditTodoTooLongDescription() throws InvalidIdException {
    }

    @Test
    public void testEditTodoPastStartDate() throws InvalidIdException {
    }

    @Test
    public void testEditTodoEndBeforeStartDate() throws InvalidIdException {
    }

    @Test
    public void testEditTodoFutureEndDate() throws InvalidIdException {
    }

    @Test
    public void testEditTodoInvalidId() throws InvalidEntityException {
    }

    /**
     * Test of deleteTodo method, of class TodoService.
     */
    @Test
    public void testDeleteTodo() throws InvalidIdException {
        Todo fromService = toTest.getById(1);
        assertEquals(expected, fromService);
        
        toTest.deleteTodo(1);
        
        try {
            toTest.getById(1);
            fail("should hit InvalidIdException when Id is invalid");
        } catch(InvalidIdException ex){}
    }

    @Test
    public void testDeleteTodoInvalidId() {
        try {
            toTest.deleteTodo(-1);
            fail("should hit InvalidIdException when Id is invalid");
        } catch(InvalidIdException ex){}
    }

}
