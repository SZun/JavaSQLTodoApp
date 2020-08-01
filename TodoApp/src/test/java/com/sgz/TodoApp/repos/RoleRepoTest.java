package com.sgz.TodoApp.repos;

import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleRepoTest {

    @Mock
    private RoleRepo roleRepo;

    private final User testUser = new User(1, "@amBam20", "Sam");

    private final Role testRole = new Role(1, "USER");

    private final Role expectedRole = new Role(1, "USER", Arrays.asList(this.testUser));

    @Test
    void save(){
        given(roleRepo.save(expectedRole)).willReturn(expectedRole);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);

        Role fromRepo = roleRepo.save(expectedRole);

        verify(roleRepo).save(captor.capture());

        Role expected = captor.getValue();
        assertRoleFields(expected);

        assertEquals(expectedRole, fromRepo);
    }

    @Test
    void findById(){
        given(roleRepo.findById(anyInt())).willReturn(Optional.of(expectedRole));

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        Optional<Role> fromRepo = roleRepo.findById(1);

        verify(roleRepo).findById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedRole, fromRepo.get());
    }

    @Test
    void findByIdEmpty(){
        given(roleRepo.findById(anyInt())).willReturn(Optional.empty());

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        Optional<Role> fromRepo = roleRepo.findById(1);

        verify(roleRepo).findById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void deleteById(){
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        roleRepo.deleteById(1);

        verify(roleRepo).deleteById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
    }

    @Test
    void findByAuthority(){
        given(roleRepo.findByAuthority(anyString())).willReturn(Optional.empty());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<Role> fromRepo = roleRepo.findByAuthority("USER");

        verify(roleRepo).findByAuthority(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("USER", expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void findByAuthorityEmpty(){
        given(roleRepo.findByAuthority(anyString())).willReturn(Optional.of(expectedRole));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<Role> fromRepo = roleRepo.findByAuthority("USER");

        verify(roleRepo).findByAuthority(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("USER", expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedRole, fromRepo.get());
    }

    @Test
    void findAll(){
        final List<Role> expectedRoles = Arrays.asList(expectedRole);

        given(roleRepo.findAll()).willReturn(expectedRoles);

        List<Role> fromRepo = roleRepo.findAll();

        verify(roleRepo).findAll();

        assertEquals(expectedRoles, fromRepo);
    }

    private void assertRoleFields(Role expected) {
        assertEquals(1, expected.getId());
        assertEquals("USER", expected.getAuthority());
        assertEquals(Arrays.asList(this.testUser), expected.getUsers());
    }

}