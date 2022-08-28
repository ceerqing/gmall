package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.config.minin.MinioProperties;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * Author：张世平
 * Date：2022/8/28 12:50
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    MinioClient client;
    @Autowired
    MinioProperties minioProperties;
    @Override
    public String upload(MultipartFile file) throws Exception {
        //设置文件夹名
        String dir = DateUtil.formatDate(new Date()).replace("-", "/");//2022/8/24 目录名
        String fileName = UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        PutObjectOptions putObjectOptions = new PutObjectOptions(file.getSize(),-1L);
        putObjectOptions.setContentType(file.getContentType());

        client.putObject(minioProperties.getBucketName(),dir+"/"+fileName,inputStream,putObjectOptions);
        String url=minioProperties.getEndpoint()+"/"+minioProperties.getBucketName()+"/"+dir+"/"+fileName;
        return url;
    }
}
