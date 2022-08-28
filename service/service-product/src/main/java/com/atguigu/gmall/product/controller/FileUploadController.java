package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author：张世平
 *
 * Date：2022/8/28 12:41
 */
//http://192.168.200.1/admin/product/fileUpload@
@RestController
@RequestMapping("/admin/product")
@Api(tags = "文件上传相关api")
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    @ApiOperation("文件上传")
    @PostMapping("/fileUpload")
    public Result upload(@RequestPart("file")
                          @ApiParam("待上传的文件")
                          MultipartFile file) throws Exception {

      String url =  fileUploadService.upload(file);
      return Result.ok(url);
    }
}
