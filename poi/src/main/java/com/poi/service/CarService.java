package com.poi.service;

import com.poi.domain.Car;
import com.poi.mapper.CarMapper;
import com.poi.util.ExcelUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    @Resource(name = "carMapper")
    private CarMapper carMapper;

    public List<Car> findAll() {
        return carMapper.findAll();
    }

    public void download(List<Car> cars, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] rowsName = new String[]{"序号", "车名", "价格", "生产日期", "图片路径"};
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            Object[] objects = new Object[rowsName.length];
            Car car = cars.get(i);
            objects[0] = i + 1;
            objects[1] = car.getName();
            objects[2] = car.getPrice();
            objects[3] = car.getCreateDate();
            objects[4] = car.getImg();
            dataList.add(objects);
        }
        ExcelUtils.exportExcel("小汽车",
                                        rowsName,
                                        dataList,
                                        "yyyy-MM-dd",
                                        request,
                                        response);
    }
}
