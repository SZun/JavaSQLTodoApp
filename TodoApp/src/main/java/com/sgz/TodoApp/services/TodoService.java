/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author samg.zun
 */
@Service
public class TodoService {

    private final TodoRepo todoRepo;

    @Autowired
    public TodoService(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    public List<Todo> getAllTodos(int id) throws NoItemsException {
        List<Todo> allTodos = todoRepo.findAllByUser_Id(id);

        if (allTodos.isEmpty()) throw new NoItemsException("No Items");

        return allTodos;
    }

    public Todo getTodoById(int id, int userId) throws InvalidIdException {
        Optional<Todo> toGet = todoRepo.findByIdAndUser_Id(id, userId);

        if (!toGet.isPresent()) throw new InvalidIdException("Invalid Id");

        return toGet.get();
    }

    public Todo createTodo(Todo toAdd, int userId) throws InvalidEntityException {
        validateTodo(toAdd);
        toAdd.getUser().setId(userId);
        return todoRepo.save(toAdd);
    }

    public Todo editTodo(Todo toEdit, int userId) throws InvalidEntityException, InvalidIdException {
        validateTodo(toEdit);
        checkExists(toEdit.getId(), userId);

        toEdit.getUser().setId(userId);
        return todoRepo.save(toEdit);
    }

    public void deleteTodoById(int id, int userId) throws InvalidIdException {
        checkExists(id, userId);
        todoRepo.deleteById(id);
    }

    private void checkExists(int id, int userId) throws InvalidIdException {
        if (!todoRepo.existsByIdAndUser_Id(id, userId)) throw new InvalidIdException("Invalid Id");
    }

    private void validateTodo(Todo toUpsert) throws InvalidEntityException {
        if (toUpsert == null
                || toUpsert.getName().trim().isEmpty()
                || toUpsert.getName().trim().length() > 50
                || (toUpsert.getDescription() != null
                && toUpsert.getDescription().length() > 255)
                || (toUpsert.getEndDate() != null
                && toUpsert.getEndDate().isBefore(toUpsert.getStartDate())
        )
                || (toUpsert.getEndDate() != null
                && toUpsert.getEndDate().isAfter(LocalDate.now())
        )
        ) {
            throw new InvalidEntityException("Invalid entity");
        }
    }

}
