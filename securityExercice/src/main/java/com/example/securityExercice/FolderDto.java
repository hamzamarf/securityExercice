package com.example.securityExercice;

import java.util.List;

public class FolderDto {
    private Long id;
    private String name;
    private List<FolderDto> subFolders;
    private List<FileDto> files;

    public FolderDto(Long id, String name, List<FolderDto> subFolders, List<FileDto> files) {
        this.id = id;
        this.name = name;
        this.subFolders = subFolders;
        this.files = files;
    }
    public FolderDto(){}

    // getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<FolderDto> getSubFolders() { return subFolders; }
    public void setSubFolders(List<FolderDto> subFolders) { this.subFolders = subFolders; }

    public List<FileDto> getFiles() { return files; }
    public void setFiles(List<FileDto> files) { this.files = files; }

}
