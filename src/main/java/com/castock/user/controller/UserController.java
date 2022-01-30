package com.castock.user.controller;

import com.castock.user.dto.ResponseDTO;
import com.castock.user.dto.UserDTO;
import com.castock.user.model.UserEntity;
import com.castock.user.security.TokenProvider;
import com.castock.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 요청을 이용해 저장할 사용자 생성하기
            UserEntity user = UserEntity.builder()
                                .email(userDTO.getEmail())
                                .username(userDTO.getUsername())
                                .password(passwordEncoder.encode(userDTO.getPassword()))
                                .build();
                            log.warn(userDTO.getEmail());
                            log.warn(userDTO.getUsername());
                            log.warn(userDTO.getPassword());
            // 서비스를 이용해 Repository 에 사용자 저장
            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                                .email(registeredUser.getEmail())
                                .id(registeredUser.getId())
                                .username(registeredUser.getUsername())
                                .build();
            
            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                .badRequest()
                .body(responseDTO);
        }
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.getByCredentials(
            userDTO.getEmail(),
            userDTO.getPassword(),
            passwordEncoder);
        
            if(user != null) {
                // 토큰 생성
                final String token = tokenProvider.create(user);
                final UserDTO responseUserDTO = UserDTO.builder()
                                .email(user.getEmail())
                                .id(user.getId())
                                .token(token)
                                .build();
                return ResponseEntity.ok().body(responseUserDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder()
                                .error("Login failed")
                                .build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
    }
}
