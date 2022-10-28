package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.common.CustomException;
import com.example.reggie.dao.DishMapper;
import com.example.reggie.dao.SetmealMapper;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.entity.SetmealDish;
import com.example.reggie.service.SetmealDishService;
import com.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWith(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);

    }
    @Transactional
    @Override
    public void deleteWithDish(List<Long> ids) {
        //查询套餐可否删除
        //select count （*）form setmeal where id in（1,2,3） and status=1；
        LambdaQueryWrapper <Setmeal> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);//1代表正在出售 出售不能删除
        int count = (int) this.count(queryWrapper);
        if (count>0){
            throw new CustomException("有商品正在出售不能删除");
        }
        //可以删除，先删除setmeal
        this.removeByIds(ids);
        //然后删除，setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapperDish=new LambdaQueryWrapper<>();
        queryWrapperDish.in(SetmealDish::getDishId,ids);
        setmealDishService.remove(queryWrapperDish);

    }
}
