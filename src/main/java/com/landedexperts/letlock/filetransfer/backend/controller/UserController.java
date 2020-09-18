/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ResetTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.UserProfileResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AlgoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.ResetTokenVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;
import com.landedexperts.letlock.filetransfer.backend.session.AppSettingsManager;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.utils.AntideoEmailValiationVO;
import com.landedexperts.letlock.filetransfer.backend.utils.EmailValidationResult;
import com.landedexperts.letlock.filetransfer.backend.utils.EmailValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.LoginNameValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.RequestData;

@RestController
public class UserController {

    private static final String EMAIL_UNRELIABLE = "EMAIL_UNRELIABLE";
    private static final String EMAIL_DISPOSABLE = "EMAIL_DISPOSABLE";
    private static final String EMAIL_VALIDATION_ERROR = "EMAIL_VALIDATION_ERROR";
    private static final String EMAIL_INVALID = "EMAIL_INVALID";
    private static final String EMAIL_INVALID_MSG = "The specified email is invalid";
    private static final String REACHED_MAXIMUM_ANTIDEO_CALL_CODE = "429";
    private static final String EMAIL_TAKEN = "EMAIL_TAKEN";
    private static final String EMAIL_VARIATION_EXIST = "EMAIL_VARIATION_EXIST";
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String LOGIN_NAME_INVALID_MSG = "Login name is invalid";

    private static final String LOGIN_NAME_INVALID = "INVALID_NAME_LOGIN";
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AppSettingsManager appsSettingsManager;

    @Autowired
    EmailServiceFacade emailServiceFacade;

    @RequestMapping(method = RequestMethod.POST, value = "/user_is_login_name_available", produces = { "application/JSON" })
    public BooleanResponse isLoginNameAvailable(@RequestParam(value = "loginName") final String loginName) {
        logger.info("UserController.isLoginNameAvailable called for loginName " + loginName);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        boolean result = true;
        try {
            BooleanResponse response = userMapper.isLoginNameAvailable(loginName);

            result = response.getResult().getValue();
            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
        } catch (Exception e) {
            returnCode = "IS_LOGIN_NAME_AVAILABLE_ERROR";
            returnMessage = e.getMessage();
            logger.error("Error in UserController.isLoginNameAvailable returnMessage: " + returnMessage);
        }
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register", produces = { "application/JSON" })
    public IdVO register(@RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "email") final String email, @RequestParam(value = "password") final String password) {
        logger.info("UserController.register called for loginName " + loginName + " email " + email);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        IdVO answer = new IdVO();
        boolean loginNameValid = true;
        try {
            EmailValidationResult emailValidationResult = validateEmail(email);
            if (emailValidationResult.isValid()) {
                loginNameValid = LoginNameValidator.isValid(loginName);
                if (!loginNameValid) {
                    returnCode = LOGIN_NAME_INVALID_MSG;
                    returnMessage = String.format(LOGIN_NAME_INVALID_MSG + " for loginName: %s", loginName);
                    answer.setReturnCode(returnCode);
                    answer.setReturnMessage(returnMessage);
                    return answer;
                }
            } else {
                answer.setReturnCode(emailValidationResult.getReturnCode());
                answer.setReturnMessage(emailValidationResult.getReturnMessage());
                return answer;
            }

            String resetToken = UUID.randomUUID().toString();
            answer = userMapper.register(loginName, email, password, resetToken);
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            if ("SUCCESS".equals(returnCode)) {
                logger.info("regsitered user with email " + email + " and with loginName " + loginName);
                emailServiceFacade.sendConfirmSignupHTMLEmail(email, resetToken);
            }
            answer.setReturnCode(returnCode);
            answer.setReturnMessage(returnMessage);
        } catch (Exception e) {
            answer.setReturnCode("REGISTER_ERROR");
            answer.setReturnMessage(e.getMessage());
            logger.error("Error in UserController.register returnMessage: " + returnMessage);
        }
        return answer;
    }

