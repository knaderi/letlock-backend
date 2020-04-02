/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
/**
 * 
 */
package com.landedexperts.letlock.filetransfer.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author knaderi
 *
 */
public class PhoneNumberValidator {

    private static String regex = "^\\+?[0-9. ()-]{10,25}$";
    
    public static  boolean isValid(String phoneNumber) {        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
