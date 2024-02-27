package org.example.entities;

public class User {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    public User() {}
    public User(String name, String surname, String username, String password, int user_id) {
        setId(user_id);
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }
    public void setPassword(String password) { this.password = password;}
    public String getPassword() {return this.password;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
