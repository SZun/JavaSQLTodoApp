package com.sgz.TodoApp.repos;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRepoTest {

    @Mock
    private UserRepo toTest;

    private final UUID id = new UUID(36,36);

    private final Set<Role> testRoles = Sets.newHashSet(Sets.newHashSet(new Role(this.id, "USER")));

    private final User expectedUser = new User(this.id, "@amBam20", "Sam", this.testRoles);

    @Test
    void save(){
        given(toTest.save(any(User.class))).willReturn(expectedUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User fromRepo = toTest.save(expectedUser);

        verify(toTest).save(captor.capture());

        User expectedParam = captor.getValue();
        assertEquals(id, expectedParam.getId());
        assertEquals("@amBam20", expectedParam.getPassword());
        assertEquals("Sam", expectedParam.getUsername());
        assertEquals(testRoles, expectedParam.getRoles());

        assertEquals(expectedUser, fromRepo);
    }

    @Test
    void findByUsername(){
        given(toTest.findByUsername(anyString())).willReturn(Optional.of(expectedUser));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<User> fromRepo = toTest.findByUsername("Sam");

        verify(toTest).findByUsername(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("Sam", expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedUser, fromRepo.get());
    }

    @Test
    void findByUsernameEmpty(){
        given(toTest.findByUsername(anyString())).willReturn(Optional.empty());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<User> fromRepo = toTest.findByUsername("Sam");

        verify(toTest).findByUsername(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("Sam", expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void findById(){
        given(toTest.findById(any(UUID.class))).willReturn(Optional.of(expectedUser));

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<User> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedUser, fromRepo.get());
    }

    @Test
    void findByIdEmpty(){
        given(toTest.findById(any(UUID.class))).willReturn(Optional.empty());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<User> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void deleteById(){
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        toTest.deleteById(id);

        verify(toTest).deleteById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
    }

    @Test
    void findAll(){
        final List<User> expectedUsers = Arrays.asList(expectedUser);

        given(toTest.findAll()).willReturn(expectedUsers);

        List<User> fromRepo = toTest.findAll();

        verify(toTest).findAll();

        assertEquals(expectedUsers, fromRepo);
    }

    @Test
    void existsByUsername(){
        given(toTest.existsByUsername(anyString())).willReturn(true);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        boolean fromRepo = toTest.existsByUsername("Sam");

        verify(toTest).existsByUsername(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("Sam", expectedParam);
        assertTrue(fromRepo);
    }

    @Test
    void existsById(){
        given(toTest.existsById(any(UUID.class))).willReturn(true);

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        boolean fromRepo = toTest.existsById(id);

        verify(toTest).existsById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo);
    }

}