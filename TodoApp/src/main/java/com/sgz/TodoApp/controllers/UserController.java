package com.sgz.TodoApp.controllers;

import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.*;
import com.sgz.TodoApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody User toAdd) throws InvalidEntityException, InvalidNameException, InvalidAuthorityException {
        return new ResponseEntity(service.createUser(toAdd), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<User>> getAllUsers() throws NoItemsException {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<User> getUserById(@PathVariable int id) throws InvalidIdException {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<User> updateUserById(@PathVariable int id, @Valid @RequestBody User toEdit) throws InvalidEntityException, InvalidIdException, InvalidAuthorityException {

        try {
            User toCheck = service.getUserByName(toEdit.getUsername());
            if(toCheck.getId() != id){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch(InvalidNameException | InvalidEntityException ex){}

        toEdit.setId(id);
        return new ResponseEntity(service.editUser(toEdit), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<User> deleteUserById(@PathVariable int id) throws InvalidIdException {
        service.deleteUserById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
