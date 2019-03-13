package com.xuyun.platform.modules.ag.http;


import com.xuyun.platform.dsfcenterdata.response.RRException;
import com.xuyun.platform.dsfcenterdata.utils.DateUtil;
import com.xuyun.platform.modules.common.constants.AgConstants;
import com.xuyun.platform.modules.common.dto.ag.AgBetRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.jumpmind.symmetric.csv.CsvReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AgFtpNetWork {


    @Autowired
    FTPClient ftp;
    /**
     * Description: 从FTP服务器下载文件
     *
     * @param model FTP服务器上的参数
     * @return
     */
    public Map<String, String> downFile(AgBetRequest model) {
        Map<String, String> map = new ConcurrentHashMap<>();
        try {
            int reply;
            ftp.connect(model.getUrl());
            //采用默认端口，使用ftp.connect(url)的方式直接连接FTP服务器
            //登录
            ftp.login(model.getUsername(), model.getPassword());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return map;
            }
            //转移到FTP服务器目录
            ftp.changeWorkingDirectory(model.getRemotePath());
            List<String> allDirectoryNames = this.allDirectoryNames(ftp, model.getLastDownloadFileName());
            allDirectoryNames.forEach( it ->{
                try {
                    ftp.changeWorkingDirectory(model.getRemotePath()+it+"/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                map.putAll(download(model, ftp, map));
            });
        } catch (IOException e) {
            log.error("Ag 数据获取失败 ftp 异常", e);
            throw new RRException("Ag 数据获取失败 ftp 异常");
        }
        return map;
    }

    private Map<String, String> download(AgBetRequest model, FTPClient ftp, Map<String, String> map) {
        try {
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile file : fs) {
                if (file.isFile()) {
                    String ftpFileName = file.getName();
                    long mintus = DateUtil.parse(ftpFileName,DateUtil.FORMAT_12_DATE_TIME_2).getTime();
                    if(mintus >= model.getLastDownloadFileName()){
                        InputStream is = ftp.retrieveFileStream(ftpFileName);
                        ArrayList<String> list = csv(is);
                        if (is != null) {
                            ftp.completePendingCommand();
                            is.close();
                        }
                        if (!list.isEmpty()) {
                            map.put(ftpFileName, list.toString());
                            if(map.size() >= AgConstants.FTP_READ_SIZE){
                                return map;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Ag 数据获取失败 ftp 异常", e);
            throw new RRException("Ag 数据获取失败 ftp 异常");
        }
        log.info("AGIN请求参数 :" + model.toString());
        log.info("AGIN返回数据 :" + map.toString());
        return map;
    }

    private List<String> allDirectoryNames(FTPClient ftp, Long lastFileDirector) {
        String day = DateUtil.getDate(DateUtil.FORMAT_8_DATE,new Date(lastFileDirector));
        long lastTimestamp = DateUtil.parse(day, DateUtil.FORMAT_8_DATE).getTime();
        List<String> DirectoryNames = new LinkedList<>();
        try {
            FTPFile[] fileNames = ftp.listFiles();
            for (FTPFile file : fileNames) {
                if (!file.isFile()) {
                    long ftpfile = DateUtil.parse(file.getName(), DateUtil.FORMAT_8_DATE).getTime();
                    if (ftpfile >= lastTimestamp) {
                        DirectoryNames.add(file.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DirectoryNames;
    }

    /**
     * <b>将一个IO流解析，转化数组形式的集合<b>
     *
     * @param in 文件inputStream流
     */
    private static ArrayList<String> csv(InputStream in) {
        ArrayList<String> csvList = new ArrayList<String>();
        if (null != in) {
            CsvReader reader = new CsvReader(in, '>', Charset.forName("UTF-8"));
            try {
                while (reader.readRecord()) {
                    //获取的为每一行的信息
                    csvList.add(reader.getRawRecord().replace("<row ", "").replace("/>", ""));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        }
        return csvList;
    }

}
