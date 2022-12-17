package com.example.cj;

public class Ob_User {
    
    String name;
    String number;

    public Ob_User() {
    }

    public Ob_User(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
}
