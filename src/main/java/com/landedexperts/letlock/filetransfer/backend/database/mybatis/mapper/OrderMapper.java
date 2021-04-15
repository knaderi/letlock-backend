/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.CreateOrderResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrdersFileTransfersCountsVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferOrderLineItemUsageVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface OrderMapper {
    @Select("SELECT"
            + " _order_id AS id,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.order_create( #{userId} )")
    IdVO orderCreate(@Param("userId") long userId);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.update_order_status_initiated_to_cancelled( #{ userId }, #{ orderId } )")
    ReturnCodeMessageResponse changeStatusInitiatedToCancelled(@Param("userId") long userId, @Param("orderId") int orderId);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.update_order_status_cancelled_to_initiated( #{ userId }, #{ orderId } )")
    ReturnCodeMessageResponse changeStatusCancelledToInitiated(@Param("userId") long userId, @Param("orderId") int orderId);

    @Select("SELECT"
            + " * "
            + " FROM product.get_packages(#{isPackageDeleted}, #{isProductDeleted})")
    String getPackages(boolean isPackageDeleted, boolean isProductDeleted);

    @Select("SELECT"
            + " * "
            + " FROM orders.vw_get_locations")
    String getLocations();

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.upsert_order_line_item( #{ userId }, #{ orderId }, #{ packageId } , #{ quantity }, #{ locationId })")
    ReturnCodeMessageResponse upsertOrderLineItem(long userId, long orderId, int packageId, short quantity, short locationId);

    @Select("SELECT *"
            + " FROM orders.get_user_order( #{ userId }, #{ orderId })")
    JsonResponse<String> getUserOrder(
            @Param("userId") long userId, @Param("orderId") long orderId);

    @Select("SELECT *"
            + " FROM orders.get_user_orders( #{ userId }, cast(#{orderStatus} AS orders.tp_order_status))")
    JsonResponse<Map<String, String>> getUserOrders(
            @Param("userId") long userId, @Param("orderStatus") String orderStatus);
    
    @Select("")
    JsonResponse<String> getUserOrdersASJson(
            @Param("userId") long userId, @Param("orderId") long orderId, @Param("userStatus") String userStatus,
            @Param("orderStatus") String orderStatus);
    
    @Select("SELECT *"
            + " FROM orders.get_orders_file_transfer_counts( #{ userId }, #{itemTypeName}, #{ orderId } )")
    OrdersFileTransfersCountsVO getOrdersFileTransferUsageCounts(
            @Param("userId") long userId, @Param("orderId") long orderId, @Param("itemTypeName") String itemTypeName);

    
    @Select("SELECT *"
            + " FROM orders.get_order_file_transfer_usage( #{ userId }, #{ orderId })")
    FileTransferOrderLineItemUsageVO[] getUsersFileTransferOrderUsageHistroy(
            @Param("userId") long userId, @Param("orderId") long orderId);
    
    @Select("SELECT *"
            + " FROM orders.buy_package_now( #{ userId }, #{ packageId })")
    CreateOrderResponse buyPackageNow(
            @Param("userId") long userId, @Param("packageId") int packageId);

}
