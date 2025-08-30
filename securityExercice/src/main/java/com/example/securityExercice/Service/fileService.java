package com.example.securityExercice.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.securityExercice.Modal.File;
import com.example.securityExercice.Modal.Folder;
import com.example.securityExercice.Modal.Users;
import com.example.securityExercice.Repo.FolderRepository;
import com.example.securityExercice.Repo.fileRepository;

import jakarta.transaction.Transactional;

@Service
public class fileService {

    // 📂 Dossier local pour stocker les fichiers
    private static final String UPLOADS_FOLDER = "uploads/";

    @Autowired
    private final fileRepository filerepository;

    @Autowired
    private FolderRepository folderRepository;

    public fileService(fileRepository filerepository) {
        this.filerepository = filerepository;
    }

    public List<File> getFiles() {
        return filerepository.findAll();
    }

    public Optional<File> getFile(Long id) {
        return filerepository.findById(id);
    }

    /**
     * Upload d'un fichier vers un dossier spécifique dans "uploads/"
     */
    public File uploadFileToFolder(Long folderId, MultipartFile file, Users user) throws IOException {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        String fileName = file.getOriginalFilename();
        if (fileName == null) throw new IOException("Nom de fichier invalide");

        // 📂 Chemin physique : uploads/{folderId}/{nomFichier}
        Path folderPath = Paths.get(UPLOADS_FOLDER, String.valueOf(folderId));
        Path filePath = folderPath.resolve(fileName);

        // Créer le dossier si nécessaire
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        // Écrire le fichier physiquement
        Files.write(filePath, file.getBytes());

        // Créer l'objet File pour la DB
        File newFile = new File();
        newFile.setFilename(fileName);
        newFile.setPath(filePath.toString());
        newFile.setFolder(folder);
        newFile.setUser(user);
        newFile.setFileType(fileName.toLowerCase().endsWith(".docx") ? File.FileType.word : File.FileType.pdf);
        newFile.setUploadedAt(LocalDateTime.now());

        return filerepository.save(newFile);
    }

    /**
     * Mise à jour d'un fichier existant
     */
    public File updateFile(Long fileId, MultipartFile newFile) throws IOException {
        File existingFile = filerepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Fichier introuvable"));

        // Supprimer l'ancien fichier
        Path oldPath = Paths.get(existingFile.getPath());
        if (Files.exists(oldPath)) {
            Files.delete(oldPath);
        }

        // Sauvegarder le nouveau fichier
        String fileName = newFile.getOriginalFilename();
        if (fileName == null) throw new IOException("Nom de fichier invalide");

        Path folderPath = Paths.get(UPLOADS_FOLDER, String.valueOf(existingFile.getFolder().getId()));
        Path newPath = folderPath.resolve(fileName);

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        Files.write(newPath, newFile.getBytes());

        existingFile.setFilename(fileName);
        existingFile.setPath(newPath.toString());
        existingFile.setFileType(fileName.toLowerCase().endsWith(".docx") ? File.FileType.word : File.FileType.pdf);
        existingFile.setUploadedAt(LocalDateTime.now());

        return filerepository.save(existingFile);
    }

    /**
     * Suppression d'un fichier
     */
    @Transactional
    public void deleteFile(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        if (Files.exists(path)) {
            Files.delete(path);
        }

        if (file.getFolder() != null) {
            file.getFolder().getFiles().remove(file);
            file.setFolder(null);
        }

        filerepository.delete(file);
        filerepository.flush();
        System.out.println("Fichier supprimé du disque et de la DB : " + file.getFilename());
    }

    /**
     * Rechercher des fichiers par nom
     */
    public List<File> getFilesByOriginalFilename(String originalFilename) {
        return filerepository.findAll().stream()
                .filter(f -> f.getFilename().contains(originalFilename))
                .toList();
    }
}
