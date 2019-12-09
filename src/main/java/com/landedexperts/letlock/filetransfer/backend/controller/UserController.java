package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ForgotPasswordResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.utils.EmailValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.LoginNameValidator;

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String LOGIN_NAME_IS_INVALID = "Login name is invalid";
    private static final String INVALID_LOGINNAME = "INVALID_LOGINNAME";
    private static final String EMAIL_IS_INVALID = "Email is invalid";
    private static final String INVALID_EMAIL = "INVALID_EMAIL";
    @Autowired
    private UserMapper userMapper; // using mybatis

    @Autowired
    EmailServiceFacade emailServiceFacade;

    @RequestMapping(method = RequestMethod.POST, value = "/user_is_login_name_available", produces = { "application/JSON" })
    public BooleanResponse isLoginNameAvailable(@RequestParam(value = "loginName") final String loginName) throws Exception {
        logger.info("UserController.isLoginNameAvailable called for loginName " + loginName);
        BooleanVO answer = userMapper.isLoginNameAvailable(loginName);

        boolean result = answer.getValue();
        String errorCode = answer.getErrorCode();
        String errorMessage = answer.getErrorMessage();

        return new BooleanResponse(result, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register", produces = { "application/JSON" })
    public BooleanResponse register(@RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "email") final String email, @RequestParam(value = "password") final String password) throws Exception {
        logger.info("UserController.register called for loginName " + loginName);
        String errorCode = "NO_ERROR";
        String errorMessage = "";
        if (!isLoginCriteriaAnEmail(email)) {
            errorCode = INVALID_EMAIL;
            errorMessage = EMAIL_IS_INVALID;
        } else if (!new LoginNameValidator().isValid(loginName)) {
            errorCode = INVALID_LOGINNAME;
            errorMessage = LOGIN_NAME_IS_INVALID;
        } else {
            logger.info("****************Calling register on db side");
            try {
                IdVO answer = userMapper.register(loginName, email, password);
                errorCode = answer.getErrorCode();
                errorMessage = answer.getErrorMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean result = errorCode.equals("NO_ERROR");

        return new BooleanResponse(result, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login", produces = { "application/JSON" })
    public SessionTokenResponse login(@RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "password") final String password) throws Exception {
        logger.info("UserController.login called for loginName " + loginName);
        IdVO answer = userMapper.login(loginName, password);

        int userId = answer.getId();
        String errorCode = answer.getErrorCode();
        String errorMessage = answer.getErrorMessage();

        String token = "";
        if (errorCode.equals("NO_ERROR")) {
            token = SessionManager.getInstance().generateSessionToken(userId);
        }

        return new SessionTokenResponse(token, errorCode, errorMessage);
    }

    private boolean isLoginCriteriaAnEmail(final String loginNameOrEmail) {
        return new EmailValidator().isValid(loginNameOrEmail);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_user_password", produces = { "application/JSON" })
    public BooleanResponse updateUserPassword(@RequestParam(value = "loginName") final String loginNameOrEmail,
            @RequestParam(value = "oldPassword") final String oldPassword, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {

        logger.info("UserController.updateUserPassword called for loginName " + loginNameOrEmail);
        ErrorCodeMessageResponse answer = userMapper.updateUserPassword(loginNameOrEmail, oldPassword, newPassword);

        String errorCode = answer.getErrorCode();
        String errorMessage = answer.getErrorMessage();
        boolean result = errorCode.equals("NO_ERROR");

        return new BooleanResponse(result, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout", produces = { "application/JSON" })
    public BooleanResponse logout(@RequestParam(value = "token") final String token) throws Exception {
        logger.info("UserController.logout called for token " + token);

        SessionManager instance = SessionManager.getInstance();
        if (instance.isActive(token)) {
            instance.cleanSession(token);
            return new BooleanResponse(true, "NO_ERROR", "");
        } else {
            return new BooleanResponse(false, "LOGIN_SESSION_NOT_FOUND", "There is no active login session for the given token");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/handle_forgot_password_request", produces = { "application/JSON" })
    public ForgotPasswordResponse forgotPassword(@RequestParam(value = "email") final String email) throws Exception {
        logger.info("UserController.forgotPassword called for email " + email);
        boolean result = false;
        String errorCode = "NO_ERROR";
        String errorMessage = "";
        String resetToken = "";
        try {
            resetToken = UUID.randomUUID().toString();

            BooleanVO response = userMapper.handleForgotPassword(email, resetToken);

            result = response.getValue();
            errorCode = response.getErrorCode();
            errorMessage = response.getErrorMessage();

            if ("NO_ERROR".equals(errorCode)) {
                emailServiceFacade.sendForgotPasswordEmail(email, resetToken);
            }
        } catch (Exception e) {
            logger.error("Exception thrown sening email." + e.getMessage());
            result = false;
            errorCode = "FORGOT_PASSWORD_EMAIL_ERROR";
            errorMessage = e.getMessage();
        }
        return new ForgotPasswordResponse(resetToken, errorCode, errorMessage);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/reset_password", produces = { "application/JSON" })
    public BooleanResponse resetPassword(@RequestParam(value = "token") final String token,
            @RequestParam(value = "email") final String email, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {
        logger.info("UserController.resetPassword called for email " + email);       
        BooleanVO response =  userMapper.resetUserPassword(email, token, newPassword);
        boolean result = response.getValue();
        String errorCode = response.getErrorCode();
        String errorMessage = response.getErrorMessage();
        return new BooleanResponse(result, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate_reset_password_token", produces = { "application/JSON" })
    public BooleanResponse validateResetPasswordToken(@RequestParam(value = "loginName") final String loginName, @RequestParam(value = "token") final String token) throws Exception {

        logger.info("UserController.validateResetPasswordToken called for loginName " + loginName);       
        BooleanVO response =  userMapper.isPasswordResetTokenValid(loginName, token);
        boolean result = response.getValue();
        String errorCode = response.getErrorCode();
        String errorMessage = response.getErrorMessage();
        return new BooleanResponse(result, errorCode, errorMessage);
    }


}
