package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface OrderLineItemMapper {
	@Select(
		"SELECT"
			+ " _order_line_item_id AS id,"
			+ " _return_code AS returnCode,"
			+ " _return_message AS returnMessage"
			+ " FROM orders.add_order_line_item( #{ userId }, #{ orderId }, #{ productId }, #{ quantity } )"
	)
	IdVO addOrderLineItem(
		@Param("userId") long userId,
		@Param("orderId") int orderId,
		@Param("productId") int productId,
		@Param("quantity") short quantity
	);

	@Select(
		"SELECT"
			+ " _return_code AS returnCode,"
			+ " _return_message AS returnMessage"
			+ " FROM orders.update_order_line_item( #{ userId }, #{ orderLineItemId }, #{ quantity } )"
	)
	ReturnCodeMessageResponse updateOrderLineItem(
		@Param("userId") long userId,
		@Param("orderLineItemId") int orderDetailId,
		@Param("quantity") short quantity
	);

	@Select(
		"SELECT"
			+ " _return_code AS returnCode,"
			+ " _return_message AS returnMessage"
			+ " FROM orders.delete_order_line_item( #{ userId }, #{ orderLineItemId } )"
	)
	ReturnCodeMessageResponse deleteOrderLineItem(
		@Param("userId") long userId,
		@Param("orderLineItemId") long orderDetailId
	);
}
