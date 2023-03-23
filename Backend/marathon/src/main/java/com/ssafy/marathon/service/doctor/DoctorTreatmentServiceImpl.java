package com.ssafy.marathon.service.doctor;

import com.ssafy.marathon.db.entity.communication.Alarm;
import com.ssafy.marathon.db.entity.treatment.Reservation;
import com.ssafy.marathon.db.entity.treatment.Treatment;
import com.ssafy.marathon.db.entity.user.User;
import com.ssafy.marathon.db.repository.*;
import com.ssafy.marathon.dto.request.treatment.ReservationReqDto;
import com.ssafy.marathon.dto.response.treatment.DayOfReservationResDto;
import com.ssafy.marathon.dto.response.treatment.DayOfTreatmentResDto;
import com.ssafy.marathon.dto.response.treatment.ReservationResDto;
import com.ssafy.marathon.util.MilliFunc;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorTreatmentServiceImpl implements DoctorTreatmentService {

    private final Logger logger = LoggerFactory.getLogger(DoctorTreatmentServiceImpl.class);
    private final ReservationRepository reservationRepository;
    private final DoctorRepository doctorRepository;
    private final TreatmentRepository treatmentRepository;
    private final CommunicationRepository communicationRepository;
    private final UserRepository userRepository;


    // 해당 의사의 예약이 몇개인지 반환한다.
    @Transactional(readOnly = true)
    @Override
    public int countRaws(Long doctorSeq) { // 매개변수로 의사 넣을것
        LocalDate startDate = MilliFunc.getStartDate();
        LocalDate endDate = MilliFunc.getEndDate();

        return reservationRepository.countByDateBetweenAndDoctor_Seq(startDate, endDate, doctorSeq);
    }

    //    예약이 없는 경우 의사의 예약을 채운다
    @Transactional
    @Override
    public void makeEmptyReservation(Long doctorSeq) { // 매개변수로 의사 넣을것
        Long milli = MilliFunc.startDayMilliSec();
        for (int i = 0; i < 21; i++) {
            LocalDate date = MilliFunc.makeDate(milli);
//            logger.info("현재" + i + "번째 반복중");
//            logger.info(reservationRepository.countReservationByDate(date) + "");
            if (0 == reservationRepository.countReservationByDateAndDoctor_Seq(date, doctorSeq)) {
                Reservation rv = Reservation.builder().date(date).bitDate("00000000")
//                    doctor받아와야함
                    .doctor(doctorRepository.findBySeq(doctorSeq)).build();
//                logger.info(rv.toString());
                reservationRepository.save(rv);

            }
            milli += MilliFunc.DAYMILLIESEC;
        }
    }

    //    각 예약의 정보를 리스트로 만들어 반환한다.
    @Transactional(readOnly = true)
    @Override
    public List<DayOfReservationResDto> makeReservationList(Long doctorSeq) { // 의사로 바꿀것? seq여도 되는가?

        List<DayOfReservationResDto> list = new ArrayList<DayOfReservationResDto>();
        Long milli = MilliFunc.startDayMilliSec();
        for (int i = 0; i < 21; i++) {
            LocalDate ld = MilliFunc.makeDate(milli);

//            logger.info(ld + ", " + doctorSeq + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            Reservation reservation = reservationRepository.findByDateAndDoctor_Seq(ld, doctorSeq);

            DayOfReservationResDto reservationResDto = DayOfReservationResDto.builder()
                .reservationSeq(reservation.getSeq())
                .localDate(reservation.getDate())
                .bitDate(reservation.getBitDate())
                .build();
            list.add(reservationResDto);
            milli += MilliFunc.DAYMILLIESEC;
        }

        return list;
    }

    @Transactional
    @Override
    public ReservationResDto makeReservationResDto(Long doctorSeq) {

//        logger.info("test check 1");
        List<DayOfReservationResDto> list = makeReservationList(doctorSeq);

//        logger.info("test check 2");

        ReservationResDto reservationResDto = ReservationResDto.builder()
            .firstDateInfo(MilliFunc.startDayMilliSec())
            .list(list)
            .build();
//        logger.info("test check 3");
        return reservationResDto;
    }

    @Transactional
    @Override
    public void updateReservation(List<ReservationReqDto> list) {
        for (ReservationReqDto dto : list) {
            Reservation reservation = reservationRepository.findBySeq(dto.getReservationSeq());
//            bitDate가 같으면 continue
            if (reservation.getBitDate().equals(dto.getBitDate())) {
                continue;
            }
            logger.info("[UPDATE] 의사의 예약 가능 시간 변경 date:{}, before:{}, After:{}", dto.getLocalDate(),
                reservation.getBitDate(), dto.getBitDate());
            reservation.updateBitDate(dto.getBitDate());
            reservationRepository.save(reservation);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<DayOfTreatmentResDto> getTreatments(Long doctorSeq) {
        List<Treatment> list = treatmentRepository.findByDateBetweenAndDoctor_Seq(
            MilliFunc.getStartDate(), MilliFunc.getEndDate(), doctorSeq);
        List<DayOfTreatmentResDto> dtoList = new ArrayList<>();

        for (Treatment treatment : list) {
            LocalDateTime dateTime = LocalDateTime.of(treatment.getDate(),
                treatment.getTime()); // Treatment바꾸기

            DayOfTreatmentResDto dayOfTreatmentResDto = DayOfTreatmentResDto.builder()
                .treatmentSeq(treatment.getSeq())
                .patientName(treatment.getPatient().getName())
                .dateTime(dateTime)
                .day(dateTime.getDayOfWeek().toString())
                .patientImg(treatment.getPatient().getImg())
                .build();

            dtoList.add(dayOfTreatmentResDto);
        }

        return dtoList;
    }

    @Transactional
    @Override
    public void cancelTreatment(Long treatmentSeq) {
        Optional<Treatment> findTreatment = treatmentRepository.findById(treatmentSeq);
        Treatment treatment = findTreatment.orElseThrow();

        Reservation reservation = reservationRepository.findByDateAndDoctor_Seq(treatment.getDate(), treatment.getDoctor().getSeq());

        StringBuilder sb = new StringBuilder();

        String oldBitdate = reservation.getBitDate();
        String[] arr = {"09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00", "17:00"};

        for (int i = 0; i < oldBitdate.length(); i++) {

            System.out.println(treatment.getTime());
            if (arr[i].equals(treatment.getTime().toString())) sb.append('1');
            else sb.append(oldBitdate.charAt(i));
        }
        reservation.updateBitDate(sb.toString());
        reservationRepository.save(reservation);

        treatmentRepository.delete(treatment);
    }

    @Transactional
    @Override
    public boolean makeAlarm(Long treatmentSeq, String sessionId, Long doctorSeq) {
        User receiver = userRepository.findBySeq(treatmentRepository.findBySeq(treatmentSeq).getPatient().getSeq());
        User sender = userRepository.findBySeq(doctorSeq);

        Alarm alarm = Alarm.builder()
            .link(sessionId)
            .receiver(receiver)
            .sender(sender)
            .checked(false)
            .dateTime(LocalDateTime.now())
            .build();
        return communicationRepository.save(alarm) != null;
    }

    @Transactional
    @Override
    public void deleteTreatment(Long treatmentSeq) {
        Optional<Treatment> findTreatment = treatmentRepository.findById(treatmentSeq);
        Treatment treatment = findTreatment.orElseThrow();
        treatmentRepository.delete(treatment);
    }


}