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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleRepoTest {

    @Mock
    private RoleRepo toTest;

    private final UUID id = new UUID(36,36);

    private final Role expectedRole = new Role(this.id, "USER");

    @Test
    void save(){
        given(toTest.save(any(Role.class))).willReturn(expectedRole);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);

        Role fromRepo = toTest.save(expectedRole);

        verify(toTest).save(captor.capture());

        Role expectedParam = captor.getValue();
        assertEquals(id, expectedParam.getId());
        assertEquals("USER", expectedParam.getAuthority());

        assertEquals(expectedRole, fromRepo);
    }

    @Test
    void findById(){
        given(toTest.findById(any(UUID.class))).willReturn(Optional.of(expectedRole));

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Role> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedRole, fromRepo.get());
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
        given(toTest.findById(any(UUID.class))).willReturn(Optional.empty());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Role> fromRepo = toTest.findById(id);

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
    void findByAuthorityEmpty(){
        given(toTest.findByAuthority(anyString())).willReturn(Optional.empty());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Optional<Role> fromRepo = toTest.findByAuthority("USER");

        verify(toTest).findByAuthority(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("USER", expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void findByAuthority(){
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