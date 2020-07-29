package com.sgz.TodoApp.services;

import com.sgz.TodoApp.entities.Role;
import com.sgz.TodoApp.entities.User;
import com.sgz.TodoApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo repo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> op = repo.findByUsername(username);

        if (!op.isPresent()) {
            new UsernameNotFoundException(String.format("Username %s not found", username));
        }

        User u = op.get();

        Set<GrantedAuthority> userRoles = new HashSet<>();

        for(Role r : u.getRoles()){
            userRoles.add(new SimpleGrantedAuthority("ROLE_" + r.getAuthority()));
        }

        return new org.springframework.security.core.userdetails.User(username, u.getPassword(), userRoles);
    }
}
