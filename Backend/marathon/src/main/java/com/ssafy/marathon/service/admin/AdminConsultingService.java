package com.ssafy.marathon.service.admin;

import com.ssafy.marathon.db.entity.consulting.Consulting;
import com.ssafy.marathon.db.repository.ConsultingRepository;
import com.ssafy.marathon.dto.response.consulting.ConsultingResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminConsultingService {
    private final ConsultingRepository consultingRepository;

    public ConsultingResDto getDetailConsulting(Long consultingSeq) {
        Optional<Consulting> findConsulting = consultingRepository.findById(consultingSeq);
        Consulting consulting = findConsulting.orElseThrow();

        ConsultingResDto consultingResDto = ConsultingResDto.builder()
            .name(consulting.getName())
            .birthDate(LocalDate.now())
            .email(consulting.getEmail())
            .phone1(consulting.getPhone1())
            .phone2(consulting.getPhone2())
            .phone3(consulting.getPhone3())
            .sickDate(LocalDate.now())
            .description(consulting.getDescription())
            .hopeDate(LocalDate.now())
            .checked(consulting.isChecked())
            .build();
        return consultingResDto;
    }

    public Page<ConsultingResDto> getConsultingPages(int pageNum, boolean checkedOrder) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);

        Page<ConsultingResDto> consultingResDtoPages;

        if (!checkedOrder) {
            consultingResDtoPages = consultingRepository.findAllByOrderByHopeDateDesc(pageRequest)
                .map(consulting -> ConsultingResDto.builder()
                    .consultingSeq(consulting.getConsultingSeq())
                    .name(consulting.getName())
                    .phone1(consulting.getPhone1())
                    .hopeDate(consulting.getHopeDate())
                    .checked(consulting.isChecked())
                    .build());
        } else {
            consultingResDtoPages = consultingRepository.findAllByOrderByCheckedAscHopeDateDesc(pageRequest)
                .map(consulting -> ConsultingResDto.builder()
                    .consultingSeq(consulting.getConsultingSeq())
                    .name(consulting.getName())
                    .phone1(consulting.getPhone1())
                    .hopeDate(consulting.getHopeDate())
                    .checked(consulting.isChecked())
                    .build());
        }

        return consultingResDtoPages;
    }

    public void checkConsulting(Long consultingSeq) {
        Optional<Consulting> findConsulting = consultingRepository.findById(consultingSeq);
        Consulting consulting = findConsulting.orElseThrow();
        consulting.changeChecked();
    }

}