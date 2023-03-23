package com.ssafy.marathon.db.entity.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity
@Table(name = "doctor")
public class Doctor extends User {

    private String license;

    private String degree;

    private String introduce;

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }
}

