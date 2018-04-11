package com.poi.mapper;

import com.poi.domain.Car;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarMapper {
    @Select("select * from car")
    List<Car> findAll();
}
