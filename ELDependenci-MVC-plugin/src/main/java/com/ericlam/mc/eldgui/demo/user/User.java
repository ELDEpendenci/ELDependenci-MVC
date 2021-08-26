package com.ericlam.mc.eldgui.demo.user;

// test entity
public class User {

    // id
    public String username;

    public String firstName;
    public String lastName;
    public int age;
    public Address address = new Address("line1", "line2");

    public User(String username, String firstName, String lastName, int age) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public User(String username, String firstName, String lastName, int age, Address address) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
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
                ", address=" + address +
                '}';
    }

    public static class Address {

        public String line1;
        public String line2;

        public Address(String line1, String line2) {
            this.line1 = line1;
            this.line2 = line2;
        }

        public Address() {
        }

        @Override
        public String toString() {
            return "Address{" +
                    "line1='" + line1 + '\'' +
                    ", line2='" + line2 + '\'' +
                    '}';
        }
    }
}
