package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdResult;

public interface OrderMapper {
	@Select(
		"SELECT"
			+ " _order_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_create( #{userId} )"
	)
	IdResult orderCreate(@Param("userId") int userId);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_change_status_initiated_to_cancelled( #{ userId }, #{ orderId } )"
	)
	ErrorCodeMessageResult changeStatusInitiatedToCancelled(@Param("userId") int userId, @Param("orderId") int orderId);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_change_status_cancelled_to_initiated( #{ userId }, #{ orderId } )"
	)
	ErrorCodeMessageResult changeStatusCancelledToInitiated(@Param("userId") int userId, @Param("orderId") int orderId);
}
