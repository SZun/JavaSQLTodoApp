/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.services;

import com.google.common.base.Strings;
import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.TodoRepo;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author samg.zun
 */
@Service
public class TodoService {

    private final TodoRepo todoRepo;
    private final UserRepo userRepo;

    @Autowired
    public TodoService(TodoRepo todoRepo, UserRepo userRepo) {
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;
    }

    public List<Todo> getAllTodos() throws NoItemsException {
        List<Todo> allTodos = todoRepo.findAllByUser_Id(getUserByName().getId());
        if (allTodos.isEmpty()) {
            throw new NoItemsException("No Items");
        }
        return allTodos;
    }

    public Todo getTodoById(int id) throws InvalidIdException {
        Optional<Todo> toGet = todoRepo.findByIdAndUser_Id(id, getUserByName().getId());
        if (!toGet.isPresent()) {
            throw new InvalidIdException("Invalid Id");
        }
        return toGet.get();
    }

    public Todo createTodo(Todo toAdd) throws InvalidEntityException {
        validateTodo(toAdd);
        toAdd.setUser(getUserByName());
        return todoRepo.save(toAdd);
    }

    public Todo editTodo(Todo toEdit) throws InvalidEntityException, InvalidIdException{
        validateTodo(toEdit);
        checkExists(toEdit.getId());
        toEdit.setUser(getUserByName());
        
        return todoRepo.save(toEdit);
    }
            
    public void deleteTodo(int id) throws InvalidIdException {
        checkExists(id);
        
        todoRepo.deleteById(id);
    }
    
    private void checkExists(int id) throws InvalidIdException {
        if(!todoRepo.existsByIdAndUser_Id(id, getUserByName().getId())){
            throw new InvalidIdException("Invalid Id");
        }
    }

    private void validateTodo(Todo toUpsert) throws InvalidEntityException {
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

    private User getUserByName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByUsername(username).get();
    }

}
