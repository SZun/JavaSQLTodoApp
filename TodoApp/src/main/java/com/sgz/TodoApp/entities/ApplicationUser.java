package com.sgz.TodoApp.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class ApplicationUser implements UserDetails {

    private final Set<? extends GrantedAuthority> authorities;
    private final String password;
    private final String username;
    private final boolean accountExpired;
    private final boolean accountLocked;
    private final boolean credentialsExpired;
    private final boolean enabled;

    @Autowired
    public ApplicationUser(String username, String password,  Set<? extends GrantedAuthority> authorities, boolean accountExpired, boolean accountLocked, boolean credentialsExpired, boolean enabled) {
        this.authorities = authorities;
        this.password = password;
        this.username = username;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

