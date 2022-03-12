package com.company.services;

import com.company.dao.repository.AuthUserRepository;

public class UserAuthServiceImpl implements UserAuthService {

    private final AuthUserRepository authUserRepository;

    public UserAuthServiceImpl(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return authUserRepository.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
