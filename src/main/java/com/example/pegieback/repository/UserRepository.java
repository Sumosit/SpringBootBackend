package com.example.pegieback.repository;

import com.example.pegieback.models.Role;
import com.example.pegieback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long user_id);
    List<User> findUsersByRole(Role role);
}
