package com.example.securityExercice.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.securityExercice.FolderDto;
import com.example.securityExercice.Modal.*;
import com.example.securityExercice.Repo.*;
import com.example.securityExercice.Service.*;
import com.example.securityExercice.UserDto;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://10.1.3.98")
public class UserController {

    @Autowired private fileService fileservice;
    @Autowired private ServiceRegistre reg;
    @Autowired private repoUser repo;
    @Autowired private FolderService folderService;
    @Autowired private FolderRepository folderepo;
    @Autowired private PasswordEncoder passwordEncoder;

    // ----------------- Auth -----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        String token = reg.verify(user);
        if ("false".equals(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }
        return ResponseEntity.ok(Map.of("token", token));
    }

    // ----------------- Users -----------------
    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers() {
        return ResponseEntity.ok(reg.getUsers());
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userdto) {
        try {
            Users user = reg.addUser(userdto);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userdto) {
        try {
            Users user = reg.updateUser(id, userdto);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            reg.deleteUser(id);
            return ResponseEntity.ok("Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user");
        }
    }

    // ----------------- Files -----------------
    @GetMapping("/files")
    public ResponseEntity<List<File>> getFiles() {
        return ResponseEntity.ok(fileservice.getFiles());
    }

    @GetMapping("/files/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) throws IOException {
        Optional<File> fileOptional = fileservice.getFile(id);
        if (fileOptional.isEmpty()) return ResponseEntity.notFound().build();

        File file = fileOptional.get();
        Path path = Paths.get(file.getPath());
        byte[] data = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(data);
    }

    @GetMapping("/files/{id}/view")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) throws IOException {
        Optional<File> fileOptional = fileservice.getFile(id);
        if (fileOptional.isEmpty()) return ResponseEntity.notFound().build();

        File file = fileOptional.get();
        Path path = Paths.get(file.getPath());
        byte[] data = Files.readAllBytes(path);

        String contentType = file.getFileType() == File.FileType.pdf
                ? "application/pdf"
                : "application/octet-stream";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(data);
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        Optional<File> fileOptional = fileservice.getFile(id);
        if (fileOptional.isEmpty()) return ResponseEntity.notFound().build();

        try {
            fileservice.deleteFile(fileOptional.get());
            return ResponseEntity.ok(Map.of("message", "File deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error deleting file"));
        }
    }

    @PutMapping("/files/{id}")
    public ResponseEntity<?> updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile newFile) {
        try {
            File updatedFile = fileservice.updateFile(id, newFile);
            return ResponseEntity.ok(updatedFile);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/files/upload/{folderId}")
    public ResponseEntity<?> uploadFileToFolder(
            @PathVariable Long folderId,
            @RequestParam("file") MultipartFile file) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            Users user = repo.findByUsername(username);
            File uploadedFile = fileservice.uploadFileToFolder(folderId, file, user);
            return ResponseEntity.ok(uploadedFile);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ----------------- Folders -----------------

    // récupère l’arbre filtré selon rôle (ADMIN/CHEF = tous, USER = ses accès)
    @GetMapping("/folders")
    public ResponseEntity<List<FolderDto>> getFolders(Authentication authentication) {
        return ResponseEntity.ok(folderService.getFolderTreeForCurrentUser());
    }

    // CRUD Folders
   @PostMapping("/folders")
public ResponseEntity<FolderDto> createRootFolder(@RequestBody Map<String, String> body) {
    String name = body.get("name");
    FolderDto folder = folderService.createFolder(name);
    return ResponseEntity.ok(folder);
}

    @PostMapping("/folders/{parentId}/subfolder")
    public ResponseEntity<FolderDto> addSubFolder(@PathVariable Long parentId, @RequestBody Map<String, String> body) {
        String name = body.get("name");
        FolderDto subFolder = folderService.addSubFolder(parentId, name);
        return ResponseEntity.ok(subFolder);
    }

    @PutMapping("/folders/{id}/rename")
    public ResponseEntity<FolderDto> renameFolder(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newName = body.get("name");
        FolderDto updated = folderService.renameFolder(id, newName);
        return ResponseEntity.ok(updated);
    }

  @DeleteMapping("/folders/{id}")
public ResponseEntity<?> deleteFolder(@PathVariable Long id) {
    try {
        folderService.deleteFolder(id);
        return ResponseEntity.ok(Map.of("message", "Folder deleted successfully"));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
    }
}
}
