package com.example.td5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.td5.entities.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

}
