/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author samg.zun
 */
@AllArgsConstructor
public class CustomError {
    
    @Getter private String message;
    @Getter private String name;

    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }

}
