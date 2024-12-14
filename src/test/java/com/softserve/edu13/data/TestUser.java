package com.softserve.edu13.data;

import java.util.Objects;

public class TestUser {
    private String email;
    private String password;

    public TestUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestUser testUser = (TestUser) o;
        return email.equals(testUser.email) && password.equals(testUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}