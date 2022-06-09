package com.softCare.Linc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity @Getter @Setter
public class Circle {


    @Id
    @Column(name = "circle_id", nullable = false)
    @GeneratedValue
    private Long circleId;

    private String circleName;


}
