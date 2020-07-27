package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepo repo;

    @Autowired
    public AdminService(UserRepo repo) {
        this.repo = repo;
    }

    public ApplicationUser updateUserRole(ApplicationUser toEdit) throws InvalidEntityException, InvalidIdException {
        validate(toEdit);
        checkExists(toEdit.getId());

        return repo.save(toEdit);
    }

    private void checkExists(int id) throws InvalidIdException {
        if (!repo.existsById(id)) {
            throw new InvalidIdException("Invalid Id");
        }
    }

    private void validate(ApplicationUser toUpsert) throws InvalidEntityException {
        if (toUpsert == null
                || toUpsert.getUsername() == null
                || toUpsert.getUsername().trim().isEmpty()
                || toUpsert.getUsername().trim().length() > 50
                || toUpsert.getPassword().trim().isEmpty()
                || toUpsert.getPassword() == null
                || toUpsert.getPassword().trim().length() > 255
                || toUpsert.getAuthorities() == null
                || toUpsert.getAuthorities().isEmpty()
        ) {
            throw new InvalidEntityException("Invalid Entity");
        }
    }
}
