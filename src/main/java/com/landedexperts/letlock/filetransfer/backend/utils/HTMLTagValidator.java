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

/**
 * @author knaderi
 *
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

 
public class HTMLTagValidator{
    
   private Pattern pattern;
   private Matcher matcher;
 
   private static final String HTML_TAG_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
      
   public HTMLTagValidator(){
      pattern = Pattern.compile(HTML_TAG_PATTERN);
   }
      
  /**
   * Validate html tag with regular expression
   * @param tag html tag for validation
   * @return true valid html tag, false invalid html tag
   */
  public boolean validate(final String tag){
          
      matcher = pattern.matcher(tag);
      return matcher.matches();
                
  }
  


  public boolean hasHTMLTags(String text){
      Matcher matcher = pattern.matcher(text);
      return matcher.find();
  }
}