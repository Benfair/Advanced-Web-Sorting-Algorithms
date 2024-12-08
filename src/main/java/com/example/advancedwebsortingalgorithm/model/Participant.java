package com.example.advancedwebsortingalgorithm.model;

import jakarta.persistence.*;

@Entity
@Table(name="participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int score;

    public Participant() {

    }

    public Participant(String name, int score) {
        this.name = name;
        this.score = score;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
