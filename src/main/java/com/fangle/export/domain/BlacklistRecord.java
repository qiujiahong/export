package com.fangle.export.domain;/*
 * @Author      : Nick
 * @Description :
 * @Date        : Create in 18:09 2018/6/9
 **/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "urpcs_blacklistrecord")
public class BlacklistRecord {
    @Id
    @GeneratedValue
    @Column(name="BlacklistID")
    private Integer blacklistID;

    @Column(name="License")
    private String license;

    @Column(name="AmountOwed")
    private Integer amountOwed;         //欠费总额

    @Column(name="RecoveredAmount")
    private Integer recoveredAmount;    //已经支付总额

    @Column(name="ArrearsCount")
    private Integer arrearsCount;       //逃欠费次数

    @Column(name="DateOfArrears")
    private Integer dateOfArrears;      //

    @Column(name="TotalSubCentre")
    private Integer totalSubCentre;

}

