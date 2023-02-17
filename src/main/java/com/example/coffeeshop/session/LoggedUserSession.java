package com.example.coffeeshop.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class LoggedUserSession {
    private long id;
    private String username;
    private String email;

    public LoggedUserSession() {
    }

    public boolean isLoggeinIn() {
        return this.id != 0 && this.username != null;
    }

    public long getId() {
        return id;
    }

    public LoggedUserSession setId(long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public LoggedUserSession setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public LoggedUserSession setEmail(String email) {
        this.email = email;
        return this;
    }
}
