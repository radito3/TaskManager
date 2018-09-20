package com.sap.exercise.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "User")
public class User extends BaseEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "mysql->int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "mysql->varchar(128)", name = "Name", nullable = false)
    private String name;

    @Column(columnDefinition = "mysql->varchar(64)", name = "Email")
    private String email;

    public User() {
        this("default");
    }

    public User(String name) {
        this(name, "");
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                "}";
    }
}
