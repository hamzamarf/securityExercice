package com.example.securityExercice.Modal;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role;
    private String service;

    // ← Nouvelle relation pour les dossiers accessibles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_folders",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "folder_id")
    )
    @JsonIgnoreProperties({"parent", "subFolders", "files", "readers"}) // ⬅ empêche la boucle
    
  private Set<Folder> readableFolders = new HashSet<>();

    public Users() {}

    public Users(Long id, String username, String password, String role, String service) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.service = service;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

   public Set<Folder> getReadableFolders() { return readableFolders; }
  public void setReadableFolders(Set<Folder> readableFolders) { this.readableFolders = readableFolders; }
}
