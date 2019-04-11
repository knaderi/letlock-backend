package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanAnswer;
import com.landedexperts.letlock.filetransfer.backend.answer.OrderDetailAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.OrderDetailMapper;
import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.OrderDetailResult;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderDetailController {
	@Autowired
	private OrderDetailMapper orderDetailMapper;

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_detail_add",
		produces = {"application/JSON"}
	)
	public OrderDetailAnswer orderDetailAdd(
		@RequestParam( value="token" ) String token,
		@RequestParam( value="order_id" ) int orderId,
		@RequestParam( value="product_id" ) int productId,
		@RequestParam( value="quantity" ) short quantity
	) throws Exception
	{
		int orderDetailId = -1;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			OrderDetailResult answer = orderDetailMapper.orderDetailAdd(userId, orderId, productId, quantity);

			orderDetailId = answer.getOrderDetailId();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new OrderDetailAnswer(orderDetailId, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_detail_update",
		produces = {"application/JSON"}
	)
	public BooleanAnswer orderDetailUpdate(
		@RequestParam( value="token" ) String token,
		@RequestParam( value="order_detail_id" ) int orderDetailId,
		@RequestParam( value="quantity" ) short quantity
	) throws Exception
	{
		boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageResult answer = orderDetailMapper.orderDetailUpdate(userId, orderDetailId, quantity);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanAnswer(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_detail_delete",
		produces = {"application/JSON"}
	)
	public BooleanAnswer orderDetailDelete(
		@RequestParam( value="token" ) String token,
		@RequestParam( value="order_detail_id" ) int orderDetailId
	) throws Exception
	{
		boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageResult answer = orderDetailMapper.orderDetailDelete(userId, orderDetailId);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanAnswer(result, errorCode, errorMessage);
	}
}
