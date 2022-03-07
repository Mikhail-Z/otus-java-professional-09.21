package com.company.dao.repository;

import com.company.dao.model.AuthUser;

import java.util.Optional;

public interface AuthUserRepository {
    public Optional<AuthUser> findByLogin(String login);
}
