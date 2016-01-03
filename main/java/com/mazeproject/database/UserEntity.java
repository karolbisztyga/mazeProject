package com.mazeproject.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class UserEntity implements Serializable {
    
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="email")
    private String email;
    
    @Column(name="password")
    private String password;
    
    @Column(name="money")
    private int money;

    public UserEntity() {
        this(null, null, null, 0);
    }

    public UserEntity(String name, String email, String password) {
        this(name, email, password, 0);
    }

    public UserEntity(String name, String email, String password, int money) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.money = money;
    }
    
    public int getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    
    
    
}
