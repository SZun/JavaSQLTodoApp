package com.sgz.TodoApp.controllers;

import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidAuthorityException;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.services.AdminService;
import com.sgz.TodoApp.services.RoleService;
import com.sgz.TodoApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @PutMapping("users/{id}/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> editUserRoles(@PathVariable int id, @RequestBody Set<Integer> roleIds) throws InvalidIdException, InvalidEntityException {
        User toEdit = userService.getUserById(id);
        Set<Role> roles = new HashSet<>();
        for (Integer roleId : roleIds) {
            roles.add(roleService.getRoleById(roleId));
        }
        return ResponseEntity.ok(adminService.updateUserRoles(toEdit));
    }

    @PutMapping("roles/{id}/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> updateRolesUsers(@PathVariable int id, @RequestBody List<Integer> userIds) throws InvalidIdException, InvalidEntityException {
        Role toEdit = roleService.getRoleById(id);
        List<User> users = new ArrayList<>();
        for (Integer userId : userIds) {
            users.add(userService.getUserById(userId));
        }
        return ResponseEntity.ok(adminService.updateRoleUsers(toEdit));
    }

    @GetMapping("roles")
    public ResponseEntity<List<Role>> getAllRoles() throws NoItemsException {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("roles/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> getAllRoleById(@PathVariable int id) throws InvalidIdException {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping("roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role toAdd) throws InvalidEntityException, InvalidAuthorityException {
        return new ResponseEntity(roleService.createRole(toAdd), HttpStatus.CREATED);
    }
    @PutMapping("roles/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> editRole(@PathVariable int id, @Valid @RequestBody Role toEdit) throws InvalidEntityException, InvalidIdException {
        try {
            Role toCheck = roleService.getRoleByAuthority(toEdit.getAuthority());
            if(toCheck.getId() != id){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch(InvalidAuthorityException | InvalidEntityException ex){}

        return ResponseEntity.ok(roleService.editRole(toEdit));
    }

    @DeleteMapping("roles/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Integer> deleteRoleById(@PathVariable int id) throws InvalidIdException {
        roleService.deleteRoleById(id);
        return ResponseEntity.ok(id);
    }

}
