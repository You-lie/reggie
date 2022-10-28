package com.example.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersDetailMapper extends BaseMapper<OrderDetail> {
}
