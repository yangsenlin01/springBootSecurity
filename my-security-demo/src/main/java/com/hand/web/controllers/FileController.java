package com.hand.web.controllers;

import com.hand.dto.FileInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-3-29
 * @description
 */

@RestController
@RequestMapping(value = "/file")
public class FileController {

    String filePath = "D:\\myResource\\springSecurityDemo\\my-security-demo\\src\\main\\java\\com\\com.hand\\web\\controllers";

    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());

        File loadFile = new File(filePath, System.currentTimeMillis() + ".txt");

        file.transferTo(loadFile);

        return new FileInfo(loadFile.getAbsolutePath());
    }

    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 在try后面加个()，里面的流会在try/catch执行完后自动关闭，就不用使用finally来关闭了。这是jdk7带进的语法
        try (InputStream inputStream = new FileInputStream(new File(filePath, id + ".txt"));
             OutputStream outputStream = response.getOutputStream()) {

            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");

            // org.apache.commons.io.IOUtils
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }
    }
}
