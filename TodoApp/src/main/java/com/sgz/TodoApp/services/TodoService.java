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
import com.sgz.TodoApp.repos.TodoRepositoryDB;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author samg.zun
 */
@Service
public class TodoService {

    @Autowired
    TodoRepositoryDB dao;

    public List<Todo> getAll() throws NoItemsException {
        List<Todo> allTodos = dao.findAll();
        if (allTodos.isEmpty()) {
            throw new NoItemsException("No Items");
        }
        return allTodos;
    }

    public Todo getById(int id) throws InvalidIdException {
        Optional<Todo> toGet = dao.findById(id);
        if (!toGet.isPresent()) {
            throw new InvalidIdException("Invalid Id");
        }
        return toGet.get();
    }

    public Todo createTodo(Todo toAdd) throws InvalidEntityException {
        validate(toAdd);
        return dao.save(toAdd);
    }

    public Todo editTodo(Todo toEdit) throws InvalidEntityException, InvalidIdException{
        validate(toEdit);
        getById(toEdit.getId());
        
        return dao.save(toEdit);
    }
            
    public void deleteTodo(int id) throws InvalidIdException {
        getById(id);
        
        dao.deleteById(id);
    }

    private void validate(Todo toUpsert) throws InvalidEntityException {
        if(toUpsert == null 
            || toUpsert.getName() == null 
            || toUpsert.getName().trim().length() == 0
            || toUpsert.getName().trim().length() > 50
            || (toUpsert.getDescription() != null
                && toUpsert.getDescription().length() > 255)
            || toUpsert.getStartDate() == null
            || (toUpsert.getEndDate() != null 
                && toUpsert.getEndDate().isBefore(toUpsert.getEndDate()))){
            throw new InvalidEntityException("Invalid entity");
        }
    }
}
