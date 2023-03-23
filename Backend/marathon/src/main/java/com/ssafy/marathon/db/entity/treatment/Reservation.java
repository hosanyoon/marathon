package com.ssafy.marathon.db.entity.treatment;

import com.ssafy.marathon.db.entity.user.Doctor;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue
    private Long seq;

    private LocalDate date;

    private String bitDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_seq")
    private Doctor doctor;

    public void updateBitDate(String bitDate) {
        this.bitDate = bitDate;
    }
}