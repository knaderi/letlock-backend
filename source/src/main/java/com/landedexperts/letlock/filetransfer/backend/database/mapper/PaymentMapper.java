package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdResult;

public interface PaymentMapper {
	@Select("SELECT"
			+ " _payment_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.payment_initiate( #{ userId }, #{ orderId }, #{ type }, #{ transactionId } )")
	IdResult paymentInitiate(
		@Param("userId") int userId,
		@Param("orderId") int orderId,
		@Param("type") int type,
		@Param("transactionId") String transactionId
	);

	@Select("SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.payment_process_failure( #{ userId }, #{ paymentId } )")
	ErrorCodeMessageResult paymentProcessFailure(
		@Param("userId") int userId,
		@Param("paymentId") int paymentId
	);

	@Select("SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM payment.payment_process_success( #{ userId }, #{ paymentId } )")
	ErrorCodeMessageResult paymentProcessSuccess(
		@Param("userId") int userId,
		@Param("paymentId") int paymentId
	);
}
