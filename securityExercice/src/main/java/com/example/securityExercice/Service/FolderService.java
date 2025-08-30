package com.example.securityExercice.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.securityExercice.FileDto;
import com.example.securityExercice.FolderDto;
import com.example.securityExercice.Modal.Folder;
import com.example.securityExercice.Modal.Users;
import com.example.securityExercice.Repo.FolderRepository;
import com.example.securityExercice.Repo.repoUser;

import jakarta.transaction.Transactional;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepo;

    @Autowired
    private repoUser userepo;

    // Récupérer l'utilisateur courant
    public Users getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userepo.findByUsername(username);
        }
        return null;
    }

    // Récupérer l'arbre pour l'utilisateur courant
    public List<FolderDto> getFolderTreeForCurrentUser() {
        Users currentUser = getCurrentUser();
        List<Folder> allRoots = folderRepo.findByParentIsNull();

        return allRoots.stream()
                .map(f -> buildTreeForUser(f, currentUser, false)) // parentHasAccess = false pour racines
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Construit un arbre filtré selon les permissions, en propagant l'accès
    private FolderDto buildTreeForUser(Folder folder, Users user, boolean parentHasAccess) {
        if (folder == null) return null;

        boolean hasAccess = parentHasAccess || user.getRole().equals("ADMIN") || folder.getReaders().contains(user);
        if (!hasAccess) return null;

        FolderDto dto = new FolderDto();
        dto.setId(folder.getId());
        dto.setName(folder.getName());

        if (folder.getFiles() != null) {
            dto.setFiles(folder.getFiles().stream()
                    .map(f -> new FileDto(f.getId(), f.getFilename(), f.getFileType(), f.getUploadedAt()))
                    .collect(Collectors.toList()));
        }

        if (folder.getSubFolders() != null) {
            dto.setSubFolders(folder.getSubFolders().stream()
                    .map(f -> buildTreeForUser(f, user, hasAccess)) // propagation de l'accès
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // Ajouter un sous-dossier
    public FolderDto addSubFolder(Long parentId, String name) {
        Folder parent = folderRepo.findById(parentId).orElseThrow();
        Users currentUser = getCurrentUser();

        Folder sub = new Folder();
        sub.setName(name);
        sub.setParent(parent);

        Set<Users> readers = new HashSet<>();
        if (!currentUser.getRole().equals("ADMIN")) {
            readers.add(currentUser); // Chef ou utilisateur ajouté automatiquement
        }
        sub.setReaders(readers);

        Folder saved = folderRepo.save(sub);
        return buildTreeForUser(saved, currentUser, false);
    }

    // Renommer un dossier
    public FolderDto renameFolder(Long id, String newName) {
        Folder folder = folderRepo.findById(id).orElseThrow();
        folder.setName(newName);
        Folder updated = folderRepo.save(folder);
        Users currentUser = getCurrentUser();
        return buildTreeForUser(updated, currentUser, false);
    }

    // Supprimer un dossier
    @Transactional
    public void deleteFolder(Long id) {
        Folder folder = folderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        // Supprimer les références dans user_folders
        folder.getReaders().clear();

        // Supprimer récursivement les sous-dossiers
        if (folder.getSubFolders() != null) {
            for (Folder sub : folder.getSubFolders()) {
                sub.setParent(null);
                folderRepo.save(sub);
            }
        }

        // Supprimer les fichiers
        if (folder.getFiles() != null) {
            folder.getFiles().clear();
        }

        // Détacher du parent si nécessaire
        if (folder.getParent() != null) {
            folder.getParent().getSubFolders().remove(folder);
            folder.setParent(null);
        }

        folderRepo.delete(folder);
    }

    // Créer un dossier racine
    public FolderDto createFolder(String name) {
        Users currentUser = getCurrentUser();

        Folder folder = new Folder();
        folder.setName(name);
        folder.setParent(null); // racine

        Set<Users> readers = new HashSet<>();
        if (currentUser.getRole().equals("CHEF")) {
            readers.add(currentUser); // Chef a accès automatiquement
        } else if (!currentUser.getRole().equals("ADMIN")) {
            throw new RuntimeException("Vous n'avez pas le droit de créer un dossier racine !");
        }
        folder.setReaders(readers);

        Folder saved = folderRepo.save(folder);
        return buildTreeForUser(saved, currentUser, false);
    }
}
