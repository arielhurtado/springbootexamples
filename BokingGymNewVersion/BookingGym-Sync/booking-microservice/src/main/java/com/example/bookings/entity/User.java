package com.example.bookings.entity;

import java.io.Serializable;
public class User implements Serializable {
  private Long id;
  private String firstName;
  private String lastName;
  private Facility facility;

  public User(Long id, String firsName, String lastName, Facility facility) {
    this.id = id;
    this.firstName = firsName;
    this.lastName = lastName;
    this.facility = facility;
  }
  public User(){

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Facility getFacility() {
    return facility;
  }

  public void setFacility(Facility facility) {
    this.facility = facility;
  }
}
