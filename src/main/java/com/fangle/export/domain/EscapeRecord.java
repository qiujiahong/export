package com.fangle.export.domain;/*
 * @Author      : Nick
 * @Description :
 * @Date        : Create in 18:52 2018/6/9
 **/

import lombok.Data;
import org.omg.PortableInterceptor.INACTIVE;

import javax.persistence.*;

@Entity
@Data
@Table(name = "urpcs_evasion_arrears")
public class EscapeRecord {

    @Id
    @GeneratedValue
    @Column(name = "EvasionID")
    private Integer evasionID;

    @Column(name = "ArtificialID")
    private Integer artificialID;


    @Column(name = "PersonnelID")
    private Integer personnelID;

    @Column(name = "CodeID")
    private Integer codeID;

    @Column(name = "PositionID")
    private Integer positionID;

    @Column(name = "ReceivablesMoney")
    private Integer receivablesMoney;

    @Column(name = "PaidInMoney")
    private Integer paidInMoney;

    @Column(name = "ParkingDuration")
    private Integer parkingDuration;

    @Column(name = "EndTimeID")
    private Integer endTimeID;

    @Column(name = "Status")
    private Integer status;     //'停车状态：0逃费  1欠费',

    @Column(name = "Operation")
    private Integer operation;  //'处理方式0 确认   1 追缴',

    @Column(name = "StartTimeID")
    private Integer startTimeID;

    @Column(name = "TotalSubCentre")
    private Integer totalSubCentre;

    @Column(name = "StartTimeNumber")
    private String startTimeNumber;

    @Column(name = "EndTimeNumber")
    private String endTimeNumber;

    @Column(name = "ProcessingTime")
    private String processingTime;

    @Column(name = "Picture")
    private String picture;

    @Column(name = "PositionNumber")
    private String positionNumber;

    @Column(name = "PersonnelNumber")
    private String personnelNumber;

    @Column(name = "PersonnelName")
    private String personnelName;

    @Column(name = "License")
    private String license;

    @Column(name = "ParkingNum")
    private String parkingNum;


    @Override
    public String toString() {
//        return "{" +
//                "license:'" + license + '\'' +
//                ", receivablesMoney:" + receivablesMoney +
//                ", paidInMoney:" + paidInMoney +
//                ", endTimeID:" + endTimeID +
//                ", startTimeID:" + startTimeID +
//                ", parkingNum:'" + parkingNum + '\'' +
//                '}';
        return license+"  ,  "+receivablesMoney+" , "+paidInMoney+" , "+startTimeID+" , "+endTimeID+" , "+parkingNum;
    }
}
