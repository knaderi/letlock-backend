package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.CompletePayPalPaymentResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderPaymentVO;

public interface PaymentMapper {
    @Select("SELECT"
            + " _payment_id AS id,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.payment_initiate( #{ userId }, #{ orderId }, CAST( #{ type } AS orders.tp_payment_type))")
    public IdVO setPaymentInitiate(
            @Param("userId") long userId,
            @Param("orderId") long orderId,
            @Param("type") String type);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.payment_process_failure( #{ userId }, #{ paymentId } )")
    CompletePayPalPaymentResponse setPaymentProcessFailure(
            @Param("userId") long userId,
            @Param("paymentId") long paymentId);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.payment_process_success( #{ userId }, #{ paymentId }, #{ paypalPaymentId } )")
    CompletePayPalPaymentResponse setPaymentProcessSuccess(
            @Param("userId") long userId,
            @Param("paymentId") long paymentId,
            @Param("paypalPaymentId") String paypalPaymentId);
    
    
    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage,"
            + " _order_id AS orderId,"
            + " order_status AS orderStatus,"
            + " tax_amount AS taxAmount,"
            + " tax_type AS taxType,"
            + " order_subtotal AS orderSubtotal,"
            + " order_total AS orderTotal,"
            + " create_dt AS orderCreateDt,"
            + " payment_id AS paymentId,"
            + " transaction_id AS paymentTransactionId,"
            + " payment_status AS paymentStatus"
            + " FROM orders.get_user_order_payment( #{ userId }, #{ orderId } )")
    OrderPaymentVO getUserOrderPayment(
            @Param("userId") long userId,
            @Param("orderId") long orderId);

}
