package com.example.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
