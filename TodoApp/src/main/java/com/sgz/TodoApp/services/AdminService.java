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
        if(toEdit == null) throw new InvalidEntityException("Invalid entity");

        checkExists(toEdit.getId());
        validate(toEdit, repo.findById(toEdit.getId()).get());

        return repo.save(toEdit);
    }

    private void checkExists(int id) throws InvalidIdException {
        if (!repo.existsById(id)) {
            throw new InvalidIdException("Invalid Id");
        }
    }

    private void validate(ApplicationUser toEdit, ApplicationUser original) throws InvalidEntityException {
        if (original == null
                || toEdit.getAuthorities() == null
                || toEdit.getAuthorities().isEmpty()
                || original.getId() != toEdit.getId()
                || !original.getUsername().equals(toEdit.getUsername())
                || !original.getPassword().equals(toEdit.getPassword())
        ) {
            throw new InvalidEntityException("Invalid Entity");
        }
    }
}
