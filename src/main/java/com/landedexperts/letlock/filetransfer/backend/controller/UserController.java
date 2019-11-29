package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.service.LetLockEmailService;
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
    private UserMapper userMapper;

    @Autowired
    private LetLockEmailService emailService;

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
        String errorCode = "";
        String errorMessage = "";
        if (!new EmailValidator().isValid(email)) {
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

    @RequestMapping(method = RequestMethod.POST, value = "/update_user_password", produces = { "application/JSON" })
    public BooleanResponse updateUserPassword(@RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "oldPassword") final String oldPassword, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {
        logger.info("UserController.updateUserPassword called for loginName " + loginName);
        ErrorCodeMessageResponse answer = userMapper.updateUserPassword(loginName, oldPassword, newPassword);

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
    public BooleanResponse forgotPassword(@RequestParam(value = "email") final String email) throws Exception {
        logger.info("UserController.forgotPassword called for email " + email);
        BooleanVO answer = userMapper.isEmailRegistered(email);
        if (answer.getValue()) {
            emailService.sendForgotPasswordEmail(email);
            return new BooleanResponse(true, "NO_ERROR", "");
        } else {
            String errorCode = answer.getErrorCode();
            String errorMessage = answer.getErrorMessage();
            boolean result = answer.getValue();

            return new BooleanResponse(result, errorCode, errorMessage);

        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/reset_password", produces = { "application/JSON" })
    public BooleanResponse resetPassword(@RequestParam(value = "token") final String token,
            @RequestParam(value = "loginName") final String loginName, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {

        logger.info("UserController.resetPassword called for email " + loginName);
        if (!"123456".equals(token)) {
            return new BooleanResponse(false, "INVALID_RESET_PASSWORD_TOKEN", "Token is invalid.");
        } else {           
            BooleanResponse response = updateUserPassword(loginName, "passw0rd!", "passw0rd!");
//            if(response.getErrorCode().equals("NO_ERROR")) {
//              emailService.sendForgotPasswordEmail(email);
//            }
            return response;
        
        }        

    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate_reset_password_token", produces = { "application/JSON" })
    public BooleanResponse validateResetPasswordToken(@RequestParam(value = "token") final String token) throws Exception {

        if ("123456".equals(token)) {
            return new BooleanResponse(true, "NO_ERROR", "");
        } else {
            return new BooleanResponse(false, "INVALID_RESET_PASSWORD_TOKEN", "Token is invalid.");
        }

    }

}
