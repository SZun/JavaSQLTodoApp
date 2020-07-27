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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author samg.zun
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestAppConfig.class)
public class TodoServiceTest {

    @Autowired
    private TodoService toTest;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private TodoRepo repo;

    private final Todo testTodo = new Todo(1, "Laundry", null, LocalDate.now(), null, false);

    private final String testLongString = "C39V2iGLMtU1xN8tctQQVPnr7Y41mgIqCCPKookK7yrKP9xweAp6Oo7NGOBp6wkWIP1cQZvxW2n40ZK0vUUHWxQzhjUCRnUXFx1uSSKXYP37nlsLcMnmaxpnGY7JGmKap7Q4e1mdtVg3aZ829B3IeMCzxTs2Ex5IOrbgu55cwUKh3z7GBFssVQL4mzr1eHqfOv67prPQgcCQCDIRSEZH1tt0h5yxVgVt2prBdgUWBmo6sg6UPS6k1quBYGDoFBIk";

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
        repo.save(new Todo("Laundry", LocalDate.now()));
        repo.save(new Todo("Dishes", LocalDate.now()));
        repo.save(new Todo("Clean Up After Dog", LocalDate.now()));
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
        assertTrue(fromService.contains(testTodo));
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
        
        assertEquals(testTodo, fromService);
        
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
        Todo toAdd = new Todo("Cook", LocalDate.now());
        Todo expected = new Todo(4, "Cook", null, LocalDate.now(), null, false);
        
        Todo fromService = toTest.createTodo(toAdd);
        
        assertEquals(expected, fromService);
    }

    @Test
    public void testCreateTodoNullEntity() {
        try {
            toTest.createTodo(null);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testCreateTodoEmptyName() {
        Todo toAdd = new Todo("", LocalDate.now());
        try {
            toTest.createTodo(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testCreateTodoBlankName() {
        Todo toAdd = new Todo("  ", LocalDate.now());
        try {
            toTest.createTodo(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testCreateTodoTooLongName() {
        Todo toAdd = new Todo(testLongString, LocalDate.now());
        try {
            toTest.createTodo(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testCreateTodoTooLongDescription() {
        Todo toAdd = new Todo("Cook", LocalDate.now());
        toAdd.setDescription(testLongString);
        try {
            toTest.createTodo(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testCreateTodoPastStartDate() {
        Todo toAdd = new Todo("Cook", LocalDate.of(2020,01,01));
        try {
            toTest.createTodo(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testCreateTodoEndBeforeStartDate() {
        Todo toAdd = new Todo("Cook", LocalDate.now());
        toAdd.setEndDate(LocalDate.of(2020,01,01));
        try {
            toTest.createTodo(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testCreateTodoFutureEndDate() {
        Todo toAdd = new Todo("Cook", LocalDate.of(9999,01,01));
        toAdd.setEndDate(LocalDate.of(9999,02,01));
        try {
            toTest.createTodo(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    /**
     * Test of editTodo method, of class TodoService.
     */
    @Test
    public void testEditTodo() throws InvalidIdException, InvalidEntityException {
        Todo expected = new Todo(1, "Laundry", "Landry Done", LocalDate.now(), LocalDate.now(), true);
        
        Todo fromService = toTest.getById(1);
        assertEquals(testTodo, fromService);
        
        fromService = toTest.editTodo(expected);
        assertEquals(expected, fromService);
        
        fromService = toTest.getById(1);
        assertNotEquals(testTodo, fromService);
        assertEquals(expected, fromService);
    }

    @Test
    public void testEditTodoNullEntity() throws InvalidIdException {
        try {
            toTest.editTodo(null);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testEditTodoEmptyName() throws InvalidIdException {
        Todo editable = new Todo(1, "", null, LocalDate.now(), null, false);
        try {
            toTest.editTodo(editable);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testEditTodoBlankName() throws InvalidIdException {
        Todo editable = new Todo(1, "  ", null, LocalDate.now(), null, false);
        try {
            toTest.editTodo(editable);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testEditTodoTooLongName() throws InvalidIdException {
        Todo editable = new Todo(1, testLongString, null, LocalDate.now(), null, false);
        try {
            toTest.editTodo(editable);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testEditTodoTooLongDescription() throws InvalidIdException {
        Todo editable = new Todo(1, "Banan", null, LocalDate.now(), null, false);
        editable.setDescription(testLongString);
        try {
            toTest.editTodo(editable);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testEditTodoPastStartDate() throws InvalidIdException {
        Todo editable = new Todo(1, "Banan", null, LocalDate.of(2020,01,01), null, false);
        editable.setDescription(testLongString);
        try {
            toTest.editTodo(editable);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testEditTodoEndBeforeStartDate() throws InvalidIdException {
        Todo editable = new Todo(1, "Banan", null, LocalDate.now(), null, false);
        editable.setEndDate(LocalDate.of(2020,01,01));
        editable.setDescription(testLongString);
        try {
            toTest.editTodo(editable);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testEditTodoFutureEndDate() throws InvalidIdException {
        Todo editable = new Todo(1, "Banan", null, LocalDate.now(), null, false);
        editable.setEndDate(LocalDate.of(2021,01,01));
        editable.setDescription(testLongString);
        try {
            toTest.editTodo(editable);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    public void testEditTodoInvalidId() throws InvalidEntityException {
        Todo editable = new Todo(-1, "Banan", null, LocalDate.now(), null, false);
        try {
            toTest.editTodo(editable);
            fail("should hit InvalidIdException");
        } catch(InvalidIdException ex){}
    }

    /**
     * Test of deleteTodo method, of class TodoService.
     */
    @Test
    public void testDeleteTodo() throws InvalidIdException {
        Todo fromService = toTest.getById(1);
        assertEquals(testTodo, fromService);
        
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
