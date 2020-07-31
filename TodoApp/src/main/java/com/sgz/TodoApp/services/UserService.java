package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.*;
import com.sgz.TodoApp.repos.RoleRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User toAdd) throws InvalidEntityException, InvalidNameException, InvalidAuthorityException {
        validate(toAdd);
        checkExistsByUsername(toAdd.getUsername());

        toAdd.setRoles(Sets.newHashSet(getRoleByAuthority("USER")));
        toAdd.setPassword(passwordEncoder.encode(toAdd.getPassword()));

        return userRepo.save(toAdd);
    }

    public List<User> getAllUsers() throws NoItemsException {
        List<User> allUsers = userRepo.findAll();
        if (allUsers.isEmpty()) {
            throw new NoItemsException("No Items");
        }

        return allUsers;
    }

    public User getUserByName(String username) throws InvalidEntityException, InvalidNameException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidEntityException("Name is invalid");
        }

        Optional<User> toGet = userRepo.findByUsername(username);
        if (!toGet.isPresent()) {
            throw new InvalidNameException("Name not found");
        }

        return toGet.get();
    }

    public User editUser(User toEdit, int authId) throws InvalidEntityException, InvalidIdException, InvalidAuthorityException, InvalidNameException, AccessDeniedException {
        isAuthoritzed(toEdit.getId(), authId);
        validate(toEdit);
        checkExists(toEdit.getId());
        checkExistsByUsername(toEdit.getUsername());

        toEdit.setRoles(Sets.newHashSet(getRoleByAuthority("USER")));
        toEdit.setPassword(passwordEncoder.encode(toEdit.getPassword()));

        return userRepo.save(toEdit);
    }

    public User getUserById(int id) throws InvalidIdException {
        Optional<User> toGet = userRepo.findById(id);
        if (!toGet.isPresent()) {
            throw new InvalidIdException("Invalid Id");
        }

        return toGet.get();
    }

    public void deleteUserById(int writeId, int authId) throws InvalidIdException, InvalidNameException, InvalidEntityException, AccessDeniedException {
        isAuthoritzed(writeId, authId);
        checkExists(writeId);
        userRepo.deleteById(writeId);
    }

    private Role getRoleByAuthority(String authority) throws InvalidAuthorityException, InvalidEntityException {
        if (authority == null || authority.trim().isEmpty()) {
            throw new InvalidEntityException("Name is invalid");
        }

        Optional<Role> toGet = roleRepo.findByAuthority(authority);
        if (!toGet.isPresent()) {
            throw new InvalidAuthorityException("Authority not found");
        }

        return toGet.get();
    }

    private void checkExistsByUsername(String username) throws InvalidNameException {
        if (userRepo.existsByUsername(username)) {
            throw new InvalidNameException("Name already exists");
        }
    }

    private void checkExists(int id) throws InvalidIdException, InvalidNameException, InvalidEntityException {
        if (!userRepo.existsById(id)) {
            throw new InvalidIdException("Invalid Id");
        }
    }

    private void isAuthoritzed(int writeId, int authId) throws AccessDeniedException {
        if(writeId != authId) throw new AccessDeniedException("Access Denied");
    }

    private void validate(User toUpsert) throws InvalidEntityException {
        if (toUpsert == null
                || toUpsert.getUsername().trim().isEmpty()
                || toUpsert.getUsername().trim().length() > 50
                || toUpsert.getPassword().trim().isEmpty()
                || toUpsert.getPassword().trim().length() > 20
                || !toUpsert.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
        ) {
            throw new InvalidEntityException("Invalid Entity");
        }
    }

}
