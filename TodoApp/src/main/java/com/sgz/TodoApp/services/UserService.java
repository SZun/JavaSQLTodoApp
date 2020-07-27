package com.sgz.TodoApp.services;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.ApplicationRole;
import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.InvalidNameException;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser createUser(ApplicationUser toAdd) throws InvalidEntityException, InvalidNameException {
        validate(toAdd);
        checkExistsByUsername(toAdd.getUsername());

        toAdd.setAuthorities(Sets.newHashSet(new ApplicationRole(1, "USER")));
        toAdd.setPassword(passwordEncoder.encode(toAdd.getPassword()));

        return repo.save(toAdd);
    }

    public ApplicationUser editUser(ApplicationUser toEdit) throws InvalidEntityException, InvalidIdException {
        validate(toEdit);
        checkExists(toEdit.getId());

        toEdit.setAuthorities(Sets.newHashSet(new ApplicationRole(1, "USER")));
        toEdit.setPassword(passwordEncoder.encode(toEdit.getPassword()));

        return repo.save(toEdit);
    }

    public ApplicationUser getUserById(int id) throws InvalidIdException {
        Optional<ApplicationUser> toGet = repo.findById(id);
        if (!toGet.isPresent()) {
            throw new InvalidIdException("Invalid Id");
        }
        return toGet.get();
    }

    public void deleteUserById(int id) throws InvalidIdException {
        checkExists(id);
        repo.deleteById(id);
    }

    private void checkExistsByUsername(String username) throws InvalidNameException {
        if(repo.existsByUsername(username)){
            throw new InvalidNameException("Name already exists");
        }
    }

    private void checkExists(int id) throws InvalidIdException {
        if(!repo.existsById(id)){
            throw new InvalidIdException("Invalid Id");
        }
    }

    private void validate(ApplicationUser toUpsert) throws InvalidEntityException {
        if (toUpsert == null
                || Strings.isNullOrEmpty(toUpsert.getUsername().trim())
                || toUpsert.getUsername().trim().length() > 50
                || Strings.isNullOrEmpty(toUpsert.getPassword().trim())
                || toUpsert.getPassword().trim().length() > 20){
            throw new InvalidEntityException("Invalid Entity");
        }
    }

}
