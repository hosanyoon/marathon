package com.ssafy.marathon.service.admin;

import com.ssafy.marathon.config.security.JwtTokenProvider;
import com.ssafy.marathon.db.entity.user.Admin;
import com.ssafy.marathon.db.repository.AdminRepository;
import com.ssafy.marathon.dto.request.user.UserReqDto;
import com.ssafy.marathon.dto.response.user.PatientResDto;
import com.ssafy.marathon.dto.response.user.UserResDto;
import com.ssafy.marathon.service.user.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminSignService {
    private final Logger LOGGER = LoggerFactory.getLogger(AdminSignService.class);
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Service awsS3Service;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public UserResDto getAdmin(Long seq) {
        Admin admin = adminRepository.getBySeq(seq);
        UserResDto loadedAdmin = PatientResDto.builder()
            .id(admin.getId())
            .name(admin.getName())
            .registDate(admin.getRegistDate())
            .email(admin.getEmail())
            .phone(admin.getPhone())
            .img(admin.getImg())
            .build();
        return loadedAdmin;
    }

    @Transactional
    public String modifyAdmin(Long seq, UserReqDto adminReqDto, MultipartFile image)
        throws Exception {
        LOGGER.info("[modifyAdmin] 관리자정보 수정 시작");
        Admin admin = adminRepository.getBySeq(seq);
        admin.updateUser(
            passwordEncoder.encode(adminReqDto.getPassword()),
            adminReqDto.getEmail(),
            adminReqDto.getPhone()
        );
        LOGGER.info("[modifyAdmin] 이미지 비교 시작");
        //이미지 url이 다르면 파일 저장하고 유저이미지 정보 수정
        if (adminReqDto.getImg() != admin.getImg()) {
            //랜덤식별자 생성
            UUID uuid = UUID.randomUUID();
            //파일이름 설정
            String fileName = uuid + "_" + image.getOriginalFilename();
            //aws s3 저장
            String url = awsS3Service.uploadFileV1(fileName, image);
            admin.updateUserImg(url);
        }
        //토큰 정보 수정
        String token = jwtTokenProvider.createToken(admin);
        LOGGER.info("[modifyAdmin] 관리자정보 수정 완료");
        return token;
    }
}