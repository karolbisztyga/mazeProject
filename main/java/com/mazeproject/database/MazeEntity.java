package com.mazeproject.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.joda.time.DateTime;

@Entity
@Table(name="maze")
public class MazeEntity implements Serializable {
    
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;
    
    @Column(name="code")
    private String code;
    
    @Column(name="createdDate")
    private DateTime createdDate;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    
    
}
