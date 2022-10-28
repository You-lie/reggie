package com.example.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
