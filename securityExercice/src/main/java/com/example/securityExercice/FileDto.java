package com.example.securityExercice;

import java.time.LocalDateTime;

import com.example.securityExercice.Modal.File.FileType;

public class FileDto {
    private Long id;
    private String filename;
    private FileType fileType; // sous forme de String pour simplifier
    private String path;
    private LocalDateTime uploadedAt;
    private Long userId;   // on ne renvoie pas tout l'objet Users, juste son id
    private Long folderId; // pareil pour le dossier

    public FileDto() {}

       public FileDto(Long id, String filename, FileType fileType, LocalDateTime uploadedAt) {
        this.id = id;
        this.filename = filename;
        this.fileType = fileType;
        this.uploadedAt = uploadedAt;
    }
    // Getters et Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFolderId() {
        return folderId;
    }
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
}
