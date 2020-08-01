package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.exceptions.InvalidAuthorityException;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    @Autowired
    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> getAllRoles() throws NoItemsException {
        List<Role> allRoles = roleRepo.findAll();

        if (allRoles.isEmpty()) throw new NoItemsException("No Items");

        return allRoles;
    }

    public Role getRoleById(UUID id) throws InvalidIdException {
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

    public void deleteRoleById(UUID id) throws InvalidIdException {
        checkRoleExists(id);
        roleRepo.deleteById(id);
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

    private void checkRoleExists(UUID id) throws InvalidIdException {
        if (!roleRepo.existsById(id)) throw new InvalidIdException("Invalid Id");
    }
}
