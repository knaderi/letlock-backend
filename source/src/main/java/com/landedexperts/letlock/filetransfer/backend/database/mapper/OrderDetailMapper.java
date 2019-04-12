package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdResult;

public interface OrderDetailMapper {
	@Select(
		"SELECT"
			+ " _order_detail_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_detail_add( #{ userId }, #{ orderId }, #{ productId }, #{ quantity } )"
	)
	IdResult orderDetailAdd(
		@Param("userId") int userId,
		@Param("orderId") int orderId,
		@Param("productId") int productId,
		@Param("quantity") short quantity
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_detail_update( #{ userId }, #{ orderDetailId }, #{ quantity } )"
	)
	ErrorCodeMessageResult orderDetailUpdate(
		@Param("userId") int userId,
		@Param("orderDetailId") int orderDetailId,
		@Param("quantity") short quantity
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_detail_delete( #{ userId }, #{ orderDetailId } )"
	)
	ErrorCodeMessageResult orderDetailDelete(
		@Param("userId") int userId,
		@Param("orderDetailId") int orderDetailId
	);
}
