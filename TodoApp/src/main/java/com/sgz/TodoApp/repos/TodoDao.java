/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.repos;

import com.sgz.TodoApp.entities.Todo;
import java.util.List;

/**
 *
 * @author samg.zun
 */
public interface TodoDao {
    
    Todo getOne(int id);
    
    List<Todo> findAll();
    
    void deleteById(int id);
    
    Todo save(Todo toSave);
    
}
