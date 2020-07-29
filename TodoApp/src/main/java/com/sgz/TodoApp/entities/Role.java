package com.sgz.TodoApp.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Data
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Please enter a role")
    @Size(max = 50, message = "Role title cannot be more than 50 characters")
    @NonNull
    @Column(nullable = false)
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

}
