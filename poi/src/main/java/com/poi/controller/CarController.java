package com.poi.controller;

import com.poi.domain.Car;
import com.poi.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 这个是先在磁盘生成excel表格，再提供下载
 *
 * @author Zheng
 */
@Controller
public class CarController {
    @Resource(name = "carService")
    private CarService carService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        List<Car> cars = carService.findAll();
        return new ModelAndView("index", "cars", cars);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String download(HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        // 获得要导出的数据集合
        List<Car> cars = carService.findAll();
        // 生成xml文件
        carService.download(cars, request, response);
        return "index";
    }
}
