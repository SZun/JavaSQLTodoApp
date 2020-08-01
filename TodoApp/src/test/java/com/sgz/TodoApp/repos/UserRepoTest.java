package com.sgz.TodoApp.repos;

import com.google.common.collect.Sets;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRepoTest {

    @Mock
    private UserRepo toTest;

    private final Set<Role> testRoles = Sets.newHashSet(Sets.newHashSet(new Role(1, "USER")));

    private final User expectedUser = new User(1, "@amBam20", "Sam", this.testRoles);

    @Test
    void save(){
        given(toTest.save(any(User.class))).willReturn(expectedUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User fromRepo = toTest.save(expectedUser);

        verify(toTest).save(captor.capture());

        User expectedParam = captor.getValue();
        assertEquals(1, expectedParam.getId());
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
        given(toTest.findById(anyInt())).willReturn(Optional.of(expectedUser));

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        Optional<User> fromRepo = toTest.findById(1);

        verify(toTest).findById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedUser, fromRepo.get());
    }

    @Test
    void findByIdEmpty(){
        given(toTest.findById(anyInt())).willReturn(Optional.empty());

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        Optional<User> fromRepo = toTest.findById(1);

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
        given(toTest.existsById(anyInt())).willReturn(true);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        boolean fromRepo = toTest.existsById(1);

        verify(toTest).existsById(captor.capture());

        Integer expectedParam = captor.getValue();

        assertEquals(1, expectedParam);
        assertTrue(fromRepo);
    }

}