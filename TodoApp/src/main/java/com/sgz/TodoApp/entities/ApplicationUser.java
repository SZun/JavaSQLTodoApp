package com.sgz.TodoApp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity(name="Users")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ApplicationUser implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToMany
    private Set<? extends GrantedAuthority> authorities;

    @NotBlank(message = "Password can not be blank")
    @Size(max = 20, message = "Password can not be more than 20 characters")
    @Column(nullable = false)
    @NonNull
    private final String password;

    @NotBlank(message = "Username can not be blank")
    @Size(max = 50, message = "Username can not be more than 50 characters")
    @Column(nullable = false)
    @NonNull
    private final String username;

    @Column(nullable = false)
    private final boolean accountExpired;

    @Column(nullable = false)
    private final boolean accountLocked;

    @Column(nullable = false)
    private final boolean credentialsExpired;

    @Column(nullable = false)
    private final boolean enabled;

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

    public void setAuthorities(Set<? extends GrantedAuthority> authorities){
        this.authorities = authorities;
    }
}

