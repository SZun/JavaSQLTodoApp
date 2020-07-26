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
import com.sgz.TodoApp.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author samg.zun
 */
@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {
    
    @Autowired
    TodoService service;
    
    @GetMapping
    public ResponseEntity<List<Todo>> getAll() throws NoItemsException{
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable int id) throws InvalidIdException {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id) throws InvalidIdException {
        service.deleteTodo(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo toAdd) throws InvalidEntityException{
        return new ResponseEntity(service.createTodo(toAdd), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable int id, @Valid @RequestBody Todo toAdd) throws InvalidEntityException, InvalidIdException{
        toAdd.setId(id);
        return new ResponseEntity(service.editTodo(toAdd), HttpStatus.OK);
    }
}
