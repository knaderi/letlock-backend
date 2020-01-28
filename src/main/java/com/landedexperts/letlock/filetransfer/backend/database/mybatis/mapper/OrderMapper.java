package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
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
    JsonResponse getPackages(boolean isPackageDeleted, boolean isProductDeleted);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM orders.upsert_order_line_item( #{ userId }, #{ orderId }, #{ packageId } , #{ quantity }, #{ locationId })")
    ReturnCodeMessageResponse upsertOrderLineItem(long userId, long orderId, int packageId, short quantity, short locationId);
}
