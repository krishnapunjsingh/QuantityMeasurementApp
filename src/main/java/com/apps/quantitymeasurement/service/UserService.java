
package com.apps.quantitymeasurement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apps.quantitymeasurement.dto.LoginDTO;
import com.apps.quantitymeasurement.dto.RegisterDTO;
import com.apps.quantitymeasurement.entity.User;
import com.apps.quantitymeasurement.repository.UserRepository;
import com.apps.quantitymeasurement.security.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // REGISTER
    public String register(RegisterDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return "User already exists";
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // HASH PASSWORD HERE
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        return "User Registered Successfully";
    }

    // LOGIN
    public String login(LoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);

        if (user == null) {
            return "User not found ";
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return "Invalid password ";
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
