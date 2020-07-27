package com.sgz.TodoApp.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity(name = "Roles")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Role {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @NotBlank(message = "Please enter a role")
    @Size(max = 50, message = "Role title cannot be more than 50 characters")
    @NonNull
    @Column(nullable = false)
    private String authority;

}
