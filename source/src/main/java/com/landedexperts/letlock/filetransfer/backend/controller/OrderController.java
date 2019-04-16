package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mapper.OrderMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.response.OrderResponse;
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
	public OrderResponse order_create(
		@RequestParam( value="token" ) final String token
	) throws Exception
	{
		int orderId = -1;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			IdVO answer = orderMapper.orderCreate(userId);

			orderId = answer.getId();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new OrderResponse(orderId, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_change_status_initiated_to_cancelled",
		produces = {"application/JSON"}
	)
	public BooleanResponse orderChangeStatusInitiatedToCancelled(
			@RequestParam( value="token" ) final String token,
			@RequestParam( value="order_id" ) final int orderId
	) throws Exception
	{
		Boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageVO answer = orderMapper.changeStatusInitiatedToCancelled(userId, orderId);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();

			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanResponse(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_change_status_cancelled_to_initiated",
		produces = {"application/JSON"}
	)
	public BooleanResponse orderChangeStatusCancelledToInitiated(
			@RequestParam( value="token" ) final String token,
			@RequestParam( value="order_id" ) final int orderId
	) throws Exception
	{
		Boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageVO answer = orderMapper.changeStatusCancelledToInitiated(userId, orderId);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();

			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanResponse(result, errorCode, errorMessage);
	}
}
