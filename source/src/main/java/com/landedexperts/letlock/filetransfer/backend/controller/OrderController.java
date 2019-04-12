package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanAnswer;
import com.landedexperts.letlock.filetransfer.backend.answer.OrderAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.OrderMapper;
import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.OrderResult;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderController {
	@Autowired
	private OrderMapper orderMapper;

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_create",
		produces = {"application/JSON"}
	)
	public OrderAnswer order_create(
		@RequestParam( value="token" ) String token
	) throws Exception
	{
		int orderId = -1;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			OrderResult answer = orderMapper.orderCreate(userId);

			orderId = answer.getOrderId();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new OrderAnswer(orderId, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_change_status_initiated_to_cancelled",
		produces = {"application/JSON"}
	)
	public BooleanAnswer orderChangeStatusInitiatedToCancelled(
			@RequestParam( value="token" ) String token,
			@RequestParam( value="order_id" ) int orderId
	) throws Exception
	{
		Boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageResult answer = orderMapper.changeStatusInitiatedToCancelled(userId, orderId);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();

			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanAnswer(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_change_status_cancelled_to_initiated",
		produces = {"application/JSON"}
	)
	public BooleanAnswer orderChangeStatusCancelledToInitiated(
			@RequestParam( value="token" ) String token,
			@RequestParam( value="order_id" ) int orderId
	) throws Exception
	{
		Boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageResult answer = orderMapper.changeStatusCancelledToInitiated(userId, orderId);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();

			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanAnswer(result, errorCode, errorMessage);
	}
}
