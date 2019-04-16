package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdVO;

public interface PaymentMapper {
	@Select(
		"SELECT"
			+ " _payment_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.payment_initiate( #{ userId }, #{ orderId }, #{ type }, #{ transactionId } )"
	)
	IdVO paymentInitiate(
		@Param("userId") int userId,
		@Param("orderId") int orderId,
		@Param("type") int type,
		@Param("transactionId") String transactionId
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.payment_process_failure( #{ userId }, #{ paymentId } )"
	)
	ErrorCodeMessageVO paymentProcessFailure(
		@Param("userId") int userId,
		@Param("paymentId") int paymentId
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.payment_process_success( #{ userId }, #{ paymentId } )"
	)
	ErrorCodeMessageVO paymentProcessSuccess(
		@Param("userId") int userId,
		@Param("paymentId") int paymentId
	);
}
