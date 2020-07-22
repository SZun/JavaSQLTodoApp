/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.Todo;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import com.sgz.TodoApp.repos.TodoRepositoryDB;
import java.util.List;
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
        if(allTodos.isEmpty()){
            throw new NoItemsException("No Items");
        }
        return allTodos;
    }
    
//    public Todo getById(int id) throws InvalidIdException {
//        return dao.findById(id);
//    }
    
    public Todo upsertTodo(Todo toEdit){
        return dao.save(toEdit);
    }
    
    public void deleteTodo(int id){
        dao.deleteById(id);
    }
}
