/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.USER_ID;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ResetTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.LoginResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.UserProfileResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AlgoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.LoginVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.ResetTokenVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;
import com.landedexperts.letlock.filetransfer.backend.session.AppSettingsManager;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.session.TwoFAManager;
import com.landedexperts.letlock.filetransfer.backend.utils.AntideoEmailValiationVO;
import com.landedexperts.letlock.filetransfer.backend.utils.EmailValidationResult;
import com.landedexperts.letlock.filetransfer.backend.utils.EmailValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.LoginNameValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.RequestData;

@RestController
public class UserController extends BaseController {

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

    @PostMapping(value = "/user_is_login_name_available", produces = { "application/JSON" })
    public BooleanResponse isLoginNameAvailable(
            @RequestParam(value = "loginName") final String loginName) throws Exception{
        logger.info("UserController.isLoginNameAvailable called for loginName " + loginName);

        BooleanResponse response = userMapper.isLoginNameAvailable(loginName);
        return response;
    }

    @PostMapping(value = "/register", produces = { "application/JSON" })
    public IdVO register(
            @RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "email") final String email,
            @RequestParam(value = "password") final String password) throws Exception {
        logger.info("UserController.register called for loginName " + loginName + " email " + email);
        IdVO answer = new IdVO();
        boolean loginNameValid = false;
        
        EmailValidationResult emailValidationResult = validateEmail(email);
        if (emailValidationResult.isValid()) {
            loginNameValid = LoginNameValidator.isValid(loginName);
            if (!loginNameValid) {
                answer.setReturnCode(LOGIN_NAME_INVALID_MSG);
                answer.setReturnMessage(String.format(LOGIN_NAME_INVALID_MSG + " for loginName: %s", loginName));
                return answer;
            }
        } else {
            answer.setReturnCode(emailValidationResult.getReturnCode());
            answer.setReturnMessage(emailValidationResult.getReturnMessage());
            return answer;
        }

        String resetToken = UUID.randomUUID().toString();
        answer = userMapper.register(loginName, email, password, resetToken);

        if ("SUCCESS".equals(answer.getReturnCode())) {
            logger.info("regsitered user with email " + email + " and with loginName " + loginName);
            try {
                emailServiceFacade.sendConfirmSignupHTMLEmail(email, resetToken);
            } catch (Exception e) {
                logger.error("sendConfirmSignupHTMLEmail call failed for email " + email);
            }
            try {
                handleFreeCredit(email, emailValidationResult);
            } catch (Exception e) {
                logger.error("addFreeTransferCredit call failed for email " + email);
            }
        }

        return answer;
    }

    @PostMapping(value = "/register/viapartner", produces = { "application/JSON" })
    public IdVO registerViaPartner(
            @RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "email") final String email, @RequestParam(value = "password") final String password,
            @RequestParam(value = "redeemCode") final String redeemCode,
            @RequestParam(value = "partnerName") final String partnerName) throws Exception {
        logger.info("UserController.register called for loginName " + loginName + " email " + email);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        IdVO answer = new IdVO();
        boolean loginNameValid = true;
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
        BooleanResponse isRedeemCodeValid = userMapper.isRedeemCodeValid(redeemCode, partnerName);
        if (!isRedeemCodeValid.getResult().getValue()) {
            answer.setReturnCode(isRedeemCodeValid.getReturnCode());
            answer.setReturnMessage(isRedeemCodeValid.getReturnMessage());
            return answer;
        }

        String resetToken = UUID.randomUUID().toString();
        answer = userMapper.registerViaPartner(loginName, email, password, resetToken, redeemCode, partnerName);

        if ("SUCCESS".equals(answer.getReturnCode())) {
            logger.info("regsitered user with email " + email + " and with loginName " + loginName);
            emailServiceFacade.sendConfirmViaPartnerSignupHTMLEmail(email, resetToken, partnerName);
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
                emailValidationResult.setReturnMessage("Email address provided is already registed with a variation that will not be ");
            } else {

                // disabled Anideo email check as it makes the system too slow.
//                AntideoEmailValiationVO antideoValidationInfo = getAntideoValidationInfo(email);
//                if (null != antideoValidationInfo.getError()) {
//                    if (antideoValidationInfo.getError().getCode().equals(REACHED_MAXIMUM_ANTIDEO_CALL_CODE)) {
//                        logger.error("LetLock has reached maximum Antideo email validation treshhold. Need to increase the limit");
//                        // TODO: Send email to admin
//                    } else {
//                        emailValidationResult.setReturnCode(EMAIL_VALIDATION_ERROR);
//                        emailValidationResult.setReturnMessage("validaton code: "
//                                + antideoValidationInfo.getError().getCode()
//                                + " validation message: "
//                                + antideoValidationInfo.getError().getMessage());
//                        emailValidationResult.setValid(false);
//                    }
//                } else {
//                    if (antideoValidationInfo.isDisposable()) {
//                        emailValidationResult.setDisposable(true);
//                        emailValidationResult.setReturnCode(EMAIL_DISPOSABLE);
//                        emailValidationResult.setReturnMessage("Email is a disposable email");
//                    }
//                    if (antideoValidationInfo.isSpam() || antideoValidationInfo.isScam()) {
//                        emailValidationResult.setReturnCode(EMAIL_UNRELIABLE);
//                        emailValidationResult.setReturnMessage("Email is listed in spam or scam email list");
//                        emailValidationResult.setValid(false);
//                    }
//                }
            }
        } else {
            emailValidationResult.setReturnCode(EMAIL_INVALID);
            emailValidationResult.setReturnMessage(EMAIL_INVALID_MSG);
            emailValidationResult.setValid(false);
        }
        return emailValidationResult;
    }

    private boolean isEmailGmailVariation(final String email) {
        return email.toLowerCase().endsWith("gmail.com") && (email.indexOf("..") != -1 || email.indexOf("+") != -1);
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

    @PostMapping(value = "/resend_signup_email", produces = { "application/JSON" })
    public IdVO resendSignUpConfirmationEmail(
            @RequestParam(value = "loginId") final String email,
            @RequestParam(value = "password") final String password) throws Exception {
        logger.info("UserController.resend_signup_email called for loginId " + email);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        IdVO answer = new IdVO();
        String resetToken = "";
        if (!isLoginCriteriaAnEmail(email)) {
            returnCode = LOGIN_NAME_INVALID;
            returnMessage = LOGIN_NAME_INVALID_MSG;
        } else {
            ResetTokenResponse resetTokenResponse = getResetToken(email, password);

            if (resetTokenResponse != null && resetTokenResponse.getResult() != null) {
                resetToken = resetTokenResponse.getResult().getResetToken();
                if (StringUtils.isBlank(resetToken)) {
                    returnCode = "NO_CONFIRMATION_NEEDED";
                    returnMessage = "user is found but no confirmation is needed.";
                }
            } else {
                returnCode = "USER_NOT_FOUND";
                returnMessage = "No user was found for the given loginId and password";
            }

            if ("SUCCESS".equals(returnCode)) {
                emailServiceFacade.sendConfirmSignupHTMLEmail(email, resetToken);
            } else {
                logger.error("UserController.resendSignUpConfirmationEmail failed for loginId "
                        + email
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

    @PostMapping(value = "/login", produces = { "application/JSON" })
    public LoginResponse login(
            @RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "password") final String password,
            HttpServletRequest request) throws Exception {
        RequestData requestData = new RequestData(request);
        logger.info("UserController.login called for loginName " + loginName);

        LoginVO answer = userMapper.login(loginName, password, requestData.toJSON());
        long userId = answer.getResult().getId();
        boolean twoFARequired = answer.getResult().getTwoFARequired();
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        String token = "";

        if (returnCode.equals("SUCCESS")) {
            if (twoFARequired) {
                token = TwoFAManager.getInstance().generateAuthToken(userId);
            } else {
                token = SessionManager.getInstance().generateSessionToken(userId, requestData.getTokenPrefix());
            }
        }

        return new LoginResponse(token, twoFARequired, 0, returnCode, returnMessage);
    }

    private boolean isLoginCriteriaAnEmail(final String loginNameOrEmail) {
        return new EmailValidator().isValid(loginNameOrEmail);
    }

    @PostMapping(value = "/user/change_password", produces = { "application/JSON" })
    public BooleanResponse changePassword(
            @RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "email") final String email,
            @RequestParam(value = "oldPassword") final String oldPassword,
            @RequestParam(value = "newPassword") final String newPassword,
            HttpServletRequest request) throws Exception {
        logger.info("UserController.changePassword called for a user");
        RequestData requestData = new RequestData(request);

        boolean result = false;
        ReturnCodeMessageResponse answer = userMapper.updateUserPassword(
                loginName, email, oldPassword, newPassword, requestData.toJSON());
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        if ("SUCCESS".equals(returnCode)) {
            try {
                emailServiceFacade.sendChangePasswordEmail(email);
            } catch (Exception e) {
            }
            result = true;
        } else {
            logger.error("UserController.changePassword for email "
                    + email
                    + " failed. returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + returnMessage);
        }

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @PostMapping(value = "/user/logout", produces = { "application/JSON" })
    public BooleanResponse logout(HttpServletRequest request) throws Exception {
        
        String token = getToken(request);
        logger.info("UserController.logout called for token " + token);

        SessionManager instance = SessionManager.getInstance();
        if (instance.isActive(token)) {
            instance.cleanSession(token);
            return new BooleanResponse(true, "SUCCESS", "");
        } else {
            return new BooleanResponse(false, "LOGIN_SESSION_NOT_FOUND", "There is no active login session for the given token");
        }

    }

    @PostMapping(value = "/handle_forgot_password", produces = { "application/JSON" })
    public ReturnCodeMessageResponse handleForgotPassword(
            @RequestParam(value = "email") final String email) throws Exception {
        logger.info("UserController.forgotPassword called for email " + email);

        String resetToken = UUID.randomUUID().toString();
        ReturnCodeMessageResponse response = userMapper.handleForgotPassword(email, resetToken);

        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();

        if ("SUCCESS".equals(returnCode)) {
            logger.info("handle_forgot_password email: " + email + ", resetToken: " + resetToken);
            emailServiceFacade.sendForgotPasswordHTMLEmail(email, resetToken);
        } else {
            logger.error("handle_forgot_password failed for: " + email + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }

        return new ReturnCodeMessageResponse(returnCode, returnMessage);

    }

    @PostMapping(value = "/getUserPasswordAlgo", produces = { "application/JSON" })
    public AlgoVO getUserPasswordAlgo(
            @RequestParam(value = "loginName") final String loginName) throws Exception {

        AlgoVO response = userMapper.getUserPasswordAlgo(loginName);
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        String encodingAlgo = "";
        String hashingAlgo = "";

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
        return new AlgoVO(hashingAlgo, encodingAlgo, returnCode, returnMessage);
    }

    @PostMapping(value = "/reset_password", produces = { "application/JSON" })
    public BooleanResponse resetPassword(
            @RequestParam(value = "token") final String token,
            @RequestParam(value = "email") final String email,
            @RequestParam(value = "newPassword") final String newPassword) throws Exception {
        logger.info("UserController.resetPassword called for email " + email);

        String decoded_email = java.net.URLDecoder.decode(email, StandardCharsets.UTF_8.name());
        BooleanResponse response = userMapper.resetUserPassword(decoded_email, token, newPassword);
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("resetPassword failed for token " + token + " return code: " + returnCode,
                    " return Message: " + returnMessage);
        }

        return response;
    }

    @PostMapping(value = "/validate_reset_password_token", produces = { "application/JSON" })
    public BooleanResponse validateResetPasswordToken(
            @RequestParam(value = "token") final String token) throws Exception {
        logger.info("UserController.validateResetPasswordToken called for token " + token);

        BooleanResponse response = userMapper.isPasswordResetTokenValid(token);
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("validateResetPasswordToken failed for token " + token + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }

        return response;
    }

    @PostMapping(value = "/get_reset_token", produces = { "application/JSON" })
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
        }
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("getUserResetToken failed for email " + email + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        }
        resetTokenResponse = new ResetTokenResponse(userVO, returnCode, returnMessage);
        return resetTokenResponse;
    }

    @GetMapping(value = "/user/get_user_profile", produces = { "application/JSON" })
    public UserProfileResponse getUserProfile(HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        UserVO userVO = userMapper.getUserProfile(userId);
        return new UserProfileResponse(userVO, "SUCCESS", "");
    }

    @PostMapping(value = "/confirm_signup", produces = { "application/JSON" })
    public BooleanResponse confirmSignup(
            @RequestParam(value = "email") final String email,
            @RequestParam(value = "resetToken") final String resetToken,
            HttpServletRequest request) throws Exception {
        logger.info("UserController.confirm_signup called for email " + email);

        RequestData requestData = new RequestData(request);
        BooleanResponse response = userMapper.confirmSignup(email, resetToken);
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("confirmSignup failed for email " + email + " error code: " + returnCode,
                    " return Message: " + returnMessage);
        } else {
            //disabled for now as we are not doing any antideo email validation
            //EmailValidationResult emailValidationResult = validateEmail(email);
            // handleFreeCredit(email, emailValidationResult);
            try {
                emailServiceFacade.sendAdminRegistrationNotification(email, requestData);
            } catch(Exception e) {
                logger.error("sendAdminRegistrationNotification failed for email " + email + ".",
                        " Error Message: " + e.getMessage());
            }
        }
        return response;
    }

    private void handleFreeCredit(final String email, EmailValidationResult emailValidationResult) throws Exception {
        boolean isFreeSignupCreditForEmail = isFreeSignUPCreditForEmail(emailValidationResult);
        if (isFreeSignupCreditForEmail) {
            IdVO addCreditResponse = userMapper.addFreeTransferCredit(1, email); // TODO: This has to be done on behalf of
            if ("SUCCESS".contentEquals(addCreditResponse.getReturnCode())) { // admin/system
                try {
                    logger.info("confirm signup free credit email: " + email);
                    emailServiceFacade.sendWelcomeWithFreeCreditEmail(email);
                } catch (Exception e) {
                    logger.error("sendWelcomeWithFreeCreditEmail call failed for email " + email);
                }
            }
            logger.info("Adding free credits: returnCode: {} returnMessage: {}  orderId: {}", addCreditResponse.getReturnCode(),
                    addCreditResponse.getReturnMessage(), addCreditResponse.getResult().getId());
        }
    }

    @GetMapping(value = "/validate_email", produces = { "application/JSON" })
    public JsonResponse<EmailValidationResult> emailFullValidate(
            @RequestParam(value = "email") final String email) throws Exception {
        logger.info("UserController.emailFullValidate called for email " + email);
        EmailValidationResult emailValidationResult = validateEmail(email);

        return new JsonResponse<EmailValidationResult>(emailValidationResult, "SUCCESS", "");
    }

    private boolean isFreeSignUPCreditForEmail(EmailValidationResult emailValidationResult) {
        return emailValidationResult.isValid()
                && !emailValidationResult.getReturnCode().contentEquals(EMAIL_VARIATION_EXIST)
                && appsSettingsManager.isFreeSignUpCreditForapps();
    }

    @PostMapping(value = "/user/message", produces = { "application/JSON" })
    public BooleanResponse submitContactUsForm(@Valid @RequestBody ContactUsModel contactUsModel) {
        logger.info("UserController.submitContactUsForm called for ContactUsModel " + contactUsModel);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        try {
            String validaionMessage = contactUsModel.validate();
            if (validaionMessage.equals(ContactUsModel.VALID_MSG)) {
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
        return new BooleanResponse("SUCCESS".equals(returnCode), returnCode, returnMessage);
    }

    @PostMapping(value = "/user/transferCredit", produces = { "application/JSON" })
    public BooleanResponse addFreeTransferCredit(
            @RequestParam(value = "customerLoginName") final String customerLoginName,
            HttpServletRequest request) throws Exception{
        logger.info("UserController.addFreeTransferCredit called for user " + customerLoginName);
        long userId = (long) request.getAttribute(USER_ID);
        IdVO answer = userMapper.addFreeTransferCredit(userId, customerLoginName);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        if (!"SUCCESS".equals(returnCode)) {
            logger.error("UserController.addFreeTransferCredit for loginName "
                    + customerLoginName
                    + " failed. returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + returnMessage);
        }
        return new BooleanResponse("SUCCESS".equals(returnCode), returnCode, returnMessage);
    }

    private BooleanResponse isEmailRegistered(String email) {
        return userMapper.isEmailRegistered(email);
    }

}
