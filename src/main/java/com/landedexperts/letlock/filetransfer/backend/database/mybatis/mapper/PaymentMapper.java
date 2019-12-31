package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface PaymentMapper {
    @Select("SELECT"
            + " _payment_id AS id,"
            + " _error_code AS errorCode,"
            + " _error_message AS errorMessage"
            + " FROM orders.payment_initiate( #{ userId }, #{ orderId }, #{ type }, #{ transactionId } )")
    IdVO paymentInitiate(
            @Param("userId") int userId,
            @Param("orderId") int orderId,
            @Param("type") int type,
            @Param("transactionId") String transactionId);

    @Select("SELECT"
            + " _error_code AS errorCode,"
            + " _error_message AS errorMessage"
            + " FROM orders.payment_process_failure( #{ userId }, #{ paymentId } )")
    ErrorCodeMessageResponse paymentProcessFailure(
            @Param("userId") int userId,
            @Param("paymentId") int paymentId);

    @Select("SELECT"
            + " _error_code AS errorCode,"
            + " _error_message AS errorMessage"
            + " FROM orders.payment_process_success( #{ userId }, #{ paymentId } )")
    ErrorCodeMessageResponse paymentProcessSuccess(
            @Param("userId") int userId,
            @Param("paymentId") int paymentId);

}
