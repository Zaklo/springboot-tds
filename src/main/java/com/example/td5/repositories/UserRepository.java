package com.example.td5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.td5.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
