package com.example.td5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.td5.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
