package ru.kata.spring.boot_security.demo.model;

public enum Roles {
    USER ("USER"),
    ADMIN ("ADMIN");

    private final String name;

    Roles(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
