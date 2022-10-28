package com.example.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
