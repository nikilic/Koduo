package com.prvaci.koduo;

import java.util.Date;

public class User {
    String username;
    String email;
    Date sessionExpiryDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String fullName) {
        this.email = fullName;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
