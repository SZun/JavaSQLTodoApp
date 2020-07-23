/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.entities.controllers;

import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.services.TodoService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author samg.zun
 */
@RestController
@RequestMapping("/api")
public class TodoController {
    
    @Autowired
    TodoService service;
    
    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getAll() throws NoItemsException{
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/todo/{id}")
    public ResponseEntity<Todo> getById(@PathVariable int id) throws InvalidIdException {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @DeleteMapping("/todo/{id}")
    public ResponseEntity deleteById(@PathVariable int id) throws InvalidIdException {
        service.deleteTodo(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping("/todos")
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo toAdd) throws InvalidEntityException{
        return new ResponseEntity(service.createTodo(toAdd), HttpStatus.CREATED);
    }
    
    @PutMapping("/todos")
    public ResponseEntity<Todo> updateTodo(@Valid @RequestBody Todo toAdd) throws InvalidEntityException, InvalidIdException{
        return new ResponseEntity(service.editTodo(toAdd), HttpStatus.OK);
    }
}
