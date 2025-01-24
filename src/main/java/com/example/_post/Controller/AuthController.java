package com.example._post.Controller;

import com.example._post.DTO.LoginDTO;
import com.example._post.DTO.RegisterDTO;
import com.example._post.Service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO request) {
        var response = userService.registerUser(request);
        if(!response){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> login(@RequestBody LoginDTO request, HttpServletResponse response) {
        String loginRequest = userService.loginUser(request);
        if (loginRequest.length() == 1) {
            return ResponseEntity.badRequest().body("Not an user");
        }
        return ResponseEntity.ok().body(loginRequest);
    }
}
