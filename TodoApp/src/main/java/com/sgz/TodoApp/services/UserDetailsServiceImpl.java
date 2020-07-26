package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.ApplicationUser;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo repo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> op = repo.findByUsername(username);

        if (!op.isPresent()) {
            new UsernameNotFoundException(String.format("Username %s not found", username));
        }

        ApplicationUser toReturn = op.get();
        toReturn.setAuthorities(null);

        return toReturn;
    }
}
