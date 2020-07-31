package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.*;
import com.sgz.TodoApp.repos.RoleRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Role updateRoleUsers(int id, List<Integer> userIds) throws InvalidIdException {
        List<User> users = new ArrayList<>();
        for (Integer userId : userIds) {
            users.add(getUserById(userId));
        }

        Role toEdit = getRoleById(id);
        toEdit.setUsers(users);

        return roleRepo.save(toEdit);
    }

    public User updateUserRoles(int id, Set<Integer> roleIds) throws InvalidIdException {
        Set<Role> userRoles = new HashSet<>();
        for (Integer roleId : roleIds) {
            userRoles.add(getRoleById(roleId));
        }

        User toEdit = getUserById(id);
        toEdit.setRoles(userRoles);

        return userRepo.save(toEdit);
    }


    public User createUser(User toAdd) throws InvalidEntityException, InvalidNameException {
        validateUser(toAdd);
        checkExistsByUsername(toAdd.getUsername());

        toAdd.setPassword(passwordEncoder.encode(toAdd.getPassword()));

        return userRepo.save(toAdd);
    }

    public List<User> getAllUsers() throws NoItemsException {
        List<User> allUsers = userRepo.findAll();

        if (allUsers.isEmpty()) throw new NoItemsException("No Items");

        return allUsers;
    }

    public User getUserByName(String username) throws InvalidEntityException, InvalidNameException {
        if (username == null
                || username.trim().isEmpty()
                || username.trim().length() > 50) {
            throw new InvalidEntityException("Name is invalid");
        }

        Optional<User> toGet = userRepo.findByUsername(username);

        if (!toGet.isPresent()) throw new InvalidNameException("Name not found");

        return toGet.get();
    }

    public User editUser(User toEdit) throws InvalidEntityException, InvalidIdException {
        validateUser(toEdit);
        checkUserExists(toEdit.getId());

        toEdit.setPassword(passwordEncoder.encode(toEdit.getPassword()));

        return userRepo.save(toEdit);
    }

    public User getUserById(int id) throws InvalidIdException {
        Optional<User> toGet = userRepo.findById(id);

        if (!toGet.isPresent()) throw new InvalidIdException("Invalid Id");

        return toGet.get();
    }

    public void deleteUserById(int id) throws InvalidIdException {
        checkUserExists(id);
        userRepo.deleteById(id);
    }

    public List<Role> getAllRoles() throws NoItemsException {
        List<Role> allRoles = roleRepo.findAll();

        if (allRoles.isEmpty()) throw new NoItemsException("No Items");

        return allRoles;
    }

    public Role getRoleById(int id) throws InvalidIdException {
        Optional<Role> toGet = roleRepo.findById(id);

        if (!toGet.isPresent()) throw new InvalidIdException("Invalid Id");

        return toGet.get();
    }

    public Role getRoleByAuthority(String authority) throws InvalidAuthorityException, InvalidEntityException {
        if (authority == null
                || authority.trim().isEmpty()
                || authority.trim().length() > 50) {
            throw new InvalidEntityException("Name is invalid");
        }

        Optional<Role> toGet = roleRepo.findByAuthority(authority);

        if (!toGet.isPresent()) throw new InvalidAuthorityException("Authority not found");

        return toGet.get();
    }

    public Role createRole(Role toAdd) throws InvalidEntityException, InvalidAuthorityException {
        validateRole(toAdd);
        checkExistsByAuthority(toAdd.getAuthority());

        return roleRepo.save(toAdd);
    }

    public Role editRole(Role toEdit) throws InvalidEntityException, InvalidIdException {
        validateRole(toEdit);
        checkRoleExists(toEdit.getId());

        return roleRepo.save(toEdit);
    }

    public void deleteRoleById(int id) throws InvalidIdException {
        checkRoleExists(id);
        roleRepo.deleteById(id);
    }

    private void validateUser(User toUpsert) throws InvalidEntityException {
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

    private void validateRole(Role toUpsert) throws InvalidEntityException {
        if (toUpsert == null
                || toUpsert.getAuthority().trim().isEmpty()
                || toUpsert.getAuthority().trim().length() > 50) {
            throw new InvalidEntityException("Invalid entity");
        }
    }

    private void checkExistsByAuthority(String authority) throws InvalidAuthorityException {
        if (roleRepo.existsByAuthority(authority)) throw new InvalidAuthorityException("Authority already in use");
    }

    private void checkUserExists(int id) throws InvalidIdException {
        if (!userRepo.existsById(id)) throw new InvalidIdException("Invalid Id");
    }

    private void checkRoleExists(int id) throws InvalidIdException {
        if (!roleRepo.existsById(id)) throw new InvalidIdException("Invalid Id");
    }

    private void checkExistsByUsername(String username) throws InvalidNameException {
        if (userRepo.existsByUsername(username)) throw new InvalidNameException("Name already exists");
    }
}
