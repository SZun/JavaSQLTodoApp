/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.exceptionhandlers;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author samg.zun
 */
@AllArgsConstructor
public class FieldErrorResponse {
    
    @Getter
    private String fieldName;
    
    @Getter
    private String message;

}
