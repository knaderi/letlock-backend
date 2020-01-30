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
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrdersInfoResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AlgoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderLineItemVO;
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
    private static final String INVALID_EMAIL = "INVALID_EMAIL";
    @Autowired
    private UserMapper userMapper;

    @Autowired
    EmailServiceFacade emailServiceFacade;

    @RequestMapping(method = RequestMethod.POST, value = "/user_is_login_name_available", produces = { "application/JSON" })
    public BooleanResponse isLoginNameAvailable(@RequestParam(value = "loginName") final String loginName) throws Exception {
        logger.info("UserController.isLoginNameAvailable called for loginName " + loginName);
        BooleanVO answer = userMapper.isLoginNameAvailable(loginName);

        boolean result = answer.getValue();
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register", produces = { "application/JSON" })
    public IdVO register(@RequestParam(value = "loginName") final String loginName,
            @RequestParam(value = "email") final String email, @RequestParam(value = "password") final String password) throws Exception {
        logger.info("UserController.register called for loginName " + loginName);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        IdVO answer = new IdVO();
        if (!isLoginCriteriaAnEmail(email)) {
            returnCode = INVALID_EMAIL;
            returnMessage = EMAIL_IS_INVALID;
        } else if (!new LoginNameValidator().isValid(loginName)) {
            returnCode = INVALID_LOGINNAME;
            returnMessage = LOGIN_NAME_IS_INVALID;
        } else {
            logger.info("****************Calling register on db side");
            try {
                answer = userMapper.register(loginName, email, password);
                returnCode = answer.getReturnCode();
                returnMessage = answer.getReturnMessage();
            } catch (Exception e) {
                e.printStackTrace();
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

        long userId = answer.getId();
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

    @RequestMapping(method = RequestMethod.POST, value = "/update_user_password", produces = { "application/JSON" })
    public BooleanResponse updateUserPassword(@RequestParam(value = "loginName") final String loginNameOrEmail,
            @RequestParam(value = "oldPassword") final String oldPassword, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {

        logger.info("UserController.updateUserPassword called for loginName " + loginNameOrEmail);
        ReturnCodeMessageResponse answer = userMapper.updateUserPassword(loginNameOrEmail, oldPassword, newPassword);

        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        boolean result = returnCode.equals("SUCCESS");

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout", produces = { "application/JSON" })
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
    public BooleanResponse handleForgotPassword(@RequestParam(value = "email") final String email) throws Exception {
        logger.info("UserController.forgotPassword called for email " + email);
        boolean result = false;
        String returnCode = "SUCCESS";
        String returnMessage = "";
        String resetToken = "";
        try {
            resetToken = UUID.randomUUID().toString();

            BooleanVO response = userMapper.handleForgotPassword(email, resetToken);

            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
            if ("SUCCESS".equals(returnCode)) {
                result = true;

            }
            if ("SUCCESS".equals(returnCode)) {
                emailServiceFacade.sendForgotPasswordHTMLEmail(email, resetToken);
            }
        } catch (Exception e) {
            result = false;
            logger.error("Exception thrown sening email." + e.getMessage());
            returnCode = "FORGOT_PASSWORD_EMAIL_ERROR";
            returnMessage = e.getMessage();
        }
        return new BooleanResponse(result, returnCode, returnMessage);

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
        BooleanVO response = userMapper.resetUserPassword(email, token, newPassword);
        boolean result = response.getValue();
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate_reset_password_token", produces = { "application/JSON" })
    public BooleanResponse validateResetPasswordToken(@RequestParam(value = "token") final String token) throws Exception {

        logger.info("UserController.validateResetPasswordToken called for validating resetToken");
        BooleanVO response = userMapper.isPasswordResetTokenValid(token);
        boolean result = response.getValue();
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_user_object", produces = { "application/JSON" })
    public UserVO getUserObject(String email) {
        String returnCode = "SUCCESS";
        String returnMessage = "";
        UserVO response = new UserVO();
        try {
            response = userMapper.getUserObject(email);

        } catch (Exception e) {
            logger.error("UserMapper.getUserObject threw an Exception " + e.getMessage());
            returnCode = "USER_NOT_FOUND";
            returnMessage = "Cannot find user using email " + email;
        }
        response.setReturnCode(returnCode);
        response.setReturnMessage(returnMessage);
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_orders_for_user", produces = {
            "application/JSON" })
    public OrdersInfoResponse getFileTransferSessionsForUser(
            @RequestParam(value = "token") final String token) throws Exception {
        logger.info("FileTransferController.getFileTransferSessionsForUser called for token " + token + "\n");

        OrderLineItemVO[] value = null;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            value = userMapper.getUserOrders(userId);
            returnCode = "SUCCESS";
            returnMessage = "";
        }

        return new OrdersInfoResponse(value, returnCode, returnMessage);
    }

}
