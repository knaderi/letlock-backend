package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.OrderResult;

public interface OrderMapper {
	@Select("SELECT _order_id AS orderId, _error_code AS errorCode, _error_message AS errorMessage FROM payment.order_create( #{userId} )")
	OrderResult orderCreate(@Param("userId") Integer userId);
}
