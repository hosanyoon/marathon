package com.ssafy.marathon.service.doctor;

import com.ssafy.marathon.db.entity.communication.Alarm;
import com.ssafy.marathon.db.entity.treatment.Treatment;
import com.ssafy.marathon.db.repository.CommunicationRepository;
import com.ssafy.marathon.db.repository.TreatmentRepository;
import com.ssafy.marathon.dto.request.communication.AlarmReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorCommunicationService {

    private final TreatmentRepository treatmentRepository;
    private final CommunicationRepository<Alarm> alarmRepository;

    @Transactional
    public void CreateAlarm(AlarmReqDto alarmReqDto) {
        Optional<Treatment> findTreatment = treatmentRepository.findById(
            alarmReqDto.getTreatmentSeq());
        Treatment treatment = findTreatment.orElseThrow();

        Alarm alarm = Alarm.builder()
            .sender(treatment.getDoctor())
            .receiver(treatment.getPatient())
            .link(alarmReqDto.getLink()) // 링크는 나중에
            .checked(false)
            .dateTime(LocalDateTime.now())
            .build();
        alarmRepository.save(alarm);
    }
}