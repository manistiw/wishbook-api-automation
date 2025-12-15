package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Employee {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("role")
    private String role;

    @JsonProperty("token")
    private String token;

    public Employee() {
    }

    public Employee(String id, String name, String role, String token) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(role, employee.role) &&
                Objects.equals(token, employee.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, role, token);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", token='[REDACTED]'" +
                '}';
    }
}