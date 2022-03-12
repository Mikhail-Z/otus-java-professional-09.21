package com.company.dao.model;

public class AuthUser {
    private final long id;
    private final String login;
    private final String password;

    public AuthUser(long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
