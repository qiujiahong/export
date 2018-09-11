package com.fangle.export.repository;/*
 * @Author      : Nick
 * @Description :
 * @Date        : Create in 19:07 2018/6/9
 **/

import com.fangle.export.domain.BlacklistRecord;
import com.fangle.export.domain.EscapeRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EscapeRecordRepository extends CrudRepository<EscapeRecord,Integer>{
    List<EscapeRecord> findByLicense(String License);
    List<EscapeRecord> findByLicenseAndTotalSubCentre(String License,Integer TotalSubCentre);
}
