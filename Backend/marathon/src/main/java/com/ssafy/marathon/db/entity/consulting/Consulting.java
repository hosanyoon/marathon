package com.ssafy.marathon.db.entity.consulting;

import java.time.LocalDate;
import javax.persistence.Column;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.lang.Nullable;

@Getter
@Entity
@Table(name = "consulting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Consulting {

    @Id
    @GeneratedValue
    private Long consultingSeq;
    private String name;
    private boolean sex;
    private LocalDate birthDate;
    private String email;
    private String phone1;
    private String phone2;
    @Column(name = "phone2_relationship")
    private String phone2Relationship;
    private String phone3;
    @Column(name = "phone3_relationship")
    private String phone3Relationship;
    @Nullable
    private LocalDate sickDate;
    private String description;
    private LocalDate hopeDate;
    private boolean checked;

    public void changeChecked() {
        this.checked = !this.checked;
    }
}
