package com.example.quiz.repository;

import com.example.quiz.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository {

    private final static Map<Long, User> userMap = new HashMap<>();
    private static final AtomicLong idTmp = new AtomicLong(0);

    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    public User getById(long id) {
        return userMap.get(id);
    }

    public long add(User user) {
        long id = generateId();
        user.setId(id);
        userMap.put(user.getId(), user);
        return id;
    }

    public void clear() {
        userMap.clear();
    }

    private long generateId() {
        return idTmp.incrementAndGet();
    }
}
