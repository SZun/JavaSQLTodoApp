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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleRepoTest {

    @Mock
    private RoleRepo toTest;

    private final User testUser = new User(1, "@amBam20", "Sam");

    private final Role expectedRole = new Role(1, "USER", Arrays.asList(this.testUser));

    @Test
    void save(){
        given(toTest.save(any(Role.class))).willReturn(expectedRole);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);

        Role fromRepo = toTest.save(expectedRole);

        verify(toTest).save(captor.capture());

        Role expectedParam = captor.getValue();
        assertEquals(1, expectedParam.getId());
        assertEquals("USER", expectedParam.getAuthority());
        assertEquals(Arrays.asList(this.testUser), expectedParam.getUsers());

        assertEquals(expectedRole, fromRepo);
    }

    @Test
    void findById(){
        given(toTest.findById(anyInt())).willReturn(Optional.of(expectedRole));

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        Optional<Role> fromRepo = toTest.findById(1);

        verify(toTest).findById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedRole, fromRepo.get());
    }

    @Test
    void existsById(){
        given(toTest.existsById(anyInt())).willReturn(true);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        boolean fromRepo = toTest.existsById(1);

        verify(toTest).existsById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
        assertTrue(fromRepo);
    }

    @Test
    void existsByAuthority(){
        given(toTest.existsByAuthority(anyString())).willReturn(true);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        boolean fromRepo = toTest.existsByAuthority("USER");

        verify(toTest).existsByAuthority(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("USER", expectedParam);
        assertTrue(fromRepo);
    }

    @Test
    void findByIdEmpty(){
        given(toTest.findById(anyInt())).willReturn(Optional.empty());

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        Optional<Role> fromRepo = toTest.findById(1);

        verify(toTest).findById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void deleteById(){
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        toTest.deleteById(1);

        verify(toTest).deleteById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
    }

    @Test
    void findByAuthority(){
        given(toTest.findByAuthority(anyString())).willReturn(Optional.empty());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<Role> fromRepo = toTest.findByAuthority("USER");

        verify(toTest).findByAuthority(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("USER", expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void findByAuthorityEmpty(){
        given(toTest.findByAuthority(anyString())).willReturn(Optional.of(expectedRole));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<Role> fromRepo = toTest.findByAuthority("USER");

        verify(toTest).findByAuthority(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("USER", expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedRole, fromRepo.get());
    }

    @Test
    void findAll(){
        final List<Role> expectedRoles = Arrays.asList(expectedRole);

        given(toTest.findAll()).willReturn(expectedRoles);

        List<Role> fromRepo = toTest.findAll();

        verify(toTest).findAll();

        assertEquals(expectedRoles, fromRepo);
    }

}