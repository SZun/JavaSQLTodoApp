package com.sgz.TodoApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@JsonIgnoreProperties("roles")
@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id = UUID.randomUUID();

    @NotBlank(message = "Password can not be blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must follow rules")
    @Column(nullable = false)
    @NonNull
    private String password;

    @NotBlank(message = "Username can not be blank")
    @Size(max = 50, message = "Username can not be more than 50 characters")
    @Column(nullable = false)
    @NonNull
    private String username;


    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    public User(UUID id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }

    public User(String password, String username, Set<Role> roles) {
        this.password = password;
        this.username = username;
        this.roles = roles;
    }
}

