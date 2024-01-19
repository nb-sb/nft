package com.nft.common.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public class FileUtils {
    /**
     * 文件保存处理
     * @param file 上传文件
     * @return 文件保存路径
     * @throws Exception 上传异常
     */

    public static String saveFile(MultipartFile file) throws Exception {
        File directory = new File("");//参数为空
        String courseFile = directory.getCanonicalPath() ;
//        System.out.println(courseFile);
        String path = courseFile + "\\imgs\\";
        String md5HashCode = "";
        try {
            md5HashCode = FileHahUtil.md5HashCode(file.getInputStream());
//            System.err.println(md5HashCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            System.err.println(e);
            md5HashCode = String.valueOf(UUID.randomUUID());
        }

        if (file != null) {                    // 有文件上传
            if (file.getSize() > 0) {
                String fileName =  md5HashCode + "."
                        + file.getContentType().substring(
                        file.getContentType().lastIndexOf("/") + 1);    // 创建文件名称
                path = path + fileName; //文件路径
                File saveFile = new File(path) ;
                System.out.println(fileName);
                System.out.println(path);
                file.transferTo(saveFile);        // 文件保存
            }
        }
        return path ;
    }

}
