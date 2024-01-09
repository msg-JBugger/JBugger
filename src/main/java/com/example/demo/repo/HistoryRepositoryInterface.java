package com.example.demo.repo;

import com.example.demo.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepositoryInterface extends JpaRepository<History, Long> {
}
