package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.answer.OrderDetailResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.OrderDetailMapper;
import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdVO;
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
	public OrderDetailResponse orderDetailAdd(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="order_id" ) final int orderId,
		@RequestParam( value="product_id" ) final int productId,
		@RequestParam( value="quantity" ) final short quantity
	) throws Exception
	{
		int orderDetailId = -1;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			IdVO answer = orderDetailMapper.orderDetailAdd(userId, orderId, productId, quantity);

			orderDetailId = answer.getId();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new OrderDetailResponse(orderDetailId, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_detail_update",
		produces = {"application/JSON"}
	)
	public BooleanResponse orderDetailUpdate(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="order_detail_id" ) final int orderDetailId,
		@RequestParam( value="quantity" ) final short quantity
	) throws Exception
	{
		boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageVO answer = orderDetailMapper.orderDetailUpdate(userId, orderDetailId, quantity);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanResponse(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_detail_delete",
		produces = {"application/JSON"}
	)
	public BooleanResponse orderDetailDelete(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="order_detail_id" ) final int orderDetailId
	) throws Exception
	{
		boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageVO answer = orderDetailMapper.orderDetailDelete(userId, orderDetailId);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanResponse(result, errorCode, errorMessage);
	}
}
