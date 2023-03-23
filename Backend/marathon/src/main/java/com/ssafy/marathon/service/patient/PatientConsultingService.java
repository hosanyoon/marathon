package com.ssafy.marathon.service.patient;

import com.ssafy.marathon.db.entity.user.Patient;
import com.ssafy.marathon.db.repository.PatientRepository;
import com.ssafy.marathon.dto.response.consulting.ConsultingResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientConsultingService {

    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public ConsultingResDto getConsultingForm(Long userSeq) {
        Optional<Patient> findPatient = patientRepository.findById(userSeq);
        Patient patient = findPatient.orElseThrow();

        return ConsultingResDto.builder()
            .name(patient.getName())
            .sex(patient.isSex())
            .birthDate(patient.getBirthDate())
            .email(patient.getEmail())
            .phone1(patient.getPhone())
            .phone2(patient.getMainPhone())
            .phone2Relationship(patient.getMainRelationship())
            .phone3(patient.getSubPhone())
            .phone3Relationship(patient.getSubRelationship())
            .build();
    }
}