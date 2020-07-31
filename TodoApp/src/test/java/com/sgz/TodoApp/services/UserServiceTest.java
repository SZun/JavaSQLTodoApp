package com.sgz.TodoApp.services;

import com.sgz.TodoApp.exceptions.InvalidEntityException;
import com.sgz.TodoApp.exceptions.InvalidIdException;
import com.sgz.TodoApp.exceptions.InvalidNameException;
import com.sgz.TodoApp.exceptions.NoItemsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Test
    void createUser() throws InvalidEntityException, InvalidNameException {
    }

    @Test
    void getAllUsers() throws NoItemsException {
    }

    @Test
    void getUserByName() throws InvalidEntityException, InvalidNameException {
    }

    @Test
    void editUser() throws InvalidEntityException, InvalidIdException, InvalidNameException, AccessDeniedException {
    }

    @Test
    void getUserById() throws InvalidIdException {
    }

    @Test
    void deleteUserById() throws InvalidIdException, AccessDeniedException {
    }
}