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
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sgz.TodoApp.repos.TodoRepo;
import java.time.LocalDate;

/**
 *
 * @author samg.zun
 */
@Service
public class TodoService {

    @Autowired
    TodoRepo repo;

    public List<Todo> getAll() throws NoItemsException {
        List<Todo> allTodos = repo.findAll();
        if (allTodos.isEmpty()) {
            throw new NoItemsException("No Items");
        }
        return allTodos;
    }

    public Todo getById(int id) throws InvalidIdException {
        Optional<Todo> toGet = repo.findById(id);
        if (!toGet.isPresent()) {
            throw new InvalidIdException("Invalid Id");
        }
        return toGet.get();
    }

    public Todo createTodo(Todo toAdd) throws InvalidEntityException {
        validate(toAdd);
        return repo.save(toAdd);
    }

    public Todo editTodo(Todo toEdit) throws InvalidEntityException, InvalidIdException{
        validate(toEdit);
        checkExists(toEdit.getId());
        
        return repo.save(toEdit);
    }
            
    public void deleteTodo(int id) throws InvalidIdException {
        checkExists(id);
        
        repo.deleteById(id);
    }
    
    private void checkExists(int id) throws InvalidIdException {
        if(!repo.existsById(id)){
            throw new InvalidIdException("Invalid Id");
        }
    }

    private void validate(Todo toUpsert) throws InvalidEntityException {
        if(toUpsert == null 
            || toUpsert.getName() == null 
            || toUpsert.getName().trim().length() == 0
            || toUpsert.getName().trim().length() > 50
            || (toUpsert.getDescription() != null
                && toUpsert.getDescription().length() > 255)
            || toUpsert.getStartDate() == null
            || toUpsert.getStartDate().isBefore(LocalDate.now())
            || (toUpsert.getEndDate() != null 
                && toUpsert.getEndDate().isBefore(toUpsert.getEndDate())
                )
            || (toUpsert.getEndDate() != null 
                && toUpsert.getEndDate().isAfter(LocalDate.now())
                )
                ){
            throw new InvalidEntityException("Invalid entity");
        }
    }
}
