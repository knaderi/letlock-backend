package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface OrderDetailMapper {
	@Select(
		"SELECT"
			+ " _order_detail_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.add_order_detail( #{ userId }, #{ orderId }, #{ productId }, #{ quantity } )"
	)
	IdVO addOrderDetail(
		@Param("userId") int userId,
		@Param("orderId") int orderId,
		@Param("productId") int productId,
		@Param("quantity") short quantity
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.update_order_detail( #{ userId }, #{ orderDetailId }, #{ quantity } )"
	)
	ErrorCodeMessageResponse updateOrderDetail(
		@Param("userId") int userId,
		@Param("orderDetailId") int orderDetailId,
		@Param("quantity") short quantity
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.delete_order_detail( #{ userId }, #{ orderDetailId } )"
	)
	ErrorCodeMessageResponse deleteOrderDetail(
		@Param("userId") int userId,
		@Param("orderDetailId") int orderDetailId
	);
}
