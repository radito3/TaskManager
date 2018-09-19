package com.sap.exercise.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "User")
public class User extends BaseEvent implements Serializable {

    //TODO add remaining fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "mysql->int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    public User() {

    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
