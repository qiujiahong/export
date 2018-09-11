package com.fangle.export.repository;/*
 * @Author      : Nick
 * @Description :
 * @Date        : Create in 18:19 2018/6/9
 **/

import com.fangle.export.domain.BlacklistRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BlacklistRecordRepository extends CrudRepository<BlacklistRecord,Integer>{
    BlacklistRecord findByBlacklistID(Integer id);

    List<BlacklistRecord> findByTotalSubCentre(Integer id);
}
