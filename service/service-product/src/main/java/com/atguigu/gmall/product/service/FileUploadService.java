package com.atguigu.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Author：张世平
 * Date：2022/8/28 12:50
 */
public interface FileUploadService {
    /**
     * 文件上传
     * @param file
     * @return
     * @throws Exception
     */
    String upload(MultipartFile file) throws Exception;
}
