package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepo userRepo;

    @Autowired
    public AdminService(UserRepo userRepo) {
        this.userRepo = userRepo;
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

}
