package com.ssafy.marathon.service.patient;

import com.ssafy.marathon.config.security.JwtTokenProvider;
import com.ssafy.marathon.db.entity.user.Patient;
import com.ssafy.marathon.db.repository.PatientRepository;
import com.ssafy.marathon.dto.request.user.PatientReqDto;
import com.ssafy.marathon.dto.response.user.PatientResDto;
import com.ssafy.marathon.dto.response.user.SignUpResDto;
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
public class PatientSignServiceImpl implements PatientSignService {

    private final Logger LOGGER = LoggerFactory.getLogger(PatientSignServiceImpl.class);
    private final PatientRepository patientRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Service awsS3Service;
    private static String defaultImg = "https://d1v10kml6l14kq.cloudfront.net/default.jpg";

    @Transactional
    @Override
    public SignUpResDto signUp(PatientReqDto patientReqDto) {

        LOGGER.info("[PatientSignServiceImpl.signUp] 환자 회원가입 정보 전달");
        Patient patient = Patient.builder()
            .id(patientReqDto.getId())
            .roles(Collections.singletonList("ROLE_PATIENT"))
            .name(patientReqDto.getName())
            .password(passwordEncoder.encode(patientReqDto.getPassword()))
            .email(patientReqDto.getEmail())
            .sex(patientReqDto.isSex())
            .birthDate(patientReqDto.getBirthDate())
            .registDate(LocalDate.now())
            .phone(patientReqDto.getPhone())
            .mainPhone(patientReqDto.getMainPhone())
            .mainRelationship(patientReqDto.getMainRelationship())
            .subPhone(patientReqDto.getSubPhone())
            .subRelationship(patientReqDto.getSubRelationship())
            .img(defaultImg)
            .build();
        if (patientReqDto.getKakao() != null) {
            patient.updateUserKakao(patientReqDto.getKakao());
            patient.updateUserImg(patientReqDto.getImg());
        }
        Patient savedPatient = patientRepository.save(patient);
        SignUpResDto signUpResDto;

        LOGGER.info("[signUp] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if (!savedPatient.getName().isEmpty()) {
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
    public PatientResDto getPatient(Long seq) {
        Patient patient = patientRepository.getBySeq(seq);
        return PatientResDto.builder()
            .id(patient.getId())
            .name(patient.getName())
            .registDate(patient.getRegistDate())
            .email(patient.getEmail())
            .phone(patient.getPhone())
            .img(patient.getImg())
            .mainPhone(patient.getMainPhone())
            .mainRelationship(patient.getMainRelationship())
            .subPhone(patient.getSubPhone())
            .subRelationship(patient.getSubRelationship())
            .build();
    }

    @Transactional
    @Override
    public String modifyPatient(Long seq, PatientReqDto patientReqDto, MultipartFile image)
        throws Exception {

        LOGGER.info("[modifyPatient] 환자정보 수정 시작");
        Patient patient = patientRepository.getBySeq(seq);
        patient.updateUser(
            passwordEncoder.encode(patientReqDto.getPassword()),
            patientReqDto.getEmail(),
            patientReqDto.getPhone()
        );
        patient.updatePatientInfo(
            patientReqDto.getMainPhone(),
            patientReqDto.getMainRelationship(),
            patientReqDto.getSubPhone(),
            patientReqDto.getSubRelationship()
        );
        LOGGER.info("[modifyPatient] 이미지 비교 시작");
        //랜덤식별자 생성
        UUID uuid = UUID.randomUUID();
        //파일이름 설정
        String fileName = uuid + "_" + image.getOriginalFilename();
        //aws s3 저장
        String url = awsS3Service.uploadFileV1(fileName, image);
        patient.updateUserImg(url);
        //토큰 정보 수정
        String token = jwtTokenProvider.createToken(patient);
        LOGGER.info("[modifyPatient] 환자정보 수정 완료");
        return token;
    }

    @Transactional
    @Override
    public String modifyPatient(Long seq, PatientReqDto patientReqDto) {
        LOGGER.info("[modifyPatient] 환자정보 수정 시작");
        Patient patient = patientRepository.getBySeq(seq);
        patient.updateUser(
            passwordEncoder.encode(patientReqDto.getPassword()),
            patientReqDto.getEmail(),
            patientReqDto.getPhone()
        );
        patient.updatePatientInfo(
            patientReqDto.getMainPhone(),
            patientReqDto.getMainRelationship(),
            patientReqDto.getSubPhone(),
            patientReqDto.getSubRelationship()
        );
        String token = jwtTokenProvider.createToken(patient);
        LOGGER.info("[modifyPatient] 환자정보 수정 완료");
        return token;
    }


}