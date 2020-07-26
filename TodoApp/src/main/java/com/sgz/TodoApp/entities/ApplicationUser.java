package com.sgz.TodoApp.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity(name="Users")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class ApplicationUser {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Users_Roles",
            joinColumns = {@JoinColumn(name = "User_Id")},
            inverseJoinColumns = {@JoinColumn(name = "Role_Id")})
    private Set<ApplicationRole> authorities;

    @NotBlank(message = "Password can not be blank")
    @Size(max = 20, message = "Password can not be more than 20 characters")
    @Column(nullable = false)
    @NonNull
    private String password;

    @NotBlank(message = "Username can not be blank")
    @Size(max = 50, message = "Username can not be more than 50 characters")
    @Column(nullable = false)
    @NonNull
    private String username;

}

