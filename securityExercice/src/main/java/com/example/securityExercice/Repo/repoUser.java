package com.example.securityExercice.Repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.securityExercice.Modal.Users;

@Repository
public interface  repoUser extends JpaRepository<Users,Long>
 {   public Users findByUsername(String username);



}
