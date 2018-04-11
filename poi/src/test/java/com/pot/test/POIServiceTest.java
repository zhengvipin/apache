package com.pot.test;

import com.poi.domain.Car;
import com.poi.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;


public class POIServiceTest {
    private CarService carService;

    @Test
    public void demo(){
        File file = new File("F:\\a\\b\\c.txt");
        file.delete();
    }

    @Test
    public void findAll() {
        for (Car car : carService.findAll()) {
            System.out.println(car.getName() + " " + car.getPrice());
        }
    }

    @Before
    public void init() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        carService = (CarService) context.getBean("carService");
    }
}
