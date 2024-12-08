package com.example.advancedwebsortingalgorithm.service;

import com.example.advancedwebsortingalgorithm.algorithm.SortingAlgorithm;
import com.example.advancedwebsortingalgorithm.model.Participant;
import com.example.advancedwebsortingalgorithm.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final SortingAlgorithm sortingAlgorithm;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository, SortingAlgorithm sortingAlgorithm) {
        this.participantRepository = participantRepository;
        this.sortingAlgorithm = sortingAlgorithm;
    }

    public List<Participant> getSortedParticipants(String algorithm) {
        List<Participant> participants = participantRepository.findAll();

        switch (algorithm.toLowerCase()) {
            case "heap":
                sortingAlgorithm.heapSort(participants);
                break;
            case "quick":
                sortingAlgorithm.quickSort(participants, 0, participants.size() - 1);
                break;
            case "merge":
                participants = sortingAlgorithm.mergeSort(participants);
                break;
            case "radix":
                sortingAlgorithm.radixSort(participants);
                break;
            case "bucket":
                sortingAlgorithm.bucketSort(participants);
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting algorithm: " + algorithm);
        }

        return participants;
    }

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public List<Participant> searchByName(String name) {
        return participantRepository.findByNameContainingIgnoreCase(name);
    }

    public Participant saveParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public Participant updateScore(Long id, int newScore) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Participant not found with id: " + id));
        participant.setScore(newScore);
        return participantRepository.save(participant);
    }


    public void deleteParticipant(Long id) {
        if (!participantRepository.existsById(id)) {
            throw new IllegalArgumentException("Participant with ID " + id + " does not exist.");
        }
        participantRepository.deleteById(id);
    }

}



