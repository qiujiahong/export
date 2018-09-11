package com.fangle.export.controller;/*
 * @Author      : Nick
 * @Description :
 * @Date        : Create in 18:20 2018/6/9
 **/

import com.fangle.export.domain.BlacklistRecord;
import com.fangle.export.domain.EscapeRecord;
import com.fangle.export.repository.BlacklistRecordRepository;
import com.fangle.export.repository.EscapeRecordRepository;
import com.fangle.export.service.ExportService;
import com.fangle.export.service.FileService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.List;

@Controller
@Slf4j
public class MainController {

    @Autowired
    BlacklistRecordRepository blacklistRecordRepository;

    @Autowired
    EscapeRecordRepository escapeRecordRepository;

    @Autowired
    FileService fileService;

    @Autowired
    ExportService exportService;



    @GetMapping(path="/")
    public @ResponseBody String index(){

        log.info("get 1 /");
        return "index";
    }


    @GetMapping(path="/export")
    public @ResponseBody String export(){

        log.info("get /export");
        return exportService.exportData();
    }

    @GetMapping(path = "/test")
    public  @ResponseBody String test() {
        fileService.WriteStringToFile("hello");
        return "test";
    }


    @GetMapping(path = "/del")
    public  @ResponseBody String del() {
        fileService.delFile();
        return "test";
    }



}
