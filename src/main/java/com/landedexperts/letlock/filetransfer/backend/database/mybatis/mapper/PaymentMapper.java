package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface PaymentMapper {
    @Select("SELECT"
            + " _payment_id AS id,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.payment_initiate( #{ userId }, #{ orderId }, #{ type }, #{ transactionId } )")
    IdVO paymentInitiate(
            @Param("userId") long userId,
            @Param("orderId") int orderId,
            @Param("type") int type,
            @Param("transactionId") String transactionId);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.payment_process_failure( #{ userId }, #{ paymentId } )")
    ReturnCodeMessageResponse paymentProcessFailure(
            @Param("userId") long userId,
            @Param("paymentId") int paymentId);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.payment_process_success( #{ userId }, #{ paymentId } )")
    ReturnCodeMessageResponse paymentProcessSuccess(
            @Param("userId") long userId,
            @Param("paymentId") int paymentId);

}
