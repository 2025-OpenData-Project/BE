package com.opendata.domain.register.service;

import com.opendata.domain.register.dto.RegisterRequest;
import com.opendata.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegisterService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String registerUser(RegisterRequest registerRequest){
        userRepository.save(registerRequest.from(passwordEncoder.encode(registerRequest.password())));
        return "회원가입 성공";
    }

    public boolean checkEmail(String email){
        return userRepository.findUserByEmail(email) == null;
    }
}
