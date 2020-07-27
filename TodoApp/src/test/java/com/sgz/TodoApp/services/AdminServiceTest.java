package com.sgz.TodoApp.services;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.TestAppConfig;
import com.sgz.TodoApp.entities.ApplicationUser;
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

    private final Set<Role> testRoles = Sets.newHashSet(new Role(1, "USER"));

    private final Role testRole = new Role(1, "USER");

    private final String testLongString = "C39V2iGLMtU1xN8tctQQVPnr7Y41mgIqCCPKookK7yrKP9xweAp6Oo7NGOBp6wkWIP1cQZvxW2n40ZK0vUUHWxQzhjUCRnUXFx1uSSKXYP37nlsLcMnmaxpnGY7JGmKap7Q4e1mdtVg3aZ829B3IeMCzxTs2Ex5IOrbgu55cwUKh3z7GBFssVQL4mzr1eHqfOv67prPQgcCQCDIRSEZH1tt0h5yxVgVt2prBdgUWBmo6sg6UPS6k1quBYGDoFBIk";

    @BeforeEach
    void setUp() throws InvalidEntityException, InvalidNameException, InvalidAuthorityException {
        uRepo.deleteAll();
        rRepo.deleteAll();
        jdbc.update("ALTER TABLE Users auto_increment = 1");
        jdbc.update("ALTER TABLE Roles auto_increment = 1");
        toTest.createRole(new Role("USER"));
        toTest.createRole(new Role("ADMIN"));
        toTest.createUser(new ApplicationUser(testRoles, "@amBam20","Sam"));
        toTest.createUser(new ApplicationUser(testRoles, "@amBam20","Sam2"));
        toTest.createUser(new ApplicationUser(testRoles, "@amBam20","Sam3"));
    }

    @Test
    void updateUserRole() throws InvalidIdException, InvalidEntityException {
        Set<Role> newRoles = Sets.newHashSet(new Role(1, "USER"), new Role(2, "ADMIN"));

        ApplicationUser original = new ApplicationUser(1, testRoles, "@amBam20", "Sam");

        ApplicationUser fromService = toTest.getUserById(1);

        assertEquals(original.getId(), fromService.getId());
        assertEquals(original.getAuthorities(), fromService.getAuthorities());
        assertTrue(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertEquals(original.getUsername(), fromService.getUsername());

        ApplicationUser expected = new ApplicationUser(1, newRoles, fromService.getPassword(), "Sam");

        fromService = toTest.updateUserRole(expected);
        assertEquals(fromService, expected);

        assertEquals(original.getId(), fromService.getId());
        assertNotEquals(original.getAuthorities(), fromService.getAuthorities());
        assertTrue(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertEquals(original.getUsername(), fromService.getUsername());

        fromService = toTest.getUserById(1);
        assertEquals(fromService, expected);
    }

    @Test
    void updateUserRoleInvalidId() throws InvalidEntityException {
        ApplicationUser testUser = new ApplicationUser(-1, testRoles, "@amBam20", "Sam");
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
        ApplicationUser testUser = new ApplicationUser(1, new HashSet<>(), "@amBam20", "Sam");
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRoleNullAuthorities() throws InvalidIdException {
        ApplicationUser testUser = new ApplicationUser(1, null, "@amBam20", "Sam");
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRoleNameDiff() throws InvalidIdException {
        ApplicationUser testUser = new ApplicationUser(1, testRoles, "@amBam20", "Sammy");
        try {
            toTest.updateUserRole(testUser);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex) {}
    }

    @Test
    void updateUserRolePasswordDiff() throws InvalidIdException {
        ApplicationUser testUser = new ApplicationUser(1, testRoles, "@amBam22", "Sam");
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
        ApplicationUser expected = new ApplicationUser(1,testRoles,"@amBam20", "Sam");

        ApplicationUser fromService = toTest.getUserByName("Sam");

        assertEquals(expected.getId(), fromService.getId());
        assertEquals(expected.getAuthorities(), fromService.getAuthorities());
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
        ApplicationUser toAdd = new ApplicationUser(testRoles,"@amBam20", "Sammy");

        ApplicationUser fromService = toTest.createUser(toAdd);
        assertEquals(toAdd, fromService);
    }

    @Test
    void createUserInvalidName() throws InvalidEntityException {
        ApplicationUser toAdd = new ApplicationUser(testRoles,"@amBam20", "Sam");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidNameException");
        } catch (InvalidNameException ex){}
    }

    @Test
    void createUserEmptyPassword() throws InvalidNameException {
        ApplicationUser toAdd = new ApplicationUser(testRoles,"", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserEmptyName() throws InvalidNameException {
        ApplicationUser toAdd = new ApplicationUser(testRoles,"@amBam20", "");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserBlankPassword() throws InvalidNameException {
        ApplicationUser toAdd = new ApplicationUser(testRoles,"   ", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserBlankName() throws InvalidNameException {
        ApplicationUser toAdd = new ApplicationUser(testRoles,"@amBam20", "   ");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserTooLongPassword() throws InvalidNameException {
        ApplicationUser toAdd = new ApplicationUser(testRoles,testLongString, "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserTooLongName() throws InvalidNameException {
        ApplicationUser toAdd = new ApplicationUser(testRoles,"@amBam20", testLongString);
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void createUserInvalidPassword() throws InvalidNameException {
        ApplicationUser toAdd = new ApplicationUser(testRoles,"password", "Sammy");
        try {
            toTest.createUser(toAdd);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUser() throws InvalidIdException, InvalidEntityException {
        ApplicationUser original = new ApplicationUser(1,testRoles,"@amBam20", "Sam");

        ApplicationUser fromService = toTest.getUserById(1);

        assertEquals(original.getId(), fromService.getId());
        assertEquals(original.getAuthorities(), fromService.getAuthorities());
        assertTrue(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertEquals(original.getUsername(), fromService.getUsername());

        ApplicationUser expected = new ApplicationUser(1, testRoles,"@amBam21", "Sammy");
        fromService = toTest.editUser(expected);

        assertEquals(original.getId(), fromService.getId());
        assertEquals(original.getAuthorities(), fromService.getAuthorities());
        assertEquals(expected,fromService);
        assertFalse(passwordEncoder.matches(original.getPassword(), fromService.getPassword()));
        assertNotEquals(original.getUsername(), fromService.getUsername());
    }


    @Test
    void editUserEmptyPassword() throws InvalidIdException {
        ApplicationUser toEdit = new ApplicationUser(1,testRoles,"", "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserEmptyName() throws InvalidIdException {
        ApplicationUser toEdit = new ApplicationUser(1,testRoles,"@amBam20", "");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserTooLongPassword() throws InvalidIdException {
        ApplicationUser toEdit = new ApplicationUser(1,testRoles,testLongString, "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserTooLongName() throws InvalidIdException {
        ApplicationUser toEdit = new ApplicationUser(1,testRoles,"@amBam20", testLongString);
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserBlankPassword() throws InvalidIdException {
        ApplicationUser toEdit = new ApplicationUser(1,testRoles,"  ", "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserBlankName() throws InvalidIdException {
        ApplicationUser toEdit = new ApplicationUser(1,testRoles,"@amBam20", "  ");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserInvalidPassword() throws InvalidIdException {
        ApplicationUser toEdit = new ApplicationUser(1,testRoles,"password", "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editUserInvalidId() throws InvalidEntityException {
        ApplicationUser toEdit = new ApplicationUser(-1,testRoles,"@amBam20", "Sammy");
        try {
            toTest.editUser(toEdit);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void getUserById() throws InvalidIdException {
        ApplicationUser expected = new ApplicationUser(1,testRoles,"@amBam20", "Sam");

        ApplicationUser fromService = toTest.getUserById(1);

        assertEquals(expected.getId(), fromService.getId());
        assertEquals(expected.getAuthorities(), fromService.getAuthorities());
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
        ApplicationUser expected = new ApplicationUser(1,testRoles,"@amBam20", "Sam");

        ApplicationUser fromService = toTest.getUserById(1);

        assertEquals(expected.getId(), fromService.getId());
        assertEquals(expected.getAuthorities(), fromService.getAuthorities());
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
        Role expected = new Role(2, "ADMIN");

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
        Role toEdit = new Role(1,"GUEST");

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
        Role toEdit = new Role(-1,"GUEST");
        try {
            toTest.editRole(toEdit);
            fail("should hit InvalidIdException");
        } catch (InvalidIdException ex){}
    }

    @Test
    void editRoleNullRole() throws InvalidIdException {
        Role toEdit = new Role(1,"GUEST");
        try {
            toTest.editRole(null);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editRoleEmptyAuthority() throws InvalidIdException {
        Role toEdit = new Role(1,"");
        try {
            toTest.editRole(toEdit);
            fail("should hit InvalidEntityException");
        } catch (InvalidEntityException ex){}
    }

    @Test
    void editRoleBlankAuthority() throws InvalidIdException {
        Role toEdit = new Role(1,"  ");
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