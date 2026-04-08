package com.info.replica.entity;

import javax.persistence.*;

@Entity
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_id", columnNames = {"id"})
})
public class User {
    @Id
    private Long id;

    @Column(name = "USER_NAME")
    private String name;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}