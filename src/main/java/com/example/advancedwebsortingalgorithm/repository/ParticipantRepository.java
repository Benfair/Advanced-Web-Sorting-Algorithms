package com.example.advancedwebsortingalgorithm.repository;

import com.example.advancedwebsortingalgorithm.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByNameContainingIgnoreCase(String name);

}
