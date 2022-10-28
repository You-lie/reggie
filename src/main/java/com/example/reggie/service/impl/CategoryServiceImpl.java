package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.common.CustomException;
import com.example.reggie.dao.CategoryMapper;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishService;
import com.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 删除菜品
     * @param ids
     */
    @Override
    public void remove(Long ids) {
    //查询当前是否关联了一个菜品，是的话抛出一个异常
        LambdaQueryWrapper<Dish> dishlambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishlambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        long count = dishService.count(dishlambdaQueryWrapper);
        if(count>0){
            throw new CustomException("当前分类关联了菜品不能删除");
        }

        //查询当前是否关联了一个套餐，是的话抛出一个异常
        LambdaQueryWrapper<Setmeal> setmeallambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmeallambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        long count2 = setmealService.count(setmeallambdaQueryWrapper);
        if(count2>0){
            throw new CustomException("当前分类关联了套餐不能删除");
        }
    //正常删除分类
        super.removeById(ids);
    }
}
