package com.example.securityExercice;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String rawPassword = "123456";
String hashedPassword = encoder.encode(rawPassword);
System.out.println("Nouveau hash: " + hashedPassword);
    }
}
