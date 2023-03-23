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
@Table(name = "patient")
public class Patient extends User {

    private String mainPhone;
    private String mainRelationship;
    private String subPhone;
    private String subRelationship;

    public void updatePatientInfo(String mainPhone, String mainRelationship, String subPhone, String subRelationship) {
        this.mainPhone = mainPhone;
        this.mainRelationship = mainRelationship;
        this.subPhone = subPhone;
        this.subRelationship = subRelationship;
    }
}