package com.example.quiz.repository;

import com.example.quiz.domain.Education;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class EducationRepository {

    private final static List<Education> list = new ArrayList<>();
    private final static AtomicLong idTmp = new AtomicLong(0);

    public void clear() {
        list.clear();
    }

    public List<Education> getAllByUserId(long userId) {
        return list.stream()
                .filter(edu -> edu.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public long add(Education education) {
        long id = generateId();
        education.setId(id);
        list.add(education);
        return id;
    }

    private long generateId() {
        return idTmp.incrementAndGet();
    }
}