    private EmailValidationResult validateEmail(final String email) throws Exception {
        // defaults to valid email, not disposable, scam or spam.
        EmailValidationResult emailValidationResult = new EmailValidationResult();

        if (new EmailValidator().isValid(email)) {
            if (isEmailRegistered(email).getResult().getValue()) {
                emailValidationResult.setValid(false);
                emailValidationResult.setReturnCode(EMAIL_TAKEN);
                emailValidationResult.setReturnCode("Email address provided is already registered or is pending for confirmation.");
                return emailValidationResult;
            } else if (isEmailGmailVariation(email)) {
                emailValidationResult.setValid(true);
                emailValidationResult.setReturnCode(EMAIL_VARIATION_EXIST);
                emailValidationResult.setReturnCode("Email address provided is already registed with a variation that will not be ");
            } else {
                AntideoEmailValiationVO antideoValidationInfo = getAntideoValidationInfo(email);
                if (null != antideoValidationInfo.getError()) {
                    if (antideoValidationInfo.getError().getCode().equals(REACHED_MAXIMUM_ANTIDEO_CALL_CODE)) {
                        logger.error("LetLock has reached maximum Antideo email validation treshhold. Need to increase the limit");
                        // TODO: Send email to admin
                    } else {
                        emailValidationResult.setReturnCode(EMAIL_VALIDATION_ERROR);
                        emailValidationResult.setReturnMessage("validaton code: "
                                + antideoValidationInfo.getError().getCode()
                                + " validation message: "
                                + antideoValidationInfo.getError().getMessage());
                        emailValidationResult.setValid(false);
                    }
                } else {
                    if (antideoValidationInfo.isDisposable()) {
                        emailValidationResult.setDisposable(true);
                        emailValidationResult.setReturnCode(EMAIL_DISPOSABLE);
                        emailValidationResult.setReturnMessage("Email is a disposable email");
                    }
                    if (antideoValidationInfo.isSpam() || antideoValidationInfo.isScam()) {
                        emailValidationResult.setReturnCode(EMAIL_UNRELIABLE);
                        emailValidationResult.setReturnMessage("Email is listed in spam or scam email list");
                        emailValidationResult.setValid(false);
                    }
                }
            }
        } else{
            emailValidationResult.setReturnCode(EMAIL_INVALID);
            emailValidationResult.setReturnMessage(EMAIL_INVALID_MSG);
            emailValidationResult.setValid(false);
        }
        return emailValidationResult;
    }

    private boolean isEmailGmailVariation(final String email) {
        return email.toLowerCase().endsWith("gmail.com") && email.indexOf("..") != -1;
    }

    public AntideoEmailValiationVO getAntideoValidationInfo(String email) throws Exception {

        String url = "http://api.antideo.com/email/" + email;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null)
            result.append(line);

        AntideoEmailValiationVO vo = new Gson().fromJson(result.toString(),
                AntideoEmailValiationVO.class);

