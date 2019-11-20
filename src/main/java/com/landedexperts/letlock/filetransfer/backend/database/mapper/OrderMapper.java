package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.response.ErrorCodeMessageResponse;

public interface OrderMapper {
	@Select(
		"SELECT"
			+ " _order_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_create( #{userId} )"
	)
	IdVO orderCreate(@Param("userId") int userId);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_change_status_initiated_to_cancelled( #{ userId }, #{ orderId } )"
	)
	ErrorCodeMessageResponse changeStatusInitiatedToCancelled(@Param("userId") int userId, @Param("orderId") int orderId);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_change_status_cancelled_to_initiated( #{ userId }, #{ orderId } )"
	)
	ErrorCodeMessageResponse changeStatusCancelledToInitiated(@Param("userId") int userId, @Param("orderId") int orderId);
}
