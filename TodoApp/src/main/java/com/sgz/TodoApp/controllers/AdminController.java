package com.sgz.TodoApp.controllers;

import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.services.AdminService;
import com.sgz.TodoApp.services.RoleService;
import com.sgz.TodoApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity(adminService.updateUserRoles(toEdit), HttpStatus.OK);
    }

    @PutMapping("roles/users/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> updateRolesUsers(@PathVariable int id, @RequestBody List<Integer> userIds) throws InvalidIdException, InvalidEntityException {
        Role toEdit = roleService.getRoleById(id);
        List<User> users = new ArrayList<>();
        for (Integer userId : userIds) {
            users.add(userService.getUserById(userId));
        }
        return ResponseEntity.ok(adminService.updateRoleUsers(toEdit));
    }

}
