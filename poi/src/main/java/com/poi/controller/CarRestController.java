package com.poi.controller;

import com.poi.domain.Car;
import com.poi.service.CarService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 只提供下载
 *
 * @author Zheng
 */
@RestController
@RequestMapping("/api")
public class CarRestController {
    @Resource(name = "carService")
    private CarService carService;

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        List<Car> cars = carService.findAll();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<?> download() throws IOException {

        List<Car> cars = carService.findAll();
        // 生成excel表格
        HSSFWorkbook workbook = carService.exportExcel(cars);
        // 将excel写入输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        // 设置输出格式
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("小汽车.xls".getBytes("UTF-8"), "iso-8859-1");
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.CREATED);
    }
}
