/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp.repos;

import com.sgz.TodoApp.entities.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author samg.zun
 */
@Repository
public interface TodoRepo extends JpaRepository<Todo, UUID> {

    List<Todo> findAllByUser_Id(UUID id);

    Optional<Todo> findByIdAndUser_Id(UUID id, UUID userId);

    boolean existsByIdAndUser_Id(UUID id, UUID userId);

}
