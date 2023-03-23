package com.ssafy.marathon.service.doctor;

import com.ssafy.marathon.config.security.JwtTokenProvider;
import com.ssafy.marathon.db.entity.user.Doctor;
import com.ssafy.marathon.db.repository.DoctorRepository;
import com.ssafy.marathon.db.repository.UserRepository;
import com.ssafy.marathon.dto.request.user.DoctorReqDto;
import com.ssafy.marathon.dto.response.user.DoctorResDto;
import com.ssafy.marathon.dto.response.user.SignUpResDto;
import com.ssafy.marathon.service.patient.PatientSignServiceImpl;
import com.ssafy.marathon.service.user.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorSignServiceImpl implements DoctorSignService {
    private final Logger LOGGER = LoggerFactory.getLogger(PatientSignServiceImpl.class);
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AwsS3Service awsS3Service;

    private static String defaultImg = "https://d1v10kml6l14kq.cloudfront.net/default.jpg";

    @Transactional
    @Override
    public SignUpResDto signUp(DoctorReqDto doctorReqDto) {
        LOGGER.info("[signUp] 의사 회원가입 정보 전달");
        Doctor doctor = Doctor.builder()
            .id(doctorReqDto.getId())
            .roles(Collections.singletonList("ROLE_DOCTOR"))
            .name(doctorReqDto.getName())
            .sex(doctorReqDto.isSex())
            .password(passwordEncoder.encode(doctorReqDto.getPassword()))
            .email(doctorReqDto.getEmail())
            .birthDate(doctorReqDto.getBirthDate())
            .registDate(LocalDate.now())
            .phone(doctorReqDto.getPhone())
            .license(doctorReqDto.getLicense())
            .degree(doctorReqDto.getDegree())
            .introduce("안녕하세요. 잘부탁드립니다^^")
            .img(defaultImg)
            .build();
        if (doctorReqDto.getKakao() != null) {
            doctor.updateUserKakao(doctorReqDto.getKakao());
            doctor.updateUserImg(doctorReqDto.getImg());
        }
        Doctor savedDoctor = (Doctor) doctorRepository.save(doctor);
        SignUpResDto signUpResDto;

        LOGGER.info("[signUp] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if (!savedDoctor.getName().isEmpty()) {
            LOGGER.info("[signUp] 정상 처리 완료");
            signUpResDto = SignUpResDto.builder().success(true).msg("회원가입 성공").build();
        } else {
            LOGGER.info("[signUp] 실패 처리 완료");
            signUpResDto = SignUpResDto.builder().success(false).msg("회원가입 실패").build();
        }
        return signUpResDto;
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorResDto getDoctor(Long seq) {
        Doctor doctor = doctorRepository.getBySeq(seq);
        DoctorResDto loadedDoctor = DoctorResDto.builder()
            .id(doctor.getId())
            .name(doctor.getName())
            .registDate(doctor.getRegistDate())
            .email(doctor.getEmail())
            .phone(doctor.getPhone())
            .img(doctor.getImg())
            .introduce(doctor.getIntroduce())
            .build();
        return loadedDoctor;
    }

    @Transactional
    @Override
    public String modifyDoctor(Long seq, DoctorReqDto doctorReqDto, MultipartFile image)
        throws Exception {
        LOGGER.info("[modifyPatient] 환자정보 수정 시작");
        Doctor doctor = doctorRepository.getBySeq(seq);
        doctor.updateUser(
            passwordEncoder.encode(doctorReqDto.getPassword()),
            doctorReqDto.getEmail(),
            doctorReqDto.getPhone()
        );
        doctor.updateIntroduce(doctorReqDto.getIntroduce());
        LOGGER.info("[modifyPatient] 환자정보 수정 시작");
        //랜덤식별자 생성
        UUID uuid = UUID.randomUUID();
        //파일이름 설정
        String fileName = uuid + "_" + image.getOriginalFilename();
        //aws s3 저장
        String url = awsS3Service.uploadFileV1(fileName, image);
        LOGGER.info("바뀐 이미지 url : {}", url);
        doctor.updateUserImg(url);
        String token = jwtTokenProvider.createToken(doctor);
        LOGGER.info("[modifyPatient] 환자정보 수정 완료");
        return token;
    }

    @Transactional
    @Override
    public String modifyDoctor(Long seq, DoctorReqDto doctorReqDto)
        throws Exception {
        LOGGER.info("[modifyPatient] 환자정보 수정 시작");
        Doctor doctor = doctorRepository.getBySeq(seq);
        doctor.updateUser(
            passwordEncoder.encode(doctorReqDto.getPassword()),
            doctorReqDto.getEmail(),
            doctorReqDto.getPhone()
        );
        doctor.updateIntroduce(doctorReqDto.getIntroduce());
        String token = jwtTokenProvider.createToken(doctor);
        LOGGER.info("[modifyPatient] 환자정보 수정 완료");
        return token;
    }


}