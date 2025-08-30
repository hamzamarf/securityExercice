package com.example.securityExercice.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.securityExercice.Modal.Folder;
import com.example.securityExercice.Modal.Users;
import com.example.securityExercice.Repo.FolderRepository;
import com.example.securityExercice.Repo.repoUser;
import com.example.securityExercice.UserDto;

@Service
public class ServiceRegistre {

    @Autowired
    private repoUser repo;

    @Autowired
    private JWTService jwt;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FolderRepository folderRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    AuthenticationManager autmanger;

    // -------------------- Auth --------------------
    public String verify(Users users) {
        Authentication auth = autmanger.authenticate(
                new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));

        if (auth.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            Users userFromDB = repo.findByUsername(users.getUsername());
            Long id = userFromDB.getId();
            return jwt.generateToken(userDetails, role, id);
        }
        return "false";
    }

    // -------------------- Users --------------------
    public List<Users> getUsers() {
        return repo.findAll();
    }

    public Users getUserById(Long id) throws Exception {
        return repo.findById(id)
                .orElseThrow(() -> new Exception("Utilisateur non trouvé avec l'id: " + id));
    }

    // -------------------- Supprimer User --------------------
    public void deleteUser(Long id) {
        Users user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // vider les dossiers liés pour éviter la contrainte FK
        user.getReadableFolders().clear();
        repo.save(user);

        // maintenant on peut supprimer le user
        repo.delete(user);
    }

    // -------------------- Ajouter User --------------------
    public Users addUser(UserDto usedto) {
        Users user = new Users();
        user.setUsername(usedto.getUsername());
        user.setRole(usedto.getRole());
        user.setService(usedto.getService());
        user.setPassword(passwordEncoder.encode(usedto.getPassword()));

        if (usedto.getFolderIds() != null && !usedto.getFolderIds().isEmpty()) {
            Set<Folder> accessibleFolders = new HashSet<>();
            for (Long folderId : usedto.getFolderIds()) {
                Folder root = folderRepository.findById(folderId).orElseThrow();
                accessibleFolders.addAll(getAllSubFolders(root)); // inclut sous-dossiers
            }
            user.setReadableFolders(accessibleFolders);
        }

        return repo.save(user);
    }

    // -------------------- Mettre à jour User --------------------
    public Users updateUser(Long id, UserDto userdto) throws Exception {
        Users user = getUserById(id);
        user.setUsername(userdto.getUsername());
        user.setRole(userdto.getRole());
        user.setService(userdto.getService());

        if (userdto.getPassword() != null && !userdto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        }

        if (userdto.getFolderIds() != null) {
            Set<Folder> accessibleFolders = new HashSet<>();
            for (Long folderId : userdto.getFolderIds()) {
                Folder root = folderRepository.findById(folderId).orElseThrow();
                accessibleFolders.addAll(getAllSubFolders(root));
            }
            user.setReadableFolders(accessibleFolders);
        }

        return repo.save(user);
    }

    // -------------------- Chercher par username --------------------
    public Users findByUsername(String name) {
        return repo.findByUsername(name);
    }

    // -------------------- Méthode utilitaire --------------------
    // Récupère un dossier et tous ses sous-dossiers récursivement
    private Set<Folder> getAllSubFolders(Folder folder) {
        Set<Folder> all = new HashSet<>();
        all.add(folder);
        if (folder.getSubFolders() != null) {
            for (Folder sub : folder.getSubFolders()) {
                all.addAll(getAllSubFolders(sub));
            }
        }
        return all;
    }

}
