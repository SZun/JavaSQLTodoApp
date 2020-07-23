/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.exceptionhandlers;

import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author samg.zun
 */
@ControllerAdvice
@RestController
public class TodoControllerExceptionHandler {

    @ExceptionHandler(InvalidIdException.class)
    public final ResponseEntity<CustomError> handleInvalidIdException(
            InvalidIdException ex,
            WebRequest req) {

        return new ResponseEntity<>(new CustomError(ex.getMessage(), "InvalidIdExceptiond"),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoItemsException.class)
    public final ResponseEntity<CustomError> handleNoItemsException(
            NoItemsException ex,
            WebRequest req) {

        return new ResponseEntity<>(new CustomError(ex.getMessage(), "NoItemsException"),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public final ResponseEntity<CustomError> handleInvalidEntityException(
            InvalidEntityException ex,
            WebRequest req) {

        final String CUSTOM_ERR_MESSAGE = "Fields entered are invalid";

        return new ResponseEntity<>(new CustomError(CUSTOM_ERR_MESSAGE, "InvalidEntityException"),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<CustomError> handleBindingErrors(
            MethodArgumentNotValidException ex,
            WebRequest req) {

        List<FieldErrorResponse> errs = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> {
            errs.add(new FieldErrorResponse(fe.getField(), fe.getDefaultMessage()));
        });

        CustomError err = new CustomError("Fields are invalid", "InvalidERequestBodyException", errs);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

}
