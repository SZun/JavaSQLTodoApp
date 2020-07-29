package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.TestAppConfig;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.exceptions.*;
import com.sgz.TodoApp.repos.RoleRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestAppConfig.class)
class AdminServiceTest {

    @Autowired
    private AdminService toTest;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRepo uRepo;

    @Autowired
    private RoleRepo rRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Set<Role> testRoles = Sets.newHashSet(new Role(1, "USER", null));

    private final Role testRole = new Role(1, "USER", null);

    private final String testLongString = "C39V2iGLMtU1xN8tctQQVPnr7Y41mgIqCCPKookK7yrKP9xweAp6Oo7NGOBp6wkWIP1cQZvxW2n40ZK0vUUHWxQzhjUCRnUXFx1uSSKXYP37nlsLcMnmaxpnGY7JGmKap7Q4e1mdtVg3aZ829B3IeMCzxTs2Ex5IOrbgu55cwUKh3z7GBFssVQL4mzr1eHqfOv67prPQgcCQCDIRSEZH1tt0h5yxVgVt2prBdgUWBmo6sg6UPS6k1quBYGDoFBIk";

    @BeforeEach
    void setUp() throws InvalidEntityException, InvalidNameException, InvalidAuthorityException {
        uRepo.deleteAll();
        rRepo.deleteAll();
        jdbc.update("ALTER TABLE Users auto_increment = 1");
        jdbc.update("ALTER TABLE Roles auto_increment = 1");
        toTest.createRole(new Role("USER"));
        toTest.createRole(new Role("ADMIN"));
        toTest.createUser(new User(testRoles, "@amBam20","Sam"));
        toTest.createUser(new User(testRoles, "@amBam20","Sam2"));
        toTest.createUser(new User(testRoles, "@amBam20","Sam3"));
    }

