/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public  final class TestUtils {
    

    public static String createWalletAddress() {

        int number = 40;
        char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };// hex digits array

        Random rand = new Random();
        String result = "";
        for (int x = 0; x < number; x++) {
            int resInt = rand.nextInt(charArr.length);// random array element
            result += charArr[resInt];
        }
        return result;
    }
    
    public static String createPayPalTransactionId() {

        int number = 13;
        char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };// hex digits array

        Random rand = new Random();
        String result = "";
        for (int x = 0; x < number; x++) {
            int resInt = rand.nextInt(charArr.length);// random array element
            result += charArr[resInt];
        }
        return result;
    }
    

    
    
    public static int getNumberOfRepetitions(String content, String repeatingString) {
           return getNumberOfRepetitions(content, repeatingString, 0,0);
    }
    
    public static int getNumberOfRepetitions(String content, String repeatingString, int count, int startingIndex) {
        int stringRepeatedLocationIndex = content.indexOf(repeatingString);
        if(stringRepeatedLocationIndex != -1) {
           int newStartingIndex = stringRepeatedLocationIndex + repeatingString.length();
            count++;
            String newContent = content.substring(newStartingIndex);
            count = getNumberOfRepetitions(newContent, repeatingString,count, newStartingIndex );
        }
        return count;
    }
    
    @Test
    public void testCount() {
        String content = "{\"returnCode\":\"SUCCESS\",\"returnMessage\":\"\",\"result\":[{\"uuid\":null,\"packageName\":\"Twenty-Transfer\",\"orderLineItemId\":20,\"receiverLoginName\":\"Bob1111\",\"contractAddress\":null,\"senderLoginName\":\"WestonDonnelly\",\"senderId\":23,\"receiverId\":2,\"ftDate\":\"2020-02-05 16:18:23.590946\",\"packageId\":3,\"isActive\":\"t\",\"creditUsed\":1,\"fTStatus\":\"Waiting for Recipient\"},{\"uuid\":null,\"packageName\":\"Twenty-Transfer\",\"orderLineItemId\":20,\"receiverLoginName\":\"Bob1111\",\"contractAddress\":null,\"senderLoginName\":\"WestonDonnelly\",\"senderId\":23,\"receiverId\":2,\"ftDate\":\"2020-02-05 16:18:23.420503\",\"packageId\":3,\"isActive\":\"t\",\"creditUsed\":1,\"fTStatus\":\"Waiting for Recipient\"}]}";
        assertEquals(2,getNumberOfRepetitions(content, "\"creditUsed\":1"));
    }

}
