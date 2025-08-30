package com.example.securityExercice.Modal;


import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "folders")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Nom du dossier

    // Relation vers le dossier parent (null si c'est un dossier racine)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference // <- empêche Jackson de sérialiser le parent
    private Folder parent;

    // Relation avec les sous-dossiers
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnoreProperties({"parent", "subFolders", "files", "readers"})
 
    private List<Folder> subFolders;

    // Relation avec les fichiers contenus dans ce dossier
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
   
    private List<File> files;

    public Folder() {}

    public Folder(String name, Folder parent) {
        this.name = name;
        this.parent = parent;
    }
  @ManyToMany(mappedBy = "readableFolders")
  @JsonIgnore
  private Set<Users> readers = new java.util.HashSet<>();

  public Set<Users> getReaders() { return readers; }
  public void setReaders(Set<Users> readers) { this.readers = readers; }
    // getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Folder getParent() { return parent; }
    public void setParent(Folder parent) { this.parent = parent; }

    public List<Folder> getSubFolders() { return subFolders; }
    public void setSubFolders(List<Folder> subFolders) { this.subFolders = subFolders; }

    public List<File> getFiles() { return files; }
    public void setFiles(List<File> files) { this.files = files; }
}
