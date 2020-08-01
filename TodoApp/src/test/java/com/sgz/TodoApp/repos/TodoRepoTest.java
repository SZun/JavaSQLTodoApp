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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoRepoTest {

    @Mock
    private TodoRepo toTest;

    private final User testUser = new User(1, "@amBam20", "Sam", Sets.newHashSet(new Role(1, "USER")));

    private final Todo expectedTodo = new Todo(1, "Walk Dog", "Finished walking baxter", LocalDate.now(), LocalDate.now(), true, this.testUser);


    @Test
    void save() {
        given(toTest.save(any(Todo.class))).willReturn(expectedTodo);

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);

        Todo fromRepo = toTest.save(expectedTodo);

        verify(toTest).save(captor.capture());

        Todo expectedParam = captor.getValue();
        assertEquals(1, expectedParam.getId());
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
        given(toTest.findByIdAndUser_Id(anyInt(), anyInt())).willReturn(Optional.of(expectedTodo));

        ArgumentCaptor<Integer> captor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> captor2 = ArgumentCaptor.forClass(Integer.class);

        Optional<Todo> fromRepo = toTest.findByIdAndUser_Id(1, 1);

        verify(toTest).findByIdAndUser_Id(captor1.capture(), captor2.capture());

        Integer expectedParam = captor1.getValue();
        assertEquals(1, expectedParam);

        expectedParam = captor2.getValue();
        assertEquals(1, expectedParam);

        assertTrue(fromRepo.isPresent());
        assertEquals(expectedTodo, fromRepo.get());

    }

    @Test
    void findByIdAndUser_IdEmpty() {
        given(toTest.findByIdAndUser_Id(anyInt(), anyInt())).willReturn(Optional.empty());

        ArgumentCaptor<Integer> captor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> captor2 = ArgumentCaptor.forClass(Integer.class);

        Optional<Todo> fromRepo = toTest.findByIdAndUser_Id(1, 1);

        verify(toTest).findByIdAndUser_Id(captor1.capture(), captor2.capture());

        Integer expectedParam = captor1.getValue();
        assertEquals(1, expectedParam);

        expectedParam = captor2.getValue();
        assertEquals(1, expectedParam);

        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void findAllByUser_Id() {
        final List<Todo> expectedTodos = Arrays.asList(expectedTodo);

        given(toTest.findAllByUser_Id(anyInt())).willReturn(expectedTodos);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        List<Todo> fromRepo = toTest.findAllByUser_Id(1);

        verify(toTest).findAllByUser_Id(captor.capture());

        Integer expectedParam = captor.getValue();
        assertEquals(1, expectedParam);

        assertEquals(expectedTodos, fromRepo);

    }

    @Test
    void existsByIdAndUser_Id() {
        given(toTest.existsByIdAndUser_Id(anyInt(),anyInt())).willReturn(true);

        ArgumentCaptor<Integer> captor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> captor2 = ArgumentCaptor.forClass(Integer.class);

        boolean fromRepo = toTest.existsByIdAndUser_Id(1,1);

        verify(toTest).existsByIdAndUser_Id(captor1.capture(), captor2.capture());

        Integer expectedParam = captor1.getValue();
        assertEquals(1, expectedParam);

        expectedParam = captor2.getValue();
        assertEquals(1, expectedParam);

        assertTrue(fromRepo);
    }
}