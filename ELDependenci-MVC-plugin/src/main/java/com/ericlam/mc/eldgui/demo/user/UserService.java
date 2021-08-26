package com.ericlam.mc.eldgui.demo.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// fake service
public class UserService {

    private final Map<String, User> fakeUserTables = new ConcurrentHashMap<>();

    public UserService() {
        this.reset();
    }

    public void reset() {
        this.fakeUserTables.clear();
        this.fakeUserTables.put("user1", new User("user1", "Lam", "TF", 11));
        this.fakeUserTables.put("user2", new User("user2", "Chan", "ZF", 21));
        this.fakeUserTables.put("user3", new User("user3", "Wong", "ZZ", 55));
        this.fakeUserTables.put("user4", new User("user4", "Siu", "OO", 32));
        this.fakeUserTables.put("user5", new User("user5", "Lai", "LL", 19));
    }

    public List<User> findAll() {
        return new ArrayList<>(fakeUserTables.values());
    }

    public List<String> findAllUsernames() {
        return new ArrayList<>(fakeUserTables.keySet());
    }


    public void save(User user) {
        this.fakeUserTables.put(user.username, user);
    }


    public void removeUser(String username) {
        this.fakeUserTables.remove(username);
    }

    public Optional<User> findById(String username) {
        return Optional.ofNullable(this.fakeUserTables.get(username));
    }
}
