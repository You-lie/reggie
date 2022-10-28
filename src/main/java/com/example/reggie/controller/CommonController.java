package com.example.reggie.controller;

import com.example.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/common")
@Slf4j
@RestController
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //对上传的临时文件进行转存
        log.info("上传文件： {}",file.toString());
        //获取原始文件后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //把上传的文件名改为uuid防止重复
        String filename = UUID.randomUUID().toString() + suffix;
        //判断路径是否存在，不存在就新建
        File f = new File(basePath);
        if (!f.exists()){
            f.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(filename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name , HttpServletResponse response){
        try {
            //输入流读取
            FileInputStream fileInputStream=new FileInputStream(basePath+name);
            //输出流
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len=0;
            byte [] bytes=new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
