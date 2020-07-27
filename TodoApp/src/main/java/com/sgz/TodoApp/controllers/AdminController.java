package com.sgz.TodoApp.controllers;

import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.InvalidNameException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {

    @Autowired
    AdminService service;

    @PutMapping("users/role/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApplicationUser> editUser(@PathVariable int id, @RequestBody ApplicationUser toEdit) throws InvalidEntityException, InvalidIdException {
        toEdit.setId(id);
        return new ResponseEntity(service.updateUserRole(toEdit), HttpStatus.OK);
    }

    @PostMapping("users/create")
    public ResponseEntity<ApplicationUser> createUser(@Valid @RequestBody ApplicationUser toAdd) throws InvalidEntityException, InvalidNameException {
        return new ResponseEntity(service.createUser(toAdd), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ApplicationUser>> getAllUsers() throws NoItemsException {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("users/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApplicationUser> getUserById(@PathVariable int id) throws InvalidIdException {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @PutMapping("users/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApplicationUser> updateUserById(@PathVariable int id, @Valid @RequestBody ApplicationUser toEdit) throws InvalidEntityException, InvalidIdException {

        try {
            ApplicationUser toCheck = service.getUserByName(toEdit.getUsername());
            if(toCheck.getId() != id){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch(InvalidNameException | InvalidEntityException ex){}

        toEdit.setId(id);
        return new ResponseEntity(service.editUser(toEdit), HttpStatus.OK);
    }

    @DeleteMapping("users/{id}")
    @PreAuthorize("hasRole(ROLE_ADMIN)")
    public ResponseEntity<ApplicationUser> deleteUserById(@PathVariable int id) throws InvalidIdException {
        service.deleteUserById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
