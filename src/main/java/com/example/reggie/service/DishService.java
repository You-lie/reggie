package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.DishFlavor;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    //根据id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新信息以及口味
    public void updateWithFlavor(DishDto dishDto);

}
