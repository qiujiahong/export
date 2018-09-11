package com.fangle.export.service;/*
 * @Author      : Nick
 * @Description :
 * @Date        : Create in 23:03 2018/6/9
 **/

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

@Service
public class FileService {

    public void WriteStringToFile(String filePath,String text) {
        try {
            if(filePath == null) {
                filePath ="output.csv";
            }
            File file = new File(filePath);
            PrintStream ps = new PrintStream(new FileOutputStream(file,true));
            //ps.println("http://www.jb51.net");// 往文件里写入字符串
            ps.append(text);// 在已有的基础上添加字符串
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {

        }
    }

    public void WriteStringToFile(String text) {
        WriteStringToFile(null,text);
    }


    public void delFile(String filePath){
        if(filePath == null) {
            filePath ="output.csv";
        }
        File file=new File(filePath);
        if(file.exists()&&file.isFile())
            file.delete();
    }
    public void delFile(){
        delFile(null);
    }

}
