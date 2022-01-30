package com.castock.user.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.castock.user.dto.ResponseDTO;
import com.castock.user.dto.UserDTO;
import com.castock.user.security.TokenProvider;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;

@RestController
@RequestMapping("/test")
public class JwtValidationTest {
    
    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("jwt")
    public ResponseEntity<?> testJwt(HttpServletRequest request) throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);
            // validateAndGetUserId
            if(token != null && !token.equalsIgnoreCase("null")) {
                final String userId = tokenProvider.validateAndGetUserId(token);
                final UserDTO responsUserDTO = UserDTO.builder()
                            .id(userId)
                            .build();
                return ResponseEntity.ok().body(responsUserDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder()
                            .error("Token is null")
                            .build();
            return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                            .error("Invalid Token")
                            .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    private String parseBearerToken(HttpServletRequest request) {
        // HTTP 요청의 헤더를 파싱해 Bearer 토큰을 리턴
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }   
}
