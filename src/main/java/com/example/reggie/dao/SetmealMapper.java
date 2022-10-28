package com.example.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie.entity.Employee;
import com.example.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
