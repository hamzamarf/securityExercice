package com.example.securityExercice;

import java.util.List;



public class UserDto {
    private String username;
    private String service;
    private String password;
    private String role;
    private java.util.List<Long> folderIds; // <<— NOUVEAU

  // getters/setters…
  public java.util.List<Long> getFolderIds() { return folderIds; }
  public void setFolderIds(java.util.List<Long> folderIds) { this.folderIds = folderIds; }
    // Constructeur vide
    public UserDto() {}

    // Getters & Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

   
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    }


