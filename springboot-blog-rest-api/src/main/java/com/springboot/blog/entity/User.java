package com.springboot.blog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",  // Create turnTable(中介表), define name, joinColumn
                joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // define user entity 中介表關聯至"擁有者" User
                inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) // 中介表關聯至"目標方" Roles

    private Set<Role> roles;
}
