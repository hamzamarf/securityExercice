package com.example.securityExercice.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.securityExercice.Modal.File;
import com.example.securityExercice.Modal.Folder;

@Repository
public interface fileRepository extends JpaRepository<File,Long> {


    // Trouver les dossiers accessibles à un utilisateur donné
    List<File> findByUser_Id(Long userId);
    List<File> findByFolder(Folder folder);


}