    @Test
    void updateUserRole() throws InvalidIdException, InvalidEntityException {
        Set<Role> newRoles = Sets.newHashSet(new Role(1, "USER", null), new Role(2, "ADMIN", null));

        User original = new User(1, "@amBam20", "Sam", testRoles);

        User fromService = toTest.getUserById(1);

        assertEquals(original.getId(), fromService.getId());
        assertEquals(original.getRoles(), fromService.getRoles());
        assertTrue(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertEquals(original.getUsername(), fromService.getUsername());

        User expected = new User(1, fromService.getPassword(), "Sam", testRoles);

        fromService = toTest.updateUserRole(expected);
        assertEquals(fromService, expected);

        assertEquals(original.getId(), fromService.getId());
        assertNotEquals(original.getRoles(), fromService.getRoles());
        assertTrue(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertEquals(original.getUsername(), fromService.getUsername());

        fromService = toTest.getUserById(1);
        assertEquals(fromService, expected);
    }

    @Test
    void updateUserRoleInvalidId() throws InvalidEntityException {
        User testUser = new User(-1, "@amBam20", "Sam", testRoles);
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex) {}
    }

    @Test
    void updateUserRoleNullUser() throws InvalidIdException {
        try {
            toTest.updateUserRole(null);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRoleEmptyAuthorities() throws InvalidIdException {
        User testUser = new User(1, "@amBam20", "Sam", new HashSet<>());
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRoleNullAuthorities() throws InvalidIdException {
        User testUser = new User(1, "@amBam20", "Sam", null);
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRoleNameDiff() throws InvalidIdException {
        User testUser = new User(1, "@amBam20", "Sammy", testRoles);
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRolePasswordDiff() throws InvalidIdException {
        User testUser = new User(1, "@amBam22", "Sam", testRoles);
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void getAllNoItems() {
        uRepo.deleteAll();
        try {
            toTest.getAll();
            fail("should hit NoItemsException");
        } catch (NoItemsException ex) {}
    }

    @Test
    void getUserByName() throws InvalidNameException, InvalidEntityException {
        User expected = new User(1,"@amBam20", "Sam", testRoles);

        User fromService = toTest.getUserByName("Sam");

        assertEquals(expected.getId(), fromService.getId());
        assertEquals(expected.getRoles(), fromService.getRoles());
        assertTrue(passwordEncoder.matches(expected.getPassword(), fromService.getPassword()));
        assertEquals(expected.getUsername(), fromService.getUsername());
    }

    @Test
    void getUserByNameNullName() throws InvalidNameException {
        try {
            toTest.getUserByName(null);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    void getUserByNameEmptyName() throws InvalidNameException {
        try {
            toTest.getUserByName("");
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    void getUserByNameBlankName() throws InvalidNameException {
        try {
            toTest.getUserByName("  ");
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    void getUserByNameInvalidName() throws InvalidEntityException {
        try {
            toTest.getUserByName("Non Existent Name");
            fail("should hit InvalidNameException");
        } catch(InvalidNameException ex){}
    }

    @Test
    void createUser() throws InvalidEntityException, InvalidNameException {
        User toAdd = new User(testRoles,"@amBam20", "Sammy");

        User fromService = toTest.createUser(toAdd);
        assertEquals(toAdd, fromService);
    }

    @Test
    void createUserInvalidName() throws InvalidEntityException {
        User toAdd = new User(testRoles,"@amBam20", "Sam");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidNameException");
        } catch (InvalidNameException ex){}
    }

    @Test
    void createUserEmptyPassword() throws InvalidNameException {
        User toAdd = new User(testRoles,"", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserEmptyName() throws InvalidNameException {
        User toAdd = new User(testRoles,"@amBam20", "");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserBlankPassword() throws InvalidNameException {
        User toAdd = new User(testRoles,"   ", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserBlankName() throws InvalidNameException {
        User toAdd = new User(testRoles,"@amBam20", "   ");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserTooLongPassword() throws InvalidNameException {
        User toAdd = new User(testRoles,testLongString, "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserTooLongName() throws InvalidNameException {
        User toAdd = new User(testRoles,"@amBam20", testLongString);
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserInvalidPassword() throws InvalidNameException {
        User toAdd = new User(testRoles,"password", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUser() throws InvalidIdException, InvalidEntityException {
        User original = new User(1,"@amBam20", "Sam", testRoles);

        User fromService = toTest.getUserById(1);

        assertEquals(original.getId(), fromService.getId());
        assertEquals(original.getRoles(), fromService.getRoles());
        assertTrue(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertEquals(original.getUsername(), fromService.getUsername());

        User expected = new User(1,"@amBam21", "Sammy", testRoles);
        fromService = toTest.editUser(expected);

        assertEquals(original.getId(), fromService.getId());
        assertEquals(original.getRoles(), fromService.getRoles());
        assertEquals(expected,fromService);
        assertFalse(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertNotEquals(original.getUsername(), fromService.getUsername());
    }


    @Test
    void editUserEmptyPassword() throws InvalidIdException {
        User toEdit = new User(1,"", "Sammy", testRoles);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserEmptyName() throws InvalidIdException {
        User toEdit = new User(1,"@amBam20", "", testRoles);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserTooLongPassword() throws InvalidIdException {
        User toEdit = new User(1,testLongString, "Sammy", testRoles);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserTooLongName() throws InvalidIdException {
        User toEdit = new User(1,"@amBam20", testLongString, testRoles);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserBlankPassword() throws InvalidIdException {
        User toEdit = new User(1,"  ", "Sammy", testRoles);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserBlankName() throws InvalidIdException {
        User toEdit = new User(1,"@amBam20", "  ", testRoles);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserInvalidPassword() throws InvalidIdException {
        User toEdit = new User(1,"password", "Sammy", testRoles);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserInvalidId() throws InvalidEntityException {
        User toEdit = new User(-1,"@amBam20", "Sammy", testRoles);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void getUserById() throws InvalidIdException {
        User expected = new User(1,"@amBam20", "Sam", testRoles);

        User fromService = toTest.getUserById(1);

        assertEquals(expected.getId(), fromService.getId());
        assertEquals(expected.getRoles(), fromService.getRoles());
        assertTrue(passwordEncoder.matches(expected.getPassword(), fromService.getPassword()));
        assertEquals(expected.getUsername(), fromService.getUsername());
    }

    @Test
    void getUserByIdInvalidId() {
        try {
            toTest.getUserById(-1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void deleteUserById() throws InvalidIdException {
        User expected = new User(1,"@amBam20", "Sam", testRoles);

        User fromService = toTest.getUserById(1);

        assertEquals(expected.getId(), fromService.getId());
        assertEquals(expected.getRoles(), fromService.getRoles());
        assertTrue(passwordEncoder.matches(expected.getPassword(), fromService.getPassword()));
        assertEquals(expected.getUsername(), fromService.getUsername());

        try {
            toTest.getUserById(-1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void deleteUserByIdInvalidId() {
        try {
            toTest.deleteUserById(-1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void getAllRoles() throws NoItemsException {
        Role expected = new Role(2, "ADMIN", null);

        List<Role> fromService = toTest.getAllRoles();

        assertEquals(2, fromService.size());
        assertTrue(fromService.contains(testRole));
        assertTrue(fromService.contains(expected));
    }

    @Test
    void getAllRolesNoItems() {
        uRepo.deleteAll();
        rRepo.deleteAll();
        try {
            toTest.getAllRoles();
            fail("should hit NoItemsException");
        } catch (NoItemsException ex){}
    }

    @Test
    void getRoleById() throws InvalidIdException {
        Role fromService = toTest.getRoleById(1);
        assertEquals(testRole, fromService);
    }

    @Test
    void getRoleByIdInvalidId() {
        try {
            toTest.getRoleById(-1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void getRoleByAuthority() throws InvalidAuthorityException, InvalidEntityException {
        Role fromService = toTest.getRoleByAuthority("USER");
        assertEquals(testRole, fromService);
    }

    @Test
    void getRoleByAuthorityInvalidAuthority() throws InvalidEntityException {
        try {
            toTest.getRoleByAuthority("banan");
            fail("should hit InvalidAuthorityException");
        } catch (InvalidAuthorityException ex){}
    }

    @Test
    void getRoleByAuthorityNullAuthority() throws InvalidAuthorityException {
        try {
            toTest.getRoleByAuthority(null);
            fail("should hit InvalidAuthorityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void getRoleByAuthorityBlankAuthority() throws InvalidAuthorityException {
        try {
            toTest.getRoleByAuthority("  ");
            fail("should hit InvalidAuthorityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void getRoleByAuthorityEmptyAuthority() throws InvalidAuthorityException {
        try {
            toTest.getRoleByAuthority("");
            fail("should hit InvalidAuthorityException");
        } catch (InvalidEntityException ex){}
    }


    @Test
    void createRole() throws InvalidEntityException, InvalidAuthorityException, InvalidIdException {
        Role expected = new Role("GUEST");

        try {
            toTest.getRoleById(3);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}

        Role fromService = toTest.createRole(expected);
        expected.setId(3);
        assertEquals(expected, fromService);

        fromService = toTest.getRoleById(3);
        assertEquals(expected, fromService);
    }

    @Test
    void createRoleInvalidAuthority() throws InvalidEntityException {
        Role toAdd = new Role("USER");
        try {
            toTest.createRole(toAdd);
            fail("should hit InvalidAuthorityException");
        } catch(InvalidAuthorityException ex){}
    }

    @Test
    void createRoleNullRole() throws InvalidAuthorityException {
        try {
            toTest.createRole(null);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    void createRoleEmptyAuthority() throws InvalidAuthorityException {
        Role toAdd = new Role("");
        try {
            toTest.createRole(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    void createRoleBlankAuthority() throws InvalidAuthorityException {
        Role toAdd = new Role("  ");
        try {
            toTest.createRole(toAdd);
            fail("should hit InvalidEntityException");
        } catch(InvalidEntityException ex){}
    }

    @Test
    void editRole() throws InvalidEntityException, InvalidIdException {
        Role toEdit = new Role(1,"GUEST", new ArrayList<>());

        Role fromService = toTest.getRoleById(1);
        assertEquals(testRole, fromService);

        fromService = toTest.editRole(toEdit);
        assertEquals(toEdit, fromService);

        fromService = toTest.getRoleById(1);
        assertNotEquals(testRole, fromService);
        assertEquals(toEdit, fromService);
    }

    @Test
    void editRoleInvalidId() throws InvalidEntityException {
        Role toEdit = new Role(-1,"GUEST", null);
        try {
            toTest.editRole(toEdit);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void editRoleNullRole() throws InvalidIdException {
        Role toEdit = new Role(1,"GUEST", null);
        try {
            toTest.editRole(null);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editRoleEmptyAuthority() throws InvalidIdException {
        Role toEdit = new Role(1,"", null);
        try {
            toTest.editRole(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editRoleBlankAuthority() throws InvalidIdException {
        Role toEdit = new Role(1,"  ", null);
        try {
            toTest.editRole(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void deleteRoleById() throws InvalidIdException {
        Role fromService = toTest.getRoleById(1);
        assertEquals(testRole, fromService);

        toTest.deleteRoleById(1);

        try {
            toTest.getRoleById(1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void deleteRoleByIdInvalidId() {
        try {
            toTest.deleteRoleById(-1);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }


}