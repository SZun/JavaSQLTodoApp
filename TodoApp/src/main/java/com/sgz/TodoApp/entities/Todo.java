/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.entities;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author samg.zun
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "stores")
@Data public class Todo {
    
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;
    
    @NotBlank(message = "Name can not be blank")
    @Size(max = 50, message = "Name can not be more than 50 characters")
    @Column(nullable = false)
    private String name;
    
    @Size(max = 255, message = "Description can not be more than 255 characters")
    @Column
    private String description;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Past(message = "Start Date must be in past")
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Past(message = "End Date must be in past")
    @Column
    private LocalDate endDate;
    
    @Column(nullable = false)
    private boolean finished;
}
