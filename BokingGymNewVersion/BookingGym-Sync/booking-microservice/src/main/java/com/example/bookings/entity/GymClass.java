package com.example.bookings.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class GymClass {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private Date date;

  @ManyToOne
  @JoinColumn(name = "instructor_id")
  private Instructor instuctor;
  private String description;

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
