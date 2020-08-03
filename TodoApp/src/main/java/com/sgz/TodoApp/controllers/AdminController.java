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
import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @PutMapping("/users/{id}/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> editUserRoles(@PathVariable UUID id, @RequestBody Set<UUID> roleIds) throws InvalidIdException, InvalidEntityException {
        User toEdit = userService.getUserById(id);
        Set<Role> roles = new HashSet<>();
        for (UUID roleId : roleIds) {
            roles.add(roleService.getRoleById(roleId));
        }
        toEdit.setRoles(roles);
        return ResponseEntity.ok(adminService.updateUserRoles(toEdit));
    }

}
