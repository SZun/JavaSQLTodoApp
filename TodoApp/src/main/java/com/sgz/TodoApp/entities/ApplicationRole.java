package com.sgz.TodoApp.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity(name = "Roles")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class ApplicationRole {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @NotBlank(message = "Please enter a role")
    @Size(max = 50, message = "Role title cannot be more than 50 characters")
    @NonNull
    private String authority;

}
