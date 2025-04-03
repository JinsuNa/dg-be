package com.project.daeng_geun.controller;


import com.project.daeng_geun.dto.UserDTO;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.UserRepository;
import com.project.daeng_geun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    //  회원가입 API
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestPart("user") UserDTO userDTO, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return userService.register(userDTO, image);
    }

    //    닉네임 중복확인 api
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean isAvailable = userService.isUsername(username);
        return ResponseEntity.ok(isAvailable);
    }

    //    이메일 중복확인 api
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean isAvailable = userService.isEmail(email);
        System.out.println(userRepository.existsByEmail(email));
        return ResponseEntity.ok(isAvailable);
    }

    //    로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {

        Map<String, Object> response = userService.loginUser(userDTO);

        if (response.isEmpty()) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호를 확인하세요.");
        }
        return ResponseEntity.ok(response);
    }
}
