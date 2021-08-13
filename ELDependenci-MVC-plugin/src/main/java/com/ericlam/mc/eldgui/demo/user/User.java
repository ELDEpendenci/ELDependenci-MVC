package com.ericlam.mc.eldgui.demo.user;

// test entity
public class User {

    public String username;

    public String firstName;
    public String lastName;
    public int age;

    public User(String username, String firstName, String lastName, int age) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
