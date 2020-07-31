/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

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

}
