package com.unicauca.clientproducthttpclient.domain.entities;

import java.util.Date;

/**
 *
 * @author libardo
 */
public class GymClass {

    private Long id;
    private Date date;
    private Instructor instuctor;
    private String description;

    public GymClass(Long id, Date date, Instructor instructor, String description) {
        this.id = id;
        this.date = date;
        this.instuctor = instructor;
        this.description = description;
    }
    public GymClass() {

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Instructor getInstuctor() {
        return instuctor;
    }

    public void setInstuctor(Instructor instuctor) {
        this.instuctor = instuctor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
