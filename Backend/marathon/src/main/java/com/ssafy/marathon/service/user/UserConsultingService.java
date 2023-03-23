package com.ssafy.marathon.service.user;

import com.ssafy.marathon.db.entity.consulting.Consulting;
import com.ssafy.marathon.db.repository.ConsultingRepository;
import com.ssafy.marathon.dto.request.consulting.ConsultingReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserConsultingService {

    private final ConsultingRepository consultingRepository;

    @Transactional
    public void createConsulting(ConsultingReqDto consultingReqDto) {
        Consulting consulting = Consulting.builder()
            .birthDate(consultingReqDto.getBirthDate())
            .name(consultingReqDto.getName())
            .sex(consultingReqDto.isSex())
            .email(consultingReqDto.getEmail())
            .phone1(consultingReqDto.getPhone1())
            .phone2(consultingReqDto.getPhone2())
            .phone2Relationship(consultingReqDto.getPhone2Relationship())
            .phone3(consultingReqDto.getPhone3())
            .phone3Relationship(consultingReqDto.getPhone3Relationship())
            .sickDate(consultingReqDto.getSickDate())
            .description(consultingReqDto.getDescription())
            .hopeDate(consultingReqDto.getHopeDate())
            .build();
        consultingRepository.save(consulting);
    }
}