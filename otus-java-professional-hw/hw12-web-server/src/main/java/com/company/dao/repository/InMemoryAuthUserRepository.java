package com.company.dao.repository;

import com.company.dao.model.AuthUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAuthUserRepository implements AuthUserRepository {
    private Map<String, AuthUser> map = new HashMap<>();

    public InMemoryAuthUserRepository() {
        map.put("admin", new AuthUser(1, "admin", "admin"));
        //map.put("admin2", new AuthUser(2, "admin2", "admin"));
    }

    @Override
    public Optional<AuthUser> findByLogin(String login) {
        return Optional.ofNullable(map.get(login));
    }
}
