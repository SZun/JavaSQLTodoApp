package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.repos.RoleRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Autowired
    public AdminService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public Role updateRoleUsers(Role toEdit) throws InvalidEntityException {
        validateRole(toEdit);
        return roleRepo.save(toEdit);
    }

    public User updateUserRoles(User toEdit) throws InvalidEntityException {
        validateUser(toEdit);
        return userRepo.save(toEdit);
    }

    private void validateUser(User toEdit) throws InvalidEntityException {
        if (toEdit == null
                || toEdit.getUsername().trim().isEmpty()
                || toEdit.getUsername().trim().length() > 50
                || toEdit.getPassword().trim().isEmpty()
                || toEdit.getRoles() == null
                || toEdit.getRoles().isEmpty()
        ) {
            throw new InvalidEntityException("Invalid Entity");
        }
    }

    private void validateRole(Role toEdit) throws InvalidEntityException {
        if (toEdit == null
                || toEdit.getAuthority().trim().isEmpty()
                || toEdit.getAuthority().trim().length() > 50
                || toEdit.getUsers() == null
                || toEdit.getUsers().isEmpty()) {
            throw new InvalidEntityException("Invalid entity");
        }
    }
}
