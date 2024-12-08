package com.example.advancedwebsortingalgorithm.controller;

import com.example.advancedwebsortingalgorithm.model.Participant;
import com.example.advancedwebsortingalgorithm.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Participant>>> getAllParticipants() {
        List<EntityModel<Participant>> participants = participantService.getAllParticipants().stream()
                .map(participant -> {
                    EntityModel<Participant> participantModel = EntityModel.of(participant,
                            linkTo(methodOn(ParticipantController.class).getAllParticipants()).withRel("all-participants")
                    );
                    return participantModel;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(participants);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EntityModel<Participant>>> searchParticipants(@RequestParam String name) {
        List<EntityModel<Participant>> participants = participantService.searchByName(name).stream()
                .map(participant -> {
                    EntityModel<Participant> participantModel = EntityModel.of(participant,
                            linkTo(methodOn(ParticipantController.class).getAllParticipants()).withRel("all-participants"),
                            linkTo(methodOn(ParticipantController.class).searchParticipants(name)).withRel("search-results")
                    );
                    return participantModel;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(participants);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<EntityModel<Participant>>> getSortedParticipants(@RequestParam String algorithm) {
        List<EntityModel<Participant>> sortedParticipants = participantService.getSortedParticipants(algorithm).stream()
                .map(participant -> {
                    EntityModel<Participant> participantModel = EntityModel.of(participant,
                            linkTo(methodOn(ParticipantController.class).getAllParticipants()).withRel("all-participants"),
                            linkTo(methodOn(ParticipantController.class).getSortedParticipants(algorithm)).withRel("sorted-participants")
                    );
                    return participantModel;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(sortedParticipants);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Participant>> saveParticipant(@RequestBody Participant participant) {
        Participant savedParticipant = participantService.saveParticipant(participant);

        EntityModel<Participant> participantModel = EntityModel.of(savedParticipant,
                linkTo(methodOn(ParticipantController.class).getAllParticipants()).withRel("all"));

        return ResponseEntity.ok(participantModel);
    }

    @PatchMapping("/{id}/score")
    public ResponseEntity<?> updateParticipantScore(
            @PathVariable Long id, @RequestParam int score) {

        try {
            Participant updatedParticipant = participantService.updateScore(id, score);
            EntityModel<Participant> resource = EntityModel.of(
                    updatedParticipant,
                    linkTo(methodOn(ParticipantController.class).getAllParticipants()).withRel("allParticipants")
            );
            return ResponseEntity.ok(resource);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<Map<String, String>>> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);

        Map<String, String> responseMessage = Map.of("message", "Participant deleted successfully.");

        EntityModel<Map<String, String>> responseModel = EntityModel.of(
                responseMessage,
                linkTo(methodOn(ParticipantController.class).getAllParticipants()).withRel("all")
        );

        return ResponseEntity.ok(responseModel);
    }

}