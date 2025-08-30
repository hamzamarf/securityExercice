package com.example.securityExercice.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.securityExercice.Modal.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    // Récupérer tous les dossiers racine
    List<Folder> findByParentIsNull();

@Query("""
           SELECT DISTINCT f
           FROM Folder f
           JOIN f.readers u
           WHERE u.id = :userId
             AND f.parent IS NULL
           """)
    List<Folder> findRootFoldersByUser(@Param("userId") Long userId);
}
