package com.example.reggie.dto;

import com.example.reggie.entity.DishFlavor;
import com.example.reggie.entity.Dish;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
