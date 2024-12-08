package com.example.advancedwebsortingalgorithm.algorithm;

import com.example.advancedwebsortingalgorithm.model.Participant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class SortingAlgorithm {

    public void heapSort(List<Participant> participants) {
        int n = participants.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(participants, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            Collections.swap(participants, 0, i);
            heapify(participants, i, 0);
        }
    }

    private void heapify(List<Participant> participants, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && participants.get(left).getScore() > participants.get(largest).getScore()) {
            largest = left;
        }
        if (right < n && participants.get(right).getScore() > participants.get(largest).getScore()) {
            largest = right;
        }

        if (largest != i) {
            Collections.swap(participants, i, largest);
            heapify(participants, n, largest);
        }
    }

    public void quickSort(List<Participant> participants, int low, int high) {
        if (low < high) {
            int pi = partition(participants, low, high);

            quickSort(participants, low, pi - 1);
            quickSort(participants, pi + 1, high);
        }
    }

    private int partition(List<Participant> participants, int low, int high) {
        Participant pivot = participants.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (participants.get(j).getScore() >= pivot.getScore()) {
                i++;
                Collections.swap(participants, i, j);
            }
        }

        Collections.swap(participants, i + 1, high);
        return i + 1;
    }

    public List<Participant> mergeSort(List<Participant> participants) {
        if (participants.size() <= 1) {
            return participants;
        }

        int mid = participants.size() / 2;

        List<Participant> left = mergeSort(participants.subList(0, mid));
        List<Participant> right = mergeSort(participants.subList(mid, participants.size()));

        return merge(left, right);
    }

    private List<Participant> merge(List<Participant> left, List<Participant> right) {
        List<Participant> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getScore() >= right.get(j).getScore()) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i++));
        }

        while (j < right.size()) {
            merged.add(right.get(j++));
        }

        return merged;
    }

    public void radixSort(List<Participant> participants) {
        int maxScore = participants.stream().mapToInt(Participant::getScore).max().orElse(0);
        int exp = 1;

        while (maxScore / exp > 0) {
            countingSort(participants, exp);
            exp *= 10;
        }
    }

    private void countingSort(List<Participant> participants, int exp) {
        List<Participant> output = new ArrayList<>(participants);
        int[] count = new int[10];

        for (Participant participant : participants) {
            int digit = (participant.getScore() / exp) % 10;
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = participants.size() - 1; i >= 0; i--) {
            int digit = (participants.get(i).getScore() / exp) % 10;
            output.set(count[digit] - 1, participants.get(i));
            count[digit]--;
        }

        for (int i = 0; i < participants.size(); i++) {
            participants.set(i, output.get(i));
        }
    }

    public void bucketSort(List<Participant> participants) {
        int maxScore = participants.stream().mapToInt(Participant::getScore).max().orElse(0);
        int bucketCount = 10;
        List<List<Participant>> buckets = new ArrayList<>(bucketCount);

        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        for (Participant participant : participants) {
            int index = (participant.getScore() * bucketCount) / (maxScore + 1);
            buckets.get(index).add(participant);
        }

        participants.clear();

        for (List<Participant> bucket : buckets) {
            bucket.sort(Comparator.comparingInt(Participant::getScore).reversed());
            participants.addAll(bucket);
        }
    }
}

