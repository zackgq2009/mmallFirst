package com.mmall.service.impl;

import com.mmall.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();

        //獲取擴展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
        String fileNewName = UUID.randomUUID().toString() + "." + fileExtensionName;

        logger.info("開始上傳文件，上傳文件的文件名：{}， 上傳的路徑：{}， 新文件名：{}", fileName, path, fileNewName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File uploadFile = new File(path, fileNewName);

        try {
            file.transferTo(uploadFile);
            //上傳文件成功

            //todo 上傳到FTP上

            //todo 上傳完ftp之後，把項目中upload目錄下的文件給刪除，避免文件夾中的文件越來越多



        } catch (IOException e) {
            logger.error("上傳文件有異常", e);
        }

        return  uploadFile.getName();
    }

}
