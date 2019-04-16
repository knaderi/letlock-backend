package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;

public interface OrderDetailMapper {
	@Select(
		"SELECT"
			+ " _order_detail_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.order_detail_add( #{ userId }, #{ orderId }, #{ productId }, #{ quantity } )"
	)
	IdVO orderDetailAdd(
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
	ErrorCodeMessageVO orderDetailUpdate(
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
	ErrorCodeMessageVO orderDetailDelete(
		@Param("userId") int userId,
		@Param("orderDetailId") int orderDetailId
	);
}
