package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.OrderAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.OrderMapper;
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
}
