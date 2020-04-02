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

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * HTMLTag validator Testing
 * @author mkyong
 *
 */
public class HTMLTagValidatorTest {
 
    private HTMLTagValidator htmlTagValidator;
    
    @BeforeClass
        public void initData(){
        htmlTagValidator = new HTMLTagValidator();
        }
    
    @DataProvider
    public Object[][] ValidHTMLTagProvider() {
           return new Object[][]{
           new Object[] {"<b>"}, 
                   new Object[] {"<input value='>'>"},
           new Object[] {"<input value='<'>"}, 
           new Object[] {"<b/>"},
                   new Object[] {"<a href='http://www.google.com'>"},
           new Object[] {"<br>"},
                   new Object[] {"<br/>"},
           new Object[] {"<input value=\"\" id='test'>"},
                   new Object[] {"<input value='' id='test'>"}
       };
    }
    
    @DataProvider
    public Object[][] InvalidHTMLTagProvider() {
        return new Object[][]{
          new Object[] {"<input value=\" id='test'>"},
          new Object[] {"<input value=' id='test'>"},
          new Object[] {"<input value=> >"}
        };
    }
    
    @Test(dataProvider = "ValidHTMLTagProvider")
    public void ValidHTMLTagTest(String tag) {
        
        boolean valid = htmlTagValidator.validate(tag);
        System.out.println("HTMLTag is valid : " + tag + " , " + valid);
        Assert.assertEquals(true, valid);
       
    }
    
    @Test(dataProvider = "InvalidHTMLTagProvider", 
                 dependsOnMethods="ValidHTMLTagTest")
    public void InValidHTMLTagTest(String tag) {
        
       boolean valid = htmlTagValidator.validate(tag);
       System.out.println("HTMLTag is valid : " + tag + " , " + valid);
       Assert.assertEquals(false, valid);
       
    }
}
