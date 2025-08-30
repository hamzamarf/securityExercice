package com.example.securityExercice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.securityExercice.Modal.Users;
import com.example.securityExercice.Modal.myUserDetails;
import com.example.securityExercice.Repo.repoUser;

@Service
public class MyUserDetailsService  implements UserDetailsService{
@Autowired
private repoUser service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user =service.findByUsername(username);
        if(user==null){
            System.out.println("User not found");
        
                throw new UsernameNotFoundException("User not found");
}
        
    return   new myUserDetails(user) ;
    } 

}
