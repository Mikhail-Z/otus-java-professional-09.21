package com.company.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
