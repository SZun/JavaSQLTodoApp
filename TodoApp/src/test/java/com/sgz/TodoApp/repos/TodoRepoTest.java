package com.sgz.TodoApp.repos;

import com.google.common.collect.Sets;
import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoRepoTest {

    @Mock
    private TodoRepo toTest;

    private final UUID id = new UUID(36,36);

    private final User testUser = new User(this.id, "@amBam20", "Sam", Sets.newHashSet(new Role(this.id, "USER")));

    private final Todo expectedTodo = new Todo(this.id, "Walk Dog", "Finished walking baxter", LocalDate.now(), LocalDate.now(), true, this.testUser);


    @Test
    void save() {
        given(toTest.save(any(Todo.class))).willReturn(expectedTodo);

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);

        Todo fromRepo = toTest.save(expectedTodo);

        verify(toTest).save(captor.capture());

        Todo expectedParam = captor.getValue();
        assertEquals(id, expectedParam.getId());
        assertEquals("Walk Dog", expectedParam.getName());
        assertEquals("Finished walking baxter", expectedParam.getDescription());
        assertEquals(LocalDate.now(), expectedParam.getStartDate());
        assertEquals(LocalDate.now(), expectedParam.getEndDate());
        assertTrue(expectedParam.isFinished());
        assertEquals(testUser, expectedParam.getUser());

        assertEquals(expectedTodo, fromRepo);
    }

    @Test
    void findByIdAndUser_Id() {
        given(toTest.findByIdAndUser_Id(any(UUID.class), any(UUID.class))).willReturn(Optional.of(expectedTodo));

        ArgumentCaptor<UUID> captor1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> captor2 = ArgumentCaptor.forClass(UUID.class);

        Optional<Todo> fromRepo = toTest.findByIdAndUser_Id(id, id);

        verify(toTest).findByIdAndUser_Id(captor1.capture(), captor2.capture());

        UUID expectedParam = captor1.getValue();
        assertEquals(id, expectedParam);

        expectedParam = captor2.getValue();
        assertEquals(id, expectedParam);

        assertTrue(fromRepo.isPresent());
        assertEquals(expectedTodo, fromRepo.get());

    }

    @Test
    void findByIdAndUser_IdEmpty() {
        given(toTest.findByIdAndUser_Id(any(UUID.class), any(UUID.class))).willReturn(Optional.empty());

        ArgumentCaptor<UUID> captor1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> captor2 = ArgumentCaptor.forClass(UUID.class);

        Optional<Todo> fromRepo = toTest.findByIdAndUser_Id(id, id);

        verify(toTest).findByIdAndUser_Id(captor1.capture(), captor2.capture());

        UUID expectedParam = captor1.getValue();
        assertEquals(id, expectedParam);

        expectedParam = captor2.getValue();
        assertEquals(id, expectedParam);

        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void findAllByUser_Id() {
        final List<Todo> expectedTodos = Arrays.asList(expectedTodo);

        given(toTest.findAllByUser_Id(any(UUID.class))).willReturn(expectedTodos);

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        List<Todo> fromRepo = toTest.findAllByUser_Id(id);

        verify(toTest).findAllByUser_Id(captor.capture());

        UUID expectedParam = captor.getValue();
        assertEquals(id, expectedParam);

        assertEquals(expectedTodos, fromRepo);

    }

    @Test
    void existsByIdAndUser_Id() {
        given(toTest.existsByIdAndUser_Id(any(UUID.class),any(UUID.class))).willReturn(true);

        ArgumentCaptor<UUID> captor1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> captor2 = ArgumentCaptor.forClass(UUID.class);

        boolean fromRepo = toTest.existsByIdAndUser_Id(id,id);

        verify(toTest).existsByIdAndUser_Id(captor1.capture(), captor2.capture());

        UUID expectedParam = captor1.getValue();
        assertEquals(id, expectedParam);

        expectedParam = captor2.getValue();
        assertEquals(id, expectedParam);

        assertTrue(fromRepo);
    }
}