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

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * HTMLTag validator Testing
 * 
 * @author knaderi
 *
 */

public class HTMLTagValidatorTest {

    private HTMLTagValidator htmlTagValidator = new HTMLTagValidator();;

    static Stream<Arguments> testValidHTMLTag() throws Throwable {
        return Stream.of(
                Arguments.of("<b>"),
                Arguments.of("<input value='>'>"),
                Arguments.of("<input value='<'>"),
                Arguments.of("<b/>"),
                Arguments.of("<a href='http://www.google.com'>"),
                Arguments.of("<br>"),
                Arguments.of("<br/>"),
                Arguments.of("<input value=\"\" id='test'>"),
                Arguments.of("<input value='' id='test'>"));
    }

    @ParameterizedTest(name = "Run {index}:input= {0}")
    @MethodSource
    public void testValidHTMLTag(String input) throws Throwable {
        Assertions.assertEquals(true, htmlTagValidator.validate(input));

    }


    @ParameterizedTest
    @ValueSource(strings = {"This is something that I am embedding<a href='www.google.com'>Google</a>", "Thi sis an advertisement to go to <a href=\"\">sdssdf</>"})
    public void testEmbededHTMLTag(String tag) {

        boolean valid = htmlTagValidator.hasHTMLTags(tag);
        System.out.println("Has HTMLTag: " + tag + " , " + valid);
        Assertions.assertEquals(true, valid);

    }


    @ParameterizedTest
    @ValueSource(strings = {"This is something that I am embedding", "  This is something that I am embedding"})
    public void hasNoHTML(String tag) {

        boolean valid = htmlTagValidator.hasHTMLTags(tag);
        Assertions.assertEquals(false, valid);
    }

    
    @ParameterizedTest
    @ValueSource(strings = {"<input value=\\\" id='test'>", "<input value=' id='test'>", "<input value=> >"})
    public void invalidHTML(String tag) {

        boolean valid = htmlTagValidator.validate(tag);
        System.out.println("HTMLTag is valid : " + tag + " , " + valid);
        Assertions.assertEquals(false, valid);
    }

}
