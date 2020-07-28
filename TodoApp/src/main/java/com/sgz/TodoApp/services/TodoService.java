/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.services;

import com.google.common.base.Strings;
import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author samg.zun
 */
@Service
public class TodoService {

    private final TodoRepo repo;

    @Autowired
    public TodoService(TodoRepo repo) {
        this.repo = repo;
    }

    public List<Todo> getAll() throws NoItemsException {
        List<Todo> allTodos = repo.findAll();
        if (allTodos.isEmpty()) {
            throw new NoItemsException("No Items");
        }
        return allTodos;
    }

//    public Todo getById(int id) throws InvalidIdException {
//        Optional<Todo> toGet = repo.findByIdAndUser_Username(id, getUsername());
//        if (!toGet.isPresent()) {
//            throw new InvalidIdException("Invalid Id");
//        }
//        return toGet.get();
//    }

    public Todo createTodo(Todo toAdd) throws InvalidEntityException {
        validate(toAdd);
        return repo.save(toAdd);
    }

//    public Todo editTodo(Todo toEdit) throws InvalidEntityException, InvalidIdException{
//        validate(toEdit);
//        checkExistsByIdAndUsername(toEdit.getId(), getUsername());
//
//        return repo.save(toEdit);
//    }
//
//    public void deleteTodo(int id) throws InvalidIdException {
//        checkExistsByIdAndUsername(id, getUsername());
//
//        repo.deleteById(id);
//    }

//    private String getUsername(){
//        return SecurityContextHolder.getContext().getAuthentication().getName();
//    }
    
//    private void checkExistsByIdAndUsername(int id, String username) throws InvalidIdException {
//        if(!repo.existsByIdAndUser_Username(id, username)){
//            throw new InvalidIdException("Invalid Id");
//        }
//    }

    private void validate(Todo toUpsert) throws InvalidEntityException {
        if(toUpsert == null 
            || Strings.isNullOrEmpty(toUpsert.getName().trim())
            || toUpsert.getName().trim().length() > 50
            || (toUpsert.getDescription() != null
                && toUpsert.getDescription().length() > 255)
            || toUpsert.getStartDate().isBefore(LocalDate.now())
            || (toUpsert.getEndDate() != null 
                && toUpsert.getEndDate().isBefore(toUpsert.getStartDate())
                )
            || (toUpsert.getEndDate() != null 
                && toUpsert.getEndDate().isAfter(LocalDate.now())
                )
                ){
            throw new InvalidEntityException("Invalid entity");
        }
    }
}
