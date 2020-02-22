package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.OrderMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.CreateOrderResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrderFileTransferUsagesResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrdersFileTransfersCountsResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferOrderLineItemUsageVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/order_create", produces = { "application/JSON" })
    public CreateOrderResponse createOrder(@RequestParam(value = "token") final String token) throws Exception {
        logger.info("OrderController.createOrder called for token " + token);
        long orderId = -1;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            IdVO answer = orderMapper.orderCreate(userId);

            orderId = answer.getResult().getId();
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
        }

        return new CreateOrderResponse(orderId, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_status_to_cancelled", produces = { "application/JSON" })
    public BooleanResponse updateOrderStatusInitiatedToCancelled(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final String orderId) throws Exception {
        logger.info("OrderController.updateOrderStatusInitiatedToCancelled called for token " + token + " and OrderId " + orderId);
        Boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ReturnCodeMessageResponse answer = orderMapper.changeStatusInitiatedToCancelled(userId, Integer.parseInt(orderId));

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            result = returnCode.equals("SUCCESS");
        }

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_status_to_initiated", produces = { "application/JSON" })
    public BooleanResponse UpdateOrderStatusCancelledToInitiated(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final int orderId) throws Exception {
        logger.info("OrderController.UpdateOrderStatusCancelledToInitiated called for token " + token + " and OrderId " + orderId);
        Boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ReturnCodeMessageResponse answer = orderMapper.changeStatusCancelledToInitiated(userId, orderId);

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            result = returnCode.equals("SUCCESS");
        }

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_packages", produces = { "application/JSON" })
    public JsonResponse<String> getPackages() {
        logger.info("OrderController.getProducts called");
        String value = orderMapper.getPackages(false, false);
        return new JsonResponse<String>(value);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_locations", produces = { "application/JSON" })
    public JsonResponse<String> getLocations() throws JSONException {
        logger.info("OrderController.getLocations called");
        return new JsonResponse<String>(orderMapper.getLocations());
    }
    
//    @RequestMapping(method = RequestMethod.POST, value = "/upsert_order_line_item", produces = { "application/JSON" })
//    public JsonResponse upsertOrderLineItem(@RequestParam(value = "token") final String token,
//            @RequestParam(value = "orderId") final int orderId,
//            @RequestParam(value = "packageId") final int packageId,
//            @RequestParam(value = "quantity") final short quantity,
//            @RequestParam(value = "locationId") final short locationId) throws Exception {
//        logger.info("OrderController.upsertOrderLineItem called for token " + token + " and OrderId " + orderId);
//        String returnCode = "TOKEN_INVALID";
//        String returnMessage = "Invalid token";
//
//        long userId = SessionManager.getInstance().getUserId(token);
//        if (userId > 0) {
//            ReturnCodeMessageResponse answer = orderMapper.upsertOrderLineItem(userId, orderId, packageId, quantity, locationId);
//
//            returnCode = answer.getReturnCode();
//            returnMessage = answer.getReturnMessage();
//        }
//        if (returnCode.equals("SUCCESS")) {
//            return getUserOrders(token, orderId);
//        }
//        
//        return new JsonResponse("",returnCode, returnMessage) ;  
//
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/upsert_order_line_item", produces = { "application/JSON" })
    public JsonResponse<String> upsertOrderLineItem(@RequestParam(value = "token") final String token,
            @RequestParam(value = "orderId") final int orderId,
            @RequestParam(value = "packageId") final int packageId,
            @RequestParam(value = "quantity") final short quantity,
            @RequestParam(value = "locationId") final short locationId) throws Exception {
        logger.info("OrderController.upsertOrderLineItem called for token " + token + " and OrderId " + orderId);
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ReturnCodeMessageResponse answer = orderMapper.upsertOrderLineItem(userId, orderId, packageId, quantity, locationId);

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

        }
        if (returnCode.equals("SUCCESS")) {
            return getUserOrder(token, orderId);
        }else {
            return new JsonResponse<String>("",returnCode, returnMessage) ;  
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_user_orders", produces = { "application/JSON" })
    public JsonResponse<Map<String, String>> getUserOrders(@RequestParam(value = "token") final String token) throws Exception {
        logger.info("OrderController.getUserOrders called for token " + token + "\n");

        JsonResponse<Map<String, String>> value = new JsonResponse<Map<String, String>>();

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            value = orderMapper.getUserOrders(userId, "any");
        }else {
            value.setReturnCode("USER_NOT_FOUND");
            value.setReturnMessage("User does not exist.");
        }

        return value;
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/get_user_order", produces = { "application/JSON" })
    public JsonResponse<String> getUserOrder(@RequestParam(value = "token") final String token,
            @RequestParam(value = "orderId") final long orderId) throws Exception {
        logger.info("OrderController.getUserOrders called for token " + token + "\n");

        JsonResponse<String> value = new JsonResponse<String>();

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            value = orderMapper.getUserOrder(userId, orderId);
        }else {
            value.setReturnCode("USER_NOT_FOUND");
            value.setReturnMessage("User does not exist.");
        }

        return value;
    }
    

    @RequestMapping(method = RequestMethod.POST, value = "/get_user_ft_order_usage_history", produces = { "application/JSON" })
    public OrderFileTransferUsagesResponse getUserOrderUsageHistroy(@RequestParam(value = "token") final String token,
            @RequestParam(value = "orderId") final long orderId) throws Exception {
        logger.info("OrderController.getUsersAllOrdersUsageHistroy called for token " + token + "\n");
        FileTransferOrderLineItemUsageVO[] lineItemsUsageForOrderArray = new FileTransferOrderLineItemUsageVO[] {};
        //TODO, should filter
        OrdersFileTransfersCountsResponse ordersFTCounts = new OrdersFileTransfersCountsResponse();
        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            lineItemsUsageForOrderArray = orderMapper.getUsersFileTransferOrderUsageHistroy(userId, orderId);
            ordersFTCounts = orderMapper.getOrdersFileTransferUsageCounts(userId, orderId); 
            if(null == ordersFTCounts) {
                ordersFTCounts =  new OrdersFileTransfersCountsResponse();
            }
        }

        return new OrderFileTransferUsagesResponse(orderId, lineItemsUsageForOrderArray, ordersFTCounts);
    }

    //TODO: Do thsi later to return all order usages.
//    @RequestMapping(method = RequestMethod.POST, value = "/get_all_user_ft_orders_usage_history", produces = { "application/JSON" })
//    public AllOrdersFileTransferUsagesResponse getAllUserOrdersUsageHistroy(@RequestParam(value = "token") final String token) throws Exception {
//        logger.info("OrderController.getUsersAllOrdersUsageHistroy called for token " + token + "\n");
//
//        List<OrderUsageVO> value = new ArrayList<OrderUsageVO>();
//        OrdersFileTransfersCountsResponse ordersFTCounts = null;
//        FileTransferOrderLineItemUsageVO[] lineItemsUsageForOrderArray = new FileTransferOrderLineItemUsageVO[] {};
//
//        long userId = SessionManager.getInstance().getUserId(token);
//        if (userId > 0) {
//            lineItemsUsageForOrderArray = orderMapper.getUsersAllFileTransferOrdersUsageHistroy(userId);
//            ordersFTCounts = orderMapper.getOrdersFileTransferUsageCounts(-1); 
//        }
//        int arraySize = lineItemsUsageForOrderArray.length;
//        Map<Integer,List<OrderUsageVO>> ordersUsageMap = new HashMap<Integer,List<OrderUsageVO>>();
//        if(lineItemsUsageForOrderArray != null && arraySize>0) {
//            for(int i; i<arraySize; i++) {
//                ordersUsageMap.put(ordersUsageMap.put(lineItemsUsageForOrderArray[i].getOrderId value);
//            }
//            
//        }
//
//        return new AllOrdersFileTransferUsagesResponse(value, ordersFTCounts);
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/buy_package_now", produces = { "application/JSON" })
    public JsonResponse buyPackageNow(@RequestParam(value = "token") final String token,
            @RequestParam(value = "package_id") final int packageId) throws Exception {
        logger.info("OrderController.buyPackageNow called for token " + token);
        long orderId = -1;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            CreateOrderResponse answer = orderMapper.buyPackageNow(userId, packageId);

            orderId = answer.getOrderId();
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
        }
        if (returnCode.equals("SUCCESS")) {
            return getUserOrder(token, orderId);
        }else {
            return new JsonResponse(orderId,returnCode, returnMessage) ;  
        }

    }

}
