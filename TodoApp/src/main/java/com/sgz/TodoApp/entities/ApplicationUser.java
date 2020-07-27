package com.sgz.TodoApp.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    private Set<Role> authorities;

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

    public ApplicationUser(Set<Role> authorities, @NotBlank(message = "Password can not be blank") @NonNull String password, @NotBlank(message = "Username can not be blank") @Size(max = 50, message = "Username can not be more than 50 characters") @NonNull String username) {
        this.authorities = authorities;
        this.password = password;
        this.username = username;
    }


}

