package com.example.freemoneynoscam.model;

public class User {

  private String name;
  private String email;
  private String link;
  public User(String email) {
    this.email = email;
    this.link = "https://www.youtube.com/";
  }

  public User(String name, String email) {
    this.name = name;
    this.email = email;
    this.link = "https://www.youtube.com/";
  }

  public String getName() {
    if (name==null || name.isEmpty()) {
      return "No name";
    }
    return name;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    if (email==null || email.isEmpty()) {
      return "No email";
    }
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
