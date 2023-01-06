package com.grigorev.srm.entity;

public class User {

    private Integer id;
    private String firstName;
    private String secondName;
    private Role role;
    private String username;
    private String password;

    public User() {
    }

    public User(Integer id, String firstName, String secondName, Role role, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
