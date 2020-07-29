package com.sgz.TodoApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
//                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "roles")
    @JsonIgnoreProperties
    private List<User> users = new ArrayList<>();

}
