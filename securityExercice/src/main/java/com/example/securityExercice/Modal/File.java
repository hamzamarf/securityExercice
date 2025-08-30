package com.example.securityExercice.Modal;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;  // lien avec le propriétaire
    private String filename ;
    @Enumerated(EnumType.STRING)
    private FileType fileType;  
    private String path;         
    private LocalDateTime uploadedAt = LocalDateTime.now();
    @ManyToOne
   @JoinColumn(name = "folder_id")
   @JsonIgnore

    private Folder folder;

  
   
    public Folder getFolder() {
        return folder;
    }
    public void setFolder(Folder folder) {
        this.folder = folder;
    }
    public enum FileType {
     pdf,
    word
}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public FileType getFileType() {
        return fileType;
    }
    public File()
    {}
    public File(Long id, Users user, String filename, FileType fileType, String path, LocalDateTime uploadedAt) {
        this.id = id;
        this.user = user;
        this.filename = filename;
        this.fileType = fileType;
        this.path = path;
        this.uploadedAt = uploadedAt;

    }
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
   
}
