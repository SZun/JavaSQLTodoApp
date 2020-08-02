/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author samg.zun
 */
@JsonIgnoreProperties("user")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "Todos")
@Data
public class Todo {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Name can not be blank")
    @Size(max = 50, message = "Name can not be more than 50 characters")
    @Column(nullable = false)
    @NonNull
    private String name;

    @Size(max = 255, message = "Description can not be more than 255 characters")
    @Column
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Start Date can not be null")
    @Column(nullable = false)
    @NonNull
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "End Date must be in past or today")
    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean finished;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user = new User();

    public Todo(String name, String description, LocalDate startDate, LocalDate endDate){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
