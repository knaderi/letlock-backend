/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AlgoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.utils.EmailValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.LoginNameValidator;

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String LOGIN_NAME_IS_INVALID = "Login name is invalid";
    private static final String INVALID_LOGINNAME = "INVALID_LOGINNAME";
    private static final String EMAIL_IS_INVALID = "Email is invalid";
    private static final String INVALID_LOGIN = "INVALID_LOGIN";
    @Autowired
    private UserMapper userMapper;

    @Autowired
    EmailServiceFacade emailServiceFacade;

    @RequestMapping(method = RequestMethod.POST, value = "/user_is_login_name_available", produces = { "application/JSON" })
    public BooleanResponse isLoginNameAvailable(@RequestParam(value = "loginName") final String loginName) throws Exception {
        logger.info("UserController.isLoginNameAvailable called for loginName " + loginName);
        BooleanResponse response = userMapper.isLoginNameAvailable(loginName);

        boolean result = response.getResult().getValue();
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register", produces = { "application/JSON" })
    public IdVO register(@RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "email") final String email, @RequestParam(value = "password") final String password) throws Exception {
        logger.info("UserController.register called for loginName " + loginName + " email " + email);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        IdVO answer = new IdVO();
        if (!isLoginCriteriaAnEmail(email)) {
            returnCode = INVALID_LOGIN;
            returnMessage = EMAIL_IS_INVALID;
        } else if (!LoginNameValidator.isValid(loginName)) {
            returnCode = INVALID_LOGINNAME;
            returnMessage = String.format(LOGIN_NAME_IS_INVALID + " for loginName: %s", loginName);
        } else {
            String resetToken = UUID.randomUUID().toString();
            logger.info("****************Calling register on db side");
            try {
                answer = userMapper.register(loginName, email, password, resetToken);
                returnCode = answer.getReturnCode();
                returnMessage = answer.getReturnMessage();
            } catch (Exception e) {
                returnCode = "REGISTER_ERROR";
                returnMessage = e.getMessage();
            }

            if ("SUCCESS".equals(returnCode)) {
                logger.info("regsitered user with email " + email + " and with loginName " + loginName);
                emailServiceFacade.sendConfirmSignupHTMLEmail(email, resetToken);
            }
        }
        answer.setReturnCode(returnCode);
        answer.setReturnMessage(returnMessage);

        return answer;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resend_signup_email", produces = { "application/JSON" })
    public IdVO resendSignUpConfirmationEmail(
            @RequestParam(value = "loginId") final String loginId, @RequestParam(value = "password") final String password)
            throws Exception {
        logger.info("UserController.resend_signup_email called for loginId " + loginId);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        IdVO answer = new IdVO();
        String resetToken = "";
        if (!isLoginCriteriaAnEmail(loginId) && !LoginNameValidator.isValid(loginId)) {
            returnCode = INVALID_LOGIN;
            returnMessage = EMAIL_IS_INVALID;
        } else {
            UserVO userVO = getUserObject(loginId, password);
            try {
                if (userVO != null) {
                    resetToken = userVO.getResetToken();
                    if (StringUtils.isBlank(resetToken)) {
                        returnCode = "NO_CONFIRMATION_NEEDED";
                        returnMessage = "user is found but no confirmation is needed.";
                    }
                } else {
                    returnCode = "USER_NOT_FOUND";
                    returnMessage = "No user was found for the given liginId and password";
                }
            } catch (Exception e) {
                returnCode = "RESEND_SIGNUP_ERROR";
                returnMessage = e.getMessage();
            }

            if ("SUCCESS".equals(returnCode)) {
                emailServiceFacade.sendConfirmSignupHTMLEmail(loginId, resetToken);
            } else {
                logger.error("UserController.resendSignUpConfirmationEmail failed for loginId "
                        + loginId
                        + " failed. returnCode: "
                        + returnCode
                        + " returnMessage: "
                        + returnMessage);
            }
        }
        answer.setReturnCode(returnCode);
        answer.setReturnMessage(returnMessage);

        return answer;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login", produces = { "application/JSON" })
    public SessionTokenResponse login(@RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "password") final String password) throws Exception {
        logger.info("UserController.login called for loginName " + loginName);
        IdVO answer = userMapper.login(loginName, password);

        long userId = answer.getResult().getId();
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        String token = "";
        if (returnCode.equals("SUCCESS")) {
            token = SessionManager.getInstance().generateSessionToken(userId);
        }

        return new SessionTokenResponse(token, returnCode, returnMessage);
    }

    private boolean isLoginCriteriaAnEmail(final String loginNameOrEmail) {
        return new EmailValidator().isValid(loginNameOrEmail);
    }

    @PostMapping(value = "/user/change_password", produces = { "application/JSON" })
    public BooleanResponse changePassword(@RequestParam(value = "token") final String token,
            @RequestParam(value = "loginName") final String loginName, @RequestParam(value = "email") final String email,
            @RequestParam(value = "oldPassword") final String oldPassword, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {
        logger.info("UserController.changePassword called for a user");
        long userId = SessionManager.getInstance().getUserId(token);
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        if (userId > 0) {
            ReturnCodeMessageResponse answer = userMapper.updateUserPassword(loginName, oldPassword, newPassword);
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
        }
        boolean result = returnCode.equals(returnCode);
        if ("SUCCESS".equals(returnCode)) {
            emailServiceFacade.sendChangePasswordEmail(email);
        } else {
            logger.error("UserController.changePassword failed for email "
                    + email
                    + " failed. returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + returnMessage);
        }
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/logout", produces = { "application/JSON" })
    public BooleanResponse logout(@RequestParam(value = "token") final String token) throws Exception {
        logger.info("UserController.logout called for token " + token);

        SessionManager instance = SessionManager.getInstance();
        if (instance.isActive(token)) {
            instance.cleanSession(token);
            return new BooleanResponse(true, "SUCCESS", "");
        } else {
            return new BooleanResponse(false, "LOGIN_SESSION_NOT_FOUND", "There is no active login session for the given token");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/handle_forgot_password", produces = { "application/JSON" })
    public ReturnCodeMessageResponse handleForgotPassword(@RequestParam(value = "email") final String email) throws Exception {
        logger.info("UserController.forgotPassword called for email " + email);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        String resetToken = "";
        try {
            resetToken = UUID.randomUUID().toString();

            ReturnCodeMessageResponse response = userMapper.handleForgotPassword(email, resetToken);

            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();

            if ("SUCCESS".equals(returnCode)) {
                logger.info("handle_forgot_password email: " + email + ", resetToken: " + resetToken);
                emailServiceFacade.sendForgotPasswordHTMLEmail(email, resetToken);
            } else {
                logger.error("handle_forgot_password failed for: " + email + " error code: " + returnCode,
                        " return Message: " + returnMessage);
            }
        } catch (Exception e) {
            logger.error("Exception thrown sening email." + e.getMessage());
            returnCode = "FORGOT_PASSWORD_EMAIL_ERROR";
            returnMessage = e.getMessage();
            logger.error("handle_forgot_password failed for: " + email + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }
        return new ReturnCodeMessageResponse(returnCode, returnMessage);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/getUserPasswordAlgo", produces = { "application/JSON" })
    public AlgoVO getUserPasswordAlgo(@RequestParam(value = "loginName") final String loginName) throws Exception {

        String encodingAlgo = "";
        String hashingAlgo = "";
        String returnCode = "SUCCESS";
        String returnMessage = "";
        try {
            AlgoVO response = userMapper.getUserPasswordAlgo(loginName);

            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
            if ("SUCCESS".equals(returnCode)) {
                encodingAlgo = response.getEncodingAlgo();
                hashingAlgo = response.getHashingAlgo();

            } else {
                logger.error("getUserPasswordAlgo call for loginName: "
                        + loginName
                        + " failed. returnCode: "
                        + returnCode
                        + " returnMessage: "
                        + returnMessage);
            }
        } catch (Exception e) {
            hashingAlgo = "";
            encodingAlgo = "";
            logger.error("Exception thrown sening email." + e.getMessage());
            returnCode = "FORGOT_PASSWORD_EMAIL_ERROR";
            returnMessage = e.getMessage();
        }
        return new AlgoVO(hashingAlgo, encodingAlgo, returnCode, returnMessage);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/reset_password", produces = { "application/JSON" })
    public BooleanResponse resetPassword(@RequestParam(value = "token") final String token,
            @RequestParam(value = "email") final String email, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {
        logger.info("UserController.resetPassword called for email " + email);
        BooleanResponse response = userMapper.resetUserPassword(email, token, newPassword);
        boolean result = response.getResult().getValue();
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("resetPassword failed for token " + token + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate_reset_password_token", produces = { "application/JSON" })
    public BooleanResponse validateResetPasswordToken(@RequestParam(value = "token") final String token) throws Exception {

        logger.info("UserController.validateResetPasswordToken called for validating resetToken");
        BooleanResponse response = userMapper.isPasswordResetTokenValid(token);
        boolean result = response.getResult().getValue();
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("validateResetPasswordToken failed for token " + token + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_user_object", produces = { "application/JSON" })
    public UserVO getUserObject(String email, String password) {
        String returnCode = "SUCCESS";
        String returnMessage = "";
        UserVO response = new UserVO();
        try {
            response = userMapper.getUserObject(email, password);

        } catch (Exception e) {
            logger.error("UserMapper.getUserObject threw an Exception " + e.getMessage());
            returnCode = "USER_NOT_FOUND";
            returnMessage = "Cannot find user using email " + email;
        }
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("getUserObject failed for email " + email + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }
        response.setReturnCode(returnCode);
        response.setReturnMessage(returnMessage);
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm_signup", produces = { "application/JSON" })
    public BooleanResponse confirmSignup(@RequestParam(value = "email") final String email,
            @RequestParam(value = "resetToken") final String resetToken) throws Exception {
        logger.info("UserController.confirm_signup called for email " + email);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        boolean result = false;
        try {

            BooleanResponse response = userMapper.confirmSignup(email, resetToken);

            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
            result = true;
            if (!"SUCCESS".equals(returnCode)) {
                logger.error("confirmSignup failed for email " + email + " error code: " + returnCode,
                        " return Message: " + returnMessage);
            }
        } catch (Exception e) {
            logger.error("Exception thrown sening email." + e.getMessage());
            returnCode = "FORGOT_PASSWORD_EMAIL_ERROR";
            returnMessage = e.getMessage();
            logger.error("confirmSignup failed for email " + email + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }

        return new BooleanResponse(result, returnCode, returnMessage);

    }

    @PostMapping(value = "/user/message", produces = { "application/JSON" })
    public BooleanResponse submitContactUsForm(@Valid @RequestBody ContactUsModel contactUsModel) throws Exception {
        logger.info("UserController.submitContactUsForm called for ContactUsModel " + contactUsModel);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        try {
            String validaionMessage = contactUsModel.validate();
            if (validaionMessage.equals(contactUsModel.VALID_MSG)) {
                emailServiceFacade.sendContactUsEmail(contactUsModel);
            } else {
                logger.error("The contact us form being sumitted is not valid");
                returnCode = "INVALID_CONTENT";
                returnMessage = validaionMessage;
                logger.error("submitContactUsForm failed for ContactUsModel " + contactUsModel.toString() + " error code: " + returnCode,
                        " return Message: " + returnMessage);
            }

        } catch (Exception e) {
            logger.error("Exception thrown sending Contact Us email." + e.getMessage());
            returnCode = "CONTACT_US_SUMISSION_ERROR";
            returnMessage = e.getMessage();
            logger.error("submitContactUsForm failed for ContactUsModel " + contactUsModel.toString() + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }
        return new BooleanResponse(false, returnCode, returnMessage);
    }

}
