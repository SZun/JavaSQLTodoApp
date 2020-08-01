/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.controllers;

import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.services.AuthService;
import com.sgz.TodoApp.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author samg.zun
 */
@RestController
@RequestMapping("/api/v1/todos/")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private AuthService authService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Todo>> getAll() throws NoItemsException {
        return ResponseEntity.ok(todoService.getAllTodos(authService.getUserId()));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Todo> getById(@PathVariable int id) throws InvalidIdException {
        return ResponseEntity.ok(todoService.getTodoById(id, authService.getUserId()));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity deleteById(@PathVariable int id) throws InvalidIdException {
        todoService.deleteTodoById(id, authService.getUserId());
        return ResponseEntity.ok(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo toAdd) throws InvalidEntityException {
        return new ResponseEntity(todoService.createTodo(toAdd, authService.getUserId()), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Todo> updateTodo(@PathVariable int id, @Valid @RequestBody Todo toEdit) throws InvalidEntityException, InvalidIdException {
        toEdit.setId(id);
        return ResponseEntity.ok(todoService.editTodo(toEdit, authService.getUserId()));
    }
}
