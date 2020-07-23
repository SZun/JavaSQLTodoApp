/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.exceptions;

/**
 *
 * @author samg.zun
 */
public class InvalidEntityException extends Exception {

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
