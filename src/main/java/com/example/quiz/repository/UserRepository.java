package com.example.quiz.repository;

import com.example.quiz.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository {

    private final static Map<Integer, User> userMap = new HashMap<>();
    private static final AtomicInteger idTmp = new AtomicInteger(0);

    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    public void add(User user) {
        user.setId(generateId());
        userMap.put(user.getId(), user);
    }

    public void clear() {
        userMap.clear();
    }

    private int generateId() {
        return idTmp.incrementAndGet();
    }
}
