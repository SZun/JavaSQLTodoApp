package com.sgz.TodoApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties("users")
@Data
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Role {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Please enter a role")
    @Size(max = 50, message = "Role title cannot be more than 50 characters")
    @NonNull
    @Column(nullable = false)
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public Role(UUID id, String authority) {
        this.id = id;
        this.authority = authority;
    }
}
