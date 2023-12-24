//package com.nft.common.configuration;
//
//
//import org.apache.log4j.PropertyConfigurator;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//
//@Configuration
//public class initLogRecord {
//
//    @PostConstruct
//    public static void initLog() {
//        FileInputStream fileInputStream = null;
//        try {
//            Properties properties = new Properties();
//            fileInputStream = new FileInputStream("app/src/main/resources/log4j.properties");
//            properties.load(fileInputStream);
//            PropertyConfigurator.configure(properties);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fileInputStream != null) {
//                try {
//                    fileInputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
