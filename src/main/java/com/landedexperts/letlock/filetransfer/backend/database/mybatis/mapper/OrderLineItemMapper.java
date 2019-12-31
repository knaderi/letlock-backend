package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface OrderLineItemMapper {
	@Select(
		"SELECT"
			+ " _order_line_item_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM orders.add_order_line_item( #{ userId }, #{ orderId }, #{ productId }, #{ quantity } )"
	)
	IdVO addOrderLineItem(
		@Param("userId") int userId,
		@Param("orderId") int orderId,
		@Param("productId") int productId,
		@Param("quantity") short quantity
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM orders.update_order_line_item( #{ userId }, #{ orderLineItemId }, #{ quantity } )"
	)
	ErrorCodeMessageResponse updateOrderLineItem(
		@Param("userId") int userId,
		@Param("orderLineItemId") int orderDetailId,
		@Param("quantity") short quantity
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM orders.delete_order_line_item( #{ userId }, #{ orderLineItemId } )"
	)
	ErrorCodeMessageResponse deleteOrderLineItem(
		@Param("userId") int userId,
		@Param("orderLineItemId") int orderDetailId
	);
}
