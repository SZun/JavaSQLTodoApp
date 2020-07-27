package com.sgz.TodoApp.controllers;

import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    AdminService service;

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApplicationUser> editUser(@PathVariable int id, @RequestBody ApplicationUser toEdit) throws InvalidEntityException, InvalidIdException {
        toEdit.setId(id);
        return new ResponseEntity(service.updateUserRole(toEdit), HttpStatus.OK);
    }

}
