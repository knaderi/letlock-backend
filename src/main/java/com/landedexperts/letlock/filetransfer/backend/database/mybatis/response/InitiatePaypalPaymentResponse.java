/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.Map;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderPaymentVO;

public class InitiatePaypalPaymentResponse extends ReturnCodeMessageResponse {

    private ResultObject result = new ResultObject();

    public InitiatePaypalPaymentResponse(OrderPaymentVO payment, String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
        setPayment(payment);
    }
    
    public InitiatePaypalPaymentResponse(OrderPaymentVO payment, Map<String, Object> responseMap,  String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
        setPayment(payment);
        setResponseMap(responseMap);
    }

    public InitiatePaypalPaymentResponse(String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
    }

    public void setResponseMap(Map<String, Object> responseMap) {
        result.setResponseMap(responseMap);
    }

    public ResultObject getResult() {
        return result;
    }

    public void setPayment(OrderPaymentVO token) {
        result.setPayment(token);
    }

    public class ResultObject {
        private OrderPaymentVO orderPaymentVO;
        private Map<String, Object> responseMap;

        public Map<String, Object> getResponseMap() {
            return responseMap;
        }

        public void setResponseMap(Map<String, Object> responseMap) {
            this.responseMap = responseMap;
        }

        public ResultObject() {
        }

        public OrderPaymentVO getPayment() {
            return orderPaymentVO;
        }

        public void setPayment(OrderPaymentVO paymentVO) {
            this.orderPaymentVO = paymentVO;
        }
    }
}
