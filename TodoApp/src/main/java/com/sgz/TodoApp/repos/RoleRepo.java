package com.sgz.TodoApp.repos;

import com.sgz.TodoApp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    boolean existsByAuthority(String authority);

    Optional<Role> findByAuthority(String authority);
}
