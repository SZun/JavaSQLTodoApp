//package com.sgz.TodoApp.controllers;
//
//import com.google.common.collect.Sets;
//import com.sgz.TodoApp.entities.User;
//import com.sgz.TodoApp.exceptions.*;
//import com.sgz.TodoApp.services.AuthService;
//import com.sgz.TodoApp.services.RoleService;
//import com.sgz.TodoApp.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.nio.file.AccessDeniedException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/users/")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoleService roleService;
//
//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("create")
//    public ResponseEntity<User> createUser(@Valid @RequestBody User toAdd) throws InvalidEntityException, InvalidNameException, InvalidAuthorityException {
//        toAdd.setRoles(Sets.newHashSet(roleService.getRoleByAuthority("USER")));
//        return new ResponseEntity(userService.createUser(toAdd), HttpStatus.CREATED);
//    }
//
//    @GetMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<List<User>> getAllUsers() throws NoItemsException {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//    @GetMapping("{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<User> getUserById(@PathVariable int id) throws InvalidIdException {
//        return ResponseEntity.ok(userService.getUserById(id));
//    }
//
//    @PutMapping("{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<User> updateUserById(@PathVariable int id, @Valid @RequestBody User toEdit) throws InvalidEntityException, InvalidIdException, InvalidAuthorityException, InvalidNameException, AccessDeniedException {
//        try {
//            User toCheck = userService.getUserByName(toEdit.getUsername());
//
//            if (toCheck.getId() != authService.getUserId()) {
//                return new ResponseEntity(HttpStatus.BAD_REQUEST);
//            }
//        } catch (InvalidNameException | InvalidEntityException ex) {
//        }
//
//        toEdit.setId(id);
//        toEdit.setRoles(Sets.newHashSet(roleService.getRoleByAuthority("USER")));
//        return new ResponseEntity(userService.editUser(toEdit, authService.getUserId()), HttpStatus.OK);
//    }
//
//    @DeleteMapping("{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<Integer> deleteUserById(@PathVariable int id) throws InvalidIdException, InvalidEntityException, InvalidNameException, AccessDeniedException {
//        userService.deleteUserById(id, authService.getUserId());
//        return ResponseEntity.ok(id);
//    }
//
//}
