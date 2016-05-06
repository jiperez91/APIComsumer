package com.example.jiperez.apiconsumer;

public class Owner {
    private String name;
    private String lastname;
    private String dni;
    private String nationality;

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}