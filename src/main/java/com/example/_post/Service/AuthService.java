package com.example._post.Service;

import com.example._post.DTO.LoginDTO;
import com.example._post.DTO.RegisterDTO;
import com.example._post.Model.User;
import com.example._post.Repository.IAuthRepository;
import com.example._post.Util.JWTUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AuthService {
    @Autowired
    private IAuthRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registerUser(RegisterDTO request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("USER");

        userRepository.save(newUser);
        return true;
    }

    public String loginUser(LoginDTO request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return jwtUtil.generateToken(user.get().username);
        }
        return "0";
    }
}
