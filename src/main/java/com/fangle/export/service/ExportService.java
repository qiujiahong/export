package com.fangle.export.service;/*
 * @Author      : Nick
 * @Description :
 * @Date        : Create in 23:15 2018/6/9
 **/

import com.fangle.export.domain.BlacklistRecord;
import com.fangle.export.domain.EscapeRecord;
import com.fangle.export.helper.TimeUtils;
import com.fangle.export.repository.BlacklistRecordRepository;
import com.fangle.export.repository.EscapeRecordRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExportService {
    @Autowired
    BlacklistRecordRepository blacklistRecordRepository;

    @Autowired
    EscapeRecordRepository escapeRecordRepository;

    @Autowired
    FileService fileService;

    @Value("${config.export.max}")
    private Integer max;

    private  Integer unpaid=0;
    private  Integer now = 0;
    private Integer percentage = 0 ;
    private Integer startTime= TimeUtils.getTimeStamp("2016-01-01 00:00:00");
    private Integer endTime=TimeUtils.getTimeStamp("2016-12-31 23:59:59");
    private Integer centerId = 3;//国通（总分中心标识为1）国合（总分中心标识为3）


    /**
     * 处理导出数据
     * @return
     */
    public String exportData(){
        if(percentage.equals(0)  || percentage.equals(100)){
            log.info("start to get black list.");
            List<BlacklistRecord> blacklistRecords = blacklistRecordRepository.findByTotalSubCentre(centerId);
            log.info("get black list success."+blacklistRecords.size()+" records.");

            fileService.delFile();
            log.info("Delete file");
            this.unpaid = 0;
            this.now= 0;

            fileService.WriteStringToFile("Licence,Should pay,Paid,Start,End,Spot\r\n");
            for (BlacklistRecord b:blacklistRecords
                    ) {
                if(b.getAmountOwed()<=max){
                    List<EscapeRecord> records =  escapeRecordRepository.findByLicenseAndTotalSubCentre(b.getLicense(),centerId);
                    handeList(b,records);
                }
                else{
//                    log.error(String.format("%s,bigger than %d",b.getLicense(),max));
                }
                this.now ++ ;
                showPercentage(blacklistRecords.size(),this.now);
            }
            log.info(String.format("percentage=%d,record=%d,unpaid=%d",this.percentage,this.now,this.unpaid));
            return "export";
        }else {
            return "Please wait for some time.";
        }


    }


    private int showPercentage(Integer total,int now){
        Integer percentage =  (int) (((float)(now)/(float)total)*100);

        if(this.percentage!= percentage){
            this.percentage = percentage;
            log.info(String.format("percentage=%d,unpaid=%d",this.percentage,this.unpaid));
        }
        return  percentage;
    }


    private void handeList(BlacklistRecord blacklistRecord, List<EscapeRecord> escapeRecords){
        if(blacklistRecord == null ||
                escapeRecords == null||
                escapeRecords.size() == 0 ){
            log.error(String.format("Error black list record:%d,%s,%d",blacklistRecord.getBlacklistID(),blacklistRecord.getLicense(),escapeRecords.size()));
            return ;
        }

        escapeRecords = clearRepeatAndDone(escapeRecords);// 删除掉重复的记录,以及已经追缴了的记录
        escapeRecords = clearTimeIllegal(escapeRecords, startTime, endTime);//删除掉时间非法的记录


        Integer unpaid = getUnPay(escapeRecords);
        Integer shouldPay = getShouldPay(escapeRecords);
        this.unpaid += unpaid;

        if((blacklistRecord.getArrearsCount().equals(escapeRecords.size()))&&
                (unpaid.equals(blacklistRecord.getAmountOwed()-blacklistRecord.getRecoveredAmount())) ){ //如果记录匹配，则直接存储
            saveRecord(escapeRecords);
            return;
        }
        escapeRecords = clearRepeatAndDone(blacklistRecord,escapeRecords);//删除掉疑似支付完的记录
        unpaid = getUnPay(escapeRecords);
        if((blacklistRecord.getArrearsCount().equals(escapeRecords.size()))&&
                (unpaid.equals(blacklistRecord.getAmountOwed()-blacklistRecord.getRecoveredAmount())) ){ //如果记录匹配，则直接存储
            saveRecord(escapeRecords);
            return;
        }

        if(shouldPay.equals(blacklistRecord.getAmountOwed())){//总额相等
            saveRecord(escapeRecords);
            return;
        }

        if(isInIgnoreList(blacklistRecord.getLicense())){
            saveRecord(escapeRecords);
            return;
        }


        boolean bool1 = (blacklistRecord.getArrearsCount().equals(escapeRecords.size()));
        boolean bool2 = (unpaid.equals(blacklistRecord.getAmountOwed() - blacklistRecord.getRecoveredAmount()));


//        log.info(new Gson().toJson(blacklistRecord));
//        log.info(new Gson().toJson(escapeRecords));
        saveRecord(escapeRecords);

    }

    List<EscapeRecord> clearRepeatAndDone(BlacklistRecord blacklistRecord,List<EscapeRecord> records){

        for(int i = 0 ;i<records.size();i++) {
            EscapeRecord record = records.get(i);

            if (blacklistRecord.getRecoveredAmount()>300&&
                    blacklistRecord.getRecoveredAmount().equals(record.getReceivablesMoney())){
                records.remove(i);
                blacklistRecord.setArrearsCount(blacklistRecord.getArrearsCount()-1);
                i=0;
            }
        }
        return records;
    }

    private  void saveRecord(List<EscapeRecord> records){
        for (EscapeRecord escapeRecord: records
                ) {
            String str = escapeRecord.toString()+"\r\n";
//            log.debug("save:"+str);
            fileService.WriteStringToFile(str);
        }
    }

    List<String> ignoreList = new ArrayList<String>(){
        {
            add("A661ST");//差2yuan
            add("A121CH");//差2yuan
        }
    };

    private boolean isInIgnoreList(String plateNumber) {

        for (String p : ignoreList
                ) {
            if (plateNumber.contains(p)) {
                return  true;
            }
        }
        return false;
    }

    private Integer getUnPay(List<EscapeRecord> records){

        Integer shouldPay = 0;
        Integer paid = 0;
        for (EscapeRecord record:records
                ) {
            shouldPay+= record.getReceivablesMoney();
            paid+= record.getPaidInMoney();
        }
        return shouldPay - paid;
    }
    private Integer getShouldPay(List<EscapeRecord> records){

        Integer shouldPay = 0;
        for (EscapeRecord record:records
                ) {
            shouldPay+= record.getReceivablesMoney();
        }
        return shouldPay;
    }
    List<EscapeRecord> clearTimeIllegal(List<EscapeRecord> records,int startTime,int endTime){

        if(startTime ==0||
                endTime == 0)
            return records;

        int i = 0;
        do{
            int start = records.get(i).getStartTimeID().intValue();
            int end = records.get(i).getStartTimeID().intValue();
            if(start < startTime ||
                    end > endTime) {
                records.remove(i);
                i=0;
                continue;
            }
            i++;
        }while (i<records.size());

        return records;
    }

    /**
     * 删除掉重复的记录,以及已经追缴了的记录
     * @param records
     * @return
     */
    List<EscapeRecord> clearRepeatAndDone(List<EscapeRecord> records){

        int i=0;
        do{
            if(records.get(i).getOperation() == 1 ){
                records.remove(i);
                i=0;
                continue;
            }
            i++;
        }while (i<records.size());


        if(records.size()>=2){
            for(i = 0 ;i<(records.size()-1)&&records.size()>=2;i++){
                if(records.get(i).getStartTimeID() == records.get(i+1).getStartTimeID() ){
                    records.remove(i);
                    i=0;
                    continue;
                }
            }
        }
        return records;
    }
}