        return vo;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resend_signup_email", produces = { "application/JSON" })
    public IdVO resendSignUpConfirmationEmail(
            @RequestParam(value = "loginId") final String loginId, @RequestParam(value = "password") final String password) {
        logger.info("UserController.resend_signup_email called for loginId " + loginId);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        IdVO answer = new IdVO();
        String resetToken = "";
        if (!isLoginCriteriaAnEmail(loginId) && !LoginNameValidator.isValid(loginId)) {
            returnCode = LOGIN_NAME_INVALID;
            returnMessage = LOGIN_NAME_INVALID_MSG;
        } else {
            try {
                ResetTokenResponse resetTokenResponse = getResetToken(loginId, password);

                if (resetTokenResponse != null && resetTokenResponse.getResult() != null) {
                    resetToken = resetTokenResponse.getResult().getResetToken();
                    if (StringUtils.isBlank(resetToken)) {
                        returnCode = "NO_CONFIRMATION_NEEDED";
                        returnMessage = "user is found but no confirmation is needed.";
                    }
                } else {
                    returnCode = "USER_NOT_FOUND";
                    returnMessage = "No user was found for the given liginId and password";
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
            } catch (Exception e) {
                returnCode = "RESEND_SIGNUP_ERROR";
                returnMessage = e.getMessage();
            }
        }
        answer.setReturnCode(returnCode);
        answer.setReturnMessage(returnMessage);

        return answer;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login", produces = { "application/JSON" })
    public SessionTokenResponse login(@RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "password") final String password, HttpServletRequest httpServletRequest) {
        String origin = httpServletRequest.getHeader("origin");
        String ipAddress = httpServletRequest.getRemoteAddr();
        String userAgent = httpServletRequest.getHeader("User-Agent");
        RequestData requestData = new RequestData(ipAddress, userAgent, origin);

        logger.info("Origin: {}, ipAddress: {}, userAgent: {}", origin, ipAddress, userAgent);
        logger.info("UserController.login called for loginName " + loginName);

        String returnCode = "SUCCESS";
        String returnMessage = "";
        String token = "";
        try {
            IdVO answer = userMapper.login(loginName, password, requestData.toJSON());

            long userId = answer.getResult().getId();
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            if (returnCode.equals("SUCCESS")) {
                token = SessionManager.getInstance().generateSessionToken(userId, requestData.getTokenPrefix());
            }
        } catch (Exception e) {
            returnCode = "LOGIN_ERROR";
            returnMessage = e.getMessage();
            logger.error("UserController.login failed for loginId "
                    + loginName
                    + " failed. returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + returnMessage);
        }

        return new SessionTokenResponse(token, returnCode, returnMessage);
    }

    private boolean isLoginCriteriaAnEmail(final String loginNameOrEmail) {
        return new EmailValidator().isValid(loginNameOrEmail);
    }

    @PostMapping(value = "/user/change_password", produces = { "application/JSON" })
    public BooleanResponse changePassword(@RequestParam(value = "token") final String token,
            @RequestParam(value = "loginName") final String loginName, @RequestParam(value = "email") final String email,
            @RequestParam(value = "oldPassword") final String oldPassword, @RequestParam(value = "newPassword") final String newPassword,
            HttpServletRequest httpServletRequest) {
        logger.info("UserController.changePassword called for a user");
        String origin = httpServletRequest.getHeader("origin");
        String ipAddress = httpServletRequest.getRemoteAddr();
        String userAgent = httpServletRequest.getHeader("User-Agent");
        RequestData requestData = new RequestData(ipAddress, userAgent, origin);

        long userId = SessionManager.getInstance().getUserId(token);
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        boolean result = false;
        try {
            if (userId > 0) {
                ReturnCodeMessageResponse answer = userMapper.updateUserPassword(loginName, oldPassword, newPassword, requestData.toJSON());
                returnCode = answer.getReturnCode();
                returnMessage = answer.getReturnMessage();
                if ("SUCCESS".equals(returnCode)) {
                    // TODO: enable after LLW-249 is implemented in sprint 16
                    // emailServiceFacade.sendChangePasswordEmail(email);
                    result = true;
                } else {
                    logger.error("UserController.changePassword failed for email "
                            + email
                            + " failed. returnCode: "
                            + returnCode
                            + " returnMessage: "
                            + returnMessage);
                }
            }
        } catch (Exception e) {
            returnCode = "CHANGE_PASSWORD_ERROR";
            logger.error("UserController.changePassword failed for email "
                    + email
                    + " failed. returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + e.getMessage());
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
    public ReturnCodeMessageResponse handleForgotPassword(@RequestParam(value = "email") final String email) {
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
    public AlgoVO getUserPasswordAlgo(@RequestParam(value = "loginName") final String loginName) {

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
            logger.error("Exception thrown sending email." + e.getMessage());
            returnCode = "FORGOT_PASSWORD_EMAIL_ERROR";
            returnMessage = e.getMessage();
        }
        return new AlgoVO(hashingAlgo, encodingAlgo, returnCode, returnMessage);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/reset_password", produces = { "application/JSON" })
    public BooleanResponse resetPassword(@RequestParam(value = "token") final String token,
            @RequestParam(value = "email") final String email, @RequestParam(value = "newPassword") final String newPassword) {
        logger.info("UserController.resetPassword called for email " + email);
        boolean result = false;
        String returnMessage = "";
        String returnCode = "SUCCESS";
        try {
            BooleanResponse response = userMapper.resetUserPassword(email, token, newPassword);
            result = response.getResult().getValue();
            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
            if (!"SUCCESS".equals(returnCode)) {
                logger.error("resetPassword failed for token " + token + " error code: " + returnCode,
                        " return Message: " + returnMessage);
            }
        } catch (Exception e) {
            returnCode = "RESET_PASSWORD_ERROR";
            returnMessage = e.getMessage();
            logger.error("Exception thrown in resetPassword. returnMessage: " + e.getMessage());
        }
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate_reset_password_token", produces = { "application/JSON" })
    public BooleanResponse validateResetPasswordToken(@RequestParam(value = "token") final String token) throws Exception {
        boolean result = true;
        String returnCode = "SUCCESS";
        String returnMessage = "";
        try {
            logger.info("UserController.validateResetPasswordToken called for validating resetToken");
            BooleanResponse response = userMapper.isPasswordResetTokenValid(token);
            result = response.getResult().getValue();
            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
            if (!"SUCCESS".equals(returnCode)) {
                logger.error("validateResetPasswordToken failed for token " + token + " error code: " + returnCode,
                        " return Message: " + returnMessage);
            }
        } catch (Exception e) {
            returnCode = "VALIDATE_RESET_PASSWORD_ERROR";
            returnMessage = e.getMessage();
            logger.error("validateResetPasswordToken failed for token " + token + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_reset_token", produces = { "application/JSON" })
    public ResetTokenResponse getResetToken(String email, String password) {
        String returnCode = "SUCCESS";
        String returnMessage = "";
        ResetTokenVO userVO = new ResetTokenVO();
        ResetTokenResponse resetTokenResponse = null;
        try {
            userVO = userMapper.getUserResetToken(email, password);
        } catch (Exception e) {
            returnCode = "USER_NOT_FOUND";
            returnMessage = "Cannot find user using email " + email;
            logger.error("UserMapper.getUserObject threw an Exception " + returnMessage);
        }
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("getUserObject failed for email " + email + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }
        resetTokenResponse = new ResetTokenResponse(userVO, returnCode, returnMessage);
        return resetTokenResponse;
    }

    @GetMapping(value = "/user/get_user_profile", produces = { "application/JSON" })
    public UserProfileResponse getUserProfile(@RequestParam(value = "token") final String token) throws Exception {
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        long userId = SessionManager.getInstance().getUserId(token);
        UserVO userVO = new UserVO();
        try {
            if (userId > 0) {
                userVO = userMapper.getUserProfile(userId);
                returnCode = "SUCCESS";
                returnMessage = "";
            }
        } catch (Exception e) {
            returnCode = "ERROR";
            returnMessage = "getUserProfilefailed : " + e.getMessage();
            logger.error("UserMapper.getUserObject threw an Exception " + returnMessage);
        }
        return new UserProfileResponse(userVO, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm_signup", produces = { "application/JSON" })
    public BooleanResponse confirmSignup(@RequestParam(value = "email") final String email,
            @RequestParam(value = "resetToken") final String resetToken) {
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
            } else {
                EmailValidationResult emailValidationResult = validateEmail(email);
                boolean isFreeSignupCreditForEmail = isFreeSignUPCreditForEmail(emailValidationResult);
                if (isFreeSignupCreditForEmail) {
                    IdVO addCreditResponse = userMapper.addFreeTransferCredit(1, email); // TODO: This has to be done on behalf of
                                                                                         // admin/system
                    logger.info("Adding free credits: returnCode: {} returnMessage: {}  orderId: {}", addCreditResponse.getReturnCode(),
                            addCreditResponse.getReturnMessage(), addCreditResponse.getResult().getId());
                }
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

    private boolean isFreeSignUPCreditForEmail(EmailValidationResult emailValidationResult) {
        return emailValidationResult.isValid() && !emailValidationResult.getReturnCode().contentEquals(EMAIL_VARIATION_EXIST) && appsSettingsManager.isFreeSignUpCreditForapps();
    }

    @PostMapping(value = "/user/message", produces = { "application/JSON" })
    public BooleanResponse submitContactUsForm(@Valid @RequestBody ContactUsModel contactUsModel) {
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

    @PostMapping(value = "/user/transferCredit", produces = { "application/JSON" })
    public BooleanResponse addFreeTransferCredit(@RequestParam(value = "token") final String token,
            @RequestParam(value = "customerLoginName") final String customerLoginName) {
        logger.info("UserController.addFreeTransferCredit called for user " + customerLoginName);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        long userId = SessionManager.getInstance().getUserId(token);
        boolean result = false;
        try {
            if (userId > 0 && userId == 1) {// TODO: check for admin role later
                IdVO answer = userMapper.addFreeTransferCredit(userId, customerLoginName);
                returnCode = answer.getReturnCode();
                returnMessage = answer.getReturnMessage();
                if ("SUCCESS".equals(returnCode)) {
                    result = true;
                } else {
                    logger.error("UserController.addFreeTransferCredit failed for logginName "
                            + customerLoginName
                            + " failed. returnCode: "
                            + returnCode
                            + " returnMessage: "
                            + returnMessage);
                }
            } else {
                returnCode = "ADMIN_USER_EXPECTED";
                returnMessage = "Admin user is required to add free credit.";
            }
        } catch (Exception e) {
            returnCode = "ADD_TRANSFER_CREDIT_ERROR";
            logger.error("UserController.addFreeTransferCredit failed for logginName "
                    + customerLoginName
                    + " failed. returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + e.getMessage());
        }
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    private BooleanResponse isEmailRegistered(String email) {
        return userMapper.isEmailRegistered(email);
    }

}
