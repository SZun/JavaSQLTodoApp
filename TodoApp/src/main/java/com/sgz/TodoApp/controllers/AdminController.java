package com.sgz.TodoApp.controllers;

import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.exceptions.*;
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

    @PostMapping("users")
    public ResponseEntity<ApplicationUser> createUser(@Valid @RequestBody ApplicationUser toAdd) throws InvalidEntityException, InvalidNameException {
        return new ResponseEntity(service.createUser(toAdd), HttpStatus.CREATED);
    }

    @GetMapping("users")
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
    public ResponseEntity<Integer> deleteUserById(@PathVariable int id) throws InvalidIdException {
        service.deleteUserById(id);
        return new ResponseEntity(id, HttpStatus.OK);
    }

    @GetMapping("roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() throws NoItemsException {
        return ResponseEntity.ok(service.getAllRoles());
    }

    @GetMapping("roles/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> getAllRoleById(@PathVariable int id) throws InvalidIdException {
        return ResponseEntity.ok(service.getRoleById(id));
    }

    @PostMapping("roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role toAdd) throws InvalidEntityException, InvalidAuthorityException {
        return ResponseEntity.ok(service.createRole(toAdd));
    }

    @PutMapping("roles/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> editRole(@PathVariable int id, @Valid @RequestBody Role toEdit) throws InvalidEntityException, InvalidIdException, InvalidAuthorityException {
        try {
            Role toCheck = service.getRoleByAuthority(toEdit.getAuthority());
            if(toCheck.getId() != id){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch(InvalidAuthorityException | InvalidEntityException ex){}

        return ResponseEntity.ok(service.editRole(toEdit));
    }

    @DeleteMapping("roles/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN)")
    public ResponseEntity<Integer> deleteRoleById(@PathVariable int id) throws InvalidIdException {
        service.deleteRoleById(id);
        return new ResponseEntity(id, HttpStatus.OK);
    }

}
