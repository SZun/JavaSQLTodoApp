/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 *
 * @author samg.zun
 */
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity(name = "Todos")
@Data
public class Todo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

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
    @FutureOrPresent(message = "Start Date must be in future or today")
    @Column(nullable = false)
    @NonNull
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "End Date must be in past or today")
    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean finished;

//    @ManyToOne
//    @JoinColumn(name = "Username")
//    ApplicationUser user;
//
//    public Todo(int id, @NotBlank(message = "Name can not be blank") @Size(max = 50, message = "Name can not be more than 50 characters") @NonNull String name, @Size(max = 255, message = "Description can not be more than 255 characters") String description, @NotNull(message = "Start Date can not be null") @FutureOrPresent(message = "Start Date must be in future or today") @NonNull LocalDate startDate, @PastOrPresent(message = "End Date must be in past or today") LocalDate endDate, boolean finished) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.finished = finished;
//    }
}
