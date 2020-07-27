package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.exceptions.*;
import com.sgz.TodoApp.repos.RoleRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepo uRepo;
    private final RoleRepo rRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(UserRepo repo, PasswordEncoder passwordEncoder, RoleRepo rRepo) {
        this.uRepo = repo;
        this.rRepo = rRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser updateUserRole(ApplicationUser toEdit) throws InvalidEntityException, InvalidIdException {
        if (toEdit == null) throw new InvalidEntityException("Invalid entity");

        checkUserExists(toEdit.getId());
        validateEditRole(toEdit, uRepo.findById(toEdit.getId()).get());

        return uRepo.save(toEdit);
    }


    public ApplicationUser createUser(ApplicationUser toAdd) throws InvalidEntityException, InvalidNameException {
        validate(toAdd);
        checkExistsByUsername(toAdd.getUsername());

        toAdd.setPassword(passwordEncoder.encode(toAdd.getPassword()));

        return uRepo.save(toAdd);
    }

    public List<ApplicationUser> getAll() throws NoItemsException {
        List<ApplicationUser> allUsers = uRepo.findAll();
        if (allUsers.isEmpty()) {
            throw new NoItemsException("No Items");
        }
        return allUsers;
    }

    public ApplicationUser getUserByName(String username) throws InvalidEntityException, InvalidNameException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidEntityException("Name is invalid");
        }

        Optional<ApplicationUser> toGet = uRepo.findByUsername(username);
        if (!toGet.isPresent()) {
            throw new InvalidNameException("Name not found");
        }
        return toGet.get();
    }

    public ApplicationUser editUser(ApplicationUser toEdit) throws InvalidEntityException, InvalidIdException {
        validate(toEdit);
        checkUserExists(toEdit.getId());

        toEdit.setPassword(passwordEncoder.encode(toEdit.getPassword()));

        return uRepo.save(toEdit);
    }

    public ApplicationUser getUserById(int id) throws InvalidIdException {
        Optional<ApplicationUser> toGet = uRepo.findById(id);
        if (!toGet.isPresent()) {
            throw new InvalidIdException("Invalid Id");
        }
        return toGet.get();
    }

    public void deleteUserById(int id) throws InvalidIdException {
        checkUserExists(id);
        uRepo.deleteById(id);
    }

    public List<Role> getAllRoles() throws NoItemsException {
        List<Role> allRoles = rRepo.findAll();
        if (allRoles.isEmpty()) {
            throw new NoItemsException("No Items");
        }
        return allRoles;
    }

    public Role getRoleById(int id) throws InvalidIdException {
        Optional<Role> toGet = rRepo.findById(id);
        if (!toGet.isPresent()) {
            throw new InvalidIdException("Invalid Id");
        }
        return toGet.get();
    }

    public Role getRoleByAuthority(String authority) throws InvalidAuthorityException, InvalidEntityException {
        if (authority == null || authority.trim().isEmpty()) {
            throw new InvalidEntityException("Name is invalid");
        }

        Optional<Role> toGet = rRepo.findByAuthority(authority);
        if (!toGet.isPresent()) {
            throw new InvalidAuthorityException("Authority not found");
        }

        return toGet.get();
    }

    public Role createRole(Role toAdd) throws InvalidEntityException, InvalidAuthorityException {
        validateRole(toAdd);
        checkExistsByAuthority(toAdd.getAuthority());

        return rRepo.save(toAdd);
    }

    public Role editRole(Role toEdit) throws InvalidEntityException, InvalidIdException {
        validateRole(toEdit);
        checkRoleExists(toEdit.getId());

        return rRepo.save(toEdit);
    }

    public void deleteRoleById(int id) throws InvalidIdException {
        checkRoleExists(id);
        rRepo.deleteById(id);
    }

    private void validateEditRole(ApplicationUser toEdit, ApplicationUser original) throws InvalidEntityException {
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

    private void validate(ApplicationUser toUpsert) throws InvalidEntityException {
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
        if (rRepo.existsByAuthority(authority)) {
            throw new InvalidAuthorityException("Authority already in use");
        }
    }

    private void checkUserExists(int id) throws InvalidIdException {
        if (!uRepo.existsById(id)) {
            throw new InvalidIdException("Invalid Id");
        }
    }

    private void checkRoleExists(int id) throws InvalidIdException {
        if (!rRepo.existsById(id)) {
            throw new InvalidIdException("Invalid Id");
        }
    }

    private void checkExistsByUsername(String username) throws InvalidNameException {
        if (uRepo.existsByUsername(username)) {
            throw new InvalidNameException("Name already exists");
        }
    }
}
