package com.ssafy.marathon.service.doctor;

import com.ssafy.marathon.db.entity.treatment.History;
import com.ssafy.marathon.db.entity.treatment.Treatment;
import com.ssafy.marathon.db.entity.user.Doctor;
import com.ssafy.marathon.db.repository.HistoryRepository;
import com.ssafy.marathon.db.repository.TreatmentRepository;
import com.ssafy.marathon.db.repository.UserRepository;
import com.ssafy.marathon.dto.request.treatment.HistoryReqDto;
import com.ssafy.marathon.dto.response.treatment.HistoryResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorHistoryServiceImpl implements DoctorHistoryService {

    private final HistoryRepository historyRepository;
    private final TreatmentRepository treatmentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<HistoryResDto> getNonFeedbackPages(int page, Long doctorSeq) {

        List<History> list = historyRepository.findAllByDoctor_SeqAndFeedbackEquals(doctorSeq, "");

        List<HistoryResDto> HistoryResList = new ArrayList<>();

        for (History history : list) {
            HistoryResDto historyResDto = HistoryResDto.builder()
                .historySeq(history.getSeq())
                .patientName(history.getPatient().getName())
                .dateTime(LocalDateTime.of(history.getDate(), history.getTime()))
                .day(history.getDate().getDayOfWeek().toString())
                .build();
            System.out.println(historyResDto.toString());
            HistoryResList.add(historyResDto);
        }

//        list to page
        PageRequest pageRequestForList = PageRequest.of(page, 5);
        int start = (int) pageRequestForList.getOffset();
        int end = Math.min((start + pageRequestForList.getPageSize()), HistoryResList.size());
        Page<HistoryResDto> historyResDtoPage = new PageImpl<>(HistoryResList.subList(start, end),
            pageRequestForList, HistoryResList.size());

        return historyResDtoPage;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<HistoryResDto> getFeedbackPages(int page, Long doctorSeq) {
        List<History> list = historyRepository.findAllByDoctor_Seq(doctorSeq);

        List<HistoryResDto> HistoryResList = new ArrayList<>();

        for (History history : list) {
            HistoryResDto historyResDto = HistoryResDto.builder()
                .historySeq(history.getSeq())
                .patientName(history.getPatient().getName())
                .dateTime(LocalDateTime.of(history.getDate(), history.getTime()))
                .day(history.getDate().getDayOfWeek().toString())
                .patientPhone(history.getPatient().getPhone())
                .build();

            HistoryResList.add(historyResDto);
        }

//        list to page
        PageRequest pageRequestForList = PageRequest.of(page, 10);
        int start = (int) pageRequestForList.getOffset();
        int end = Math.min((start + pageRequestForList.getPageSize()), HistoryResList.size());
        Page<HistoryResDto> historyResDtoPage = new PageImpl<>(HistoryResList.subList(start, end),
            pageRequestForList, HistoryResList.size());

        return historyResDtoPage;
    }

    @Transactional
    @Override
    public Void writeFeedback(HistoryReqDto historyReqDto) {
        History history = historyRepository.findBySeq(historyReqDto.getHistorySeq());
        history.updateFeedback(historyReqDto.getFeedback());
        historyRepository.save(history);
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public HistoryResDto getHistoryDetail(Long historySeq) {
        History history = historyRepository.findBySeq(historySeq);

        HistoryResDto historyResDto = HistoryResDto.builder()
            .historySeq(history.getSeq())
            .patientName(history.getPatient().getName())
            .patientPhone(history.getPatient().getPhone())
            .patientMainPhone(history.getPatient().getMainPhone())
            .dateTime(LocalDateTime.of(history.getDate(), history.getTime()))
            .day(history.getDate().getDayOfWeek().toString())
            .videoUrl(history.getVideoUrl())
            .feedback(history.getFeedback())
            .patientImg(history.getPatient().getImg())
            .build();


        return historyResDto;
    }

    @Transactional
    @Override
    public void makeHistory(Long doctorSeq, Long treatmentSeq) {
        Treatment treatment = treatmentRepository.findBySeq(treatmentSeq);
        History history = History.builder()
            .doctor((Doctor) userRepository.findBySeq(doctorSeq))
            .patient(treatment.getPatient())
            .feedback("")
            .date(treatment.getDate())
            .time(treatment.getTime())
            .build();
        historyRepository.save(history);
    }

    @Transactional(readOnly = true)
    @Override
    public Long getHistorySeq(Long treatmentSeq) {
        Treatment treatment = treatmentRepository.findBySeq(treatmentSeq);
        History history = historyRepository.findByDoctor_SeqAndPatient_SeqAndDateAndTime(
            treatment.getDoctor().getSeq(),
            treatment.getPatient().getSeq(),
            treatment.getDate(),
            treatment.getTime()
        );
        return history.getSeq();
    }

    @Transactional(readOnly = true)
    public Page<HistoryResDto> searchPaitentHistory(Long doctorSeq, String name, int page) {
        List<History> list = historyRepository.findAllByDoctor_SeqAndPatient_NameContaining(doctorSeq, name);

        List<HistoryResDto> HistoryResList = new ArrayList<>();

        for (History history : list) {
            HistoryResDto historyResDto = HistoryResDto.builder()
                .historySeq(history.getSeq())
                .patientName(history.getPatient().getName())
                .dateTime(LocalDateTime.of(history.getDate(), history.getTime()))
                .day(history.getDate().getDayOfWeek().toString())
                .patientPhone(history.getPatient().getPhone())
                .build();

            HistoryResList.add(historyResDto);
        }

//        list to page
        PageRequest pageRequestForList = PageRequest.of(page, 10);
        int start = (int) pageRequestForList.getOffset();
        int end = Math.min((start + pageRequestForList.getPageSize()), HistoryResList.size());
        Page<HistoryResDto> historyResDtoPage = new PageImpl<>(HistoryResList.subList(start, end),
            pageRequestForList, HistoryResList.size());

        return historyResDtoPage;
    }
}