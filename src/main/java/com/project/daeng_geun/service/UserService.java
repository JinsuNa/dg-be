package com.project.daeng_geun.service;


import com.project.daeng_geun.dto.UserDTO;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    //    회원가입
    public ResponseEntity<Map<String, Object>> register(UserDTO userDTO, MultipartFile image) throws IOException {
        Map<String, Object> response = new HashMap<>();

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            response.put("success", false);
            response.put("message", "이미 사용 중인 이메일입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String imageUrl = (image != null) ? s3Service.uploadFile(image) : null;

        // 사용자 저장
        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .nickname(userDTO.getNickname())
                .address(userDTO.getAddress())
                .location(userDTO.getLocation())
                .petName(userDTO.getPetName())
                .petBreed(userDTO.getPetBreed())
                .petAge(userDTO.getPetAge())
                .petGender(userDTO.getPetGender())
                .petPersonality(userDTO.getPetPersonality())
                .image(imageUrl)
                .build();

        userRepository.save(user);


        // JSON 형식으로 응답 생성
        response.put("success", true);
        response.put("message", "회원가입 성공!");
        response.put("user", user);


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //    아이디 중복 확인
    public boolean isUsername(String username) {
        return !userRepository.existsByNickname(username);
    }

    //    이메일 중복 확인
    public boolean isEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    //    로그인
    public Map<String, Object> loginUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("userId", user.getId());
                response.put("email", user.getEmail());
                response.put("nickname", user.getNickname());

                return response;
            }
        }
        return new HashMap<>();
    }

}
