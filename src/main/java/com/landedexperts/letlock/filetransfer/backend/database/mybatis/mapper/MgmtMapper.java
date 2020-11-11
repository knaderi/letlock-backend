/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AppsSettingsVO;

public interface MgmtMapper {

    @Select("SELECT *"
            + " FROM mgmt.is_free_signup_credit(cast (#{ appName } AS mgmt.tp_app_name))")
    JsonResponse<String> isFreeSignupTransferCredit(
            @Param("appName") String appName);
    
    @Select("SELECT *"
            + " FROM mgmt.apps_settings where deleted=false")
    AppsSettingsVO[] readSettings();
    
    @Select("INSERT INTO product.package_discount(" + 
            "    package_id, code, partner_name, redeem_on_action, valid_until, discount_value, discount_unit)" + 
            "    VALUES (#{ packageId }, #{ redeemCode }, #{ partnerName },#{action}, CAST (#{ validUntil } AS Timestamp), #{ discountValue }, #{ discountUnit })")
    AppsSettingsVO[] addRedeemCode( @Param("packageId") int packageId,  
                                    @Param("redeemCode") String redeemCode,
                                    @Param("partnerName") String partnerName,
                                    @Param("action") String action,
                                    @Param("validUntil") Timestamp validUntil,
                                    @Param("discountValue") BigDecimal discountValue,
                                    @Param("discountUnit") String discountUnit);

}
