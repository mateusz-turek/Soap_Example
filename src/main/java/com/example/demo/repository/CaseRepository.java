package com.example.demo.repository;

import com.example.demo.model.Case;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CaseRepository extends CrudRepository<Case,Long> {
    List<Case> findAll();

    List<Case> findByStatus(String status);

    List<Case> findByAssignedTo(String assignedTo);
}
