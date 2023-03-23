package com.ssafy.marathon.service.patient;

import com.ssafy.marathon.db.entity.treatment.Reservation;
import com.ssafy.marathon.db.entity.treatment.Treatment;
import com.ssafy.marathon.db.entity.user.Doctor;
import com.ssafy.marathon.db.repository.DoctorRepository;
import com.ssafy.marathon.db.repository.PatientRepository;
import com.ssafy.marathon.db.repository.ReservationRepository;
import com.ssafy.marathon.db.repository.TreatmentRepository;
import com.ssafy.marathon.dto.response.treatment.DayOfReservationResDto;
import com.ssafy.marathon.dto.response.treatment.DayOfTreatmentResDto;
import com.ssafy.marathon.dto.response.user.DoctorResDto;
import com.ssafy.marathon.util.MilliFunc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientTreatmentServiceImpl implements PatientTreatmentService {

    private final DoctorRepository doctorRepository;
    private final ReservationRepository reservationRepository;
    private final TreatmentRepository treatmentRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    @Override
    public List<DoctorResDto> getDoctorList() {
        List<Doctor> list = doctorRepository.findAll();
        List<DoctorResDto> resList = new ArrayList<>();

        for (Doctor doctor : list) {
            DoctorResDto doctorResDto = DoctorResDto.builder()
                .seq(doctor.getSeq())
                .name(doctor.getName())
                .introduce(doctor.getIntroduce())
                .img(doctor.getImg())
                .build();

            resList.add(doctorResDto);
        }

        return resList;
    }

    @Transactional
    @Override
    public List<DayOfReservationResDto> getDoctorReservationTime(Long doctorSeq) {

        if (reservationRepository.countByDateBetweenAndDoctor_Seq(MilliFunc.getStartDate(), MilliFunc.getEndDate(), doctorSeq) != 21) {
            Long milli = MilliFunc.startDayMilliSec();
            for (int i = 0; i < 21; i++) {
                LocalDate date = MilliFunc.makeDate(milli);
//            logger.info("현재" + i + "번째 반복중");
//            logger.info(reservationRepository.countReservationByDate(date) + "");
                if (0 == reservationRepository.countReservationByDateAndDoctor_Seq(date, doctorSeq)) {
                    Reservation rv = Reservation.builder()
                        .date(date)
                        .bitDate("00000000")
                        .doctor(doctorRepository.getBySeq(doctorSeq))
                        .build();
//                logger.info(rv.toString());
                    reservationRepository.save(rv);
                }
                milli += MilliFunc.DAYMILLIESEC;
            }
        }

        List<Reservation> list = reservationRepository.findAllByDateBetweenAndDoctor_Seq(MilliFunc.getStartDate(), MilliFunc.getEndDate(), doctorSeq);
        List<DayOfReservationResDto> resList = new ArrayList<>();
        for (Reservation reservation : list) {
            DayOfReservationResDto reservationResDto = DayOfReservationResDto.builder()
                .reservationSeq(reservation.getSeq())
                .localDate(reservation.getDate())
                .bitDate(reservation.getBitDate())
                .build();

            resList.add(reservationResDto);
        }

        return resList;
    }

    @Transactional
    @Override
    public Treatment makeTreatment(Long doctorSeq, Long patientSeq, List<DayOfReservationResDto> list) {

        List<Reservation> originList = reservationRepository.findAllByDateBetweenAndDoctor_Seq(MilliFunc.getStartDate(), MilliFunc.getEndDate(), doctorSeq);

        for (int i = 0; i < list.size(); i++) {
            String pBit = list.get(i).getBitDate();
            String dBit = originList.get(i).getBitDate();

            if (pBit.equals(dBit)) continue;

            int[] hour = {9, 10, 11, 13, 14, 15, 16, 17};

            for (int j = 0; j < 8; j++) {
                if (pBit.charAt(j) == dBit.charAt(j)) continue;

                Treatment treatment = Treatment.builder()
                    .date(list.get(i).getLocalDate())
                    .time(LocalTime.of(hour[j], 0))
                    .doctor(doctorRepository.findBySeq(doctorSeq))
                    .patient(patientRepository.findBySeq(patientSeq))
                    .build();

//                새 예약 저장
                treatmentRepository.save(treatment);

//                비트 데이터 변경
                originList.get(i).updateBitDate(list.get(i).getBitDate());
                reservationRepository.save(originList.get(i));
                return treatment;
//                브레이크 없애면 다중 예약 가능
//                break;
            }
//            브레이크 없애면 다중 예약 가능
//            break;
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DayOfTreatmentResDto> getCalendar(Long patientSeq) {
        List<Treatment> list = treatmentRepository.findAllByPatient_Seq(patientSeq);
        List<DayOfTreatmentResDto> resList = new ArrayList<>();


        for (Treatment treatment : list) {
            DayOfTreatmentResDto dayOfTreatmentResDto = DayOfTreatmentResDto.builder()
                .treatmentSeq(treatment.getSeq())
                .doctorName(treatment.getDoctor().getName())
                .dateTime(LocalDateTime.of(treatment.getDate(), treatment.getTime()))
                .doctorImg(treatment.getDoctor().getImg())
                .day(treatment.getDate().getDayOfWeek().toString())
                .build();

            resList.add(dayOfTreatmentResDto);
        }

        return resList;
    }
}