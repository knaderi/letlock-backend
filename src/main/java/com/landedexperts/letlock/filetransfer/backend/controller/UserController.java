package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.jpa.UserDTO;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.types.UserStatusType;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.service.UserService;
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
    private UserService userService; // using JPA

    
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
                String encodedPassword = encode(password);
                IdVO answer = userMapper.register(loginName, email, encodedPassword);
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
    public SessionTokenResponse login(@RequestParam(value = "loginName") final String loginNameOrEmail,
            @RequestParam(value = "password") final String password) throws Exception {
        logger.info("UserController.login called for loginName " + loginNameOrEmail);
        Optional<UserDTO> userContainer = null;
        if(isLoginCriteriaAnEmail(loginNameOrEmail)) {
           userContainer = userService.findUserByEmail(loginNameOrEmail);
        }else {
            userContainer = userService.findUserByLoginName(loginNameOrEmail);
        }
        String token = "";
        if (userContainer.isPresent() && userContainer.get().getStatus()== UserStatusType.active) {
            long userId = userContainer.get().getId();
            if(compareStringWithEncodedValues(password,userContainer.get().getPassword())) {
                token = SessionManager.getInstance().generateSessionToken(userId);
                UserDTO updatedUser = userContainer.get();
                updatedUser.setLastLogin(getSQLTime());
                updatedUser.setUpdatedDate(getSQLTime());
                userService.save(updatedUser);
                return new SessionTokenResponse(token, "NO_ERROR", "");
            }else {
                String errorCode = "WRONG_PASSWORD";
                String errorMessage = "The entered password is incorrect";
                return new SessionTokenResponse(token, errorCode, errorMessage);
            }
        }else {
            String errorCode = "USER_NOT_FOUND";
            String errorMessage = "User with given email address does not exist";
            return new SessionTokenResponse(token, errorCode, errorMessage);
        }
        
    }

    private boolean isLoginCriteriaAnEmail(final String loginNameOrEmail) {
        return new EmailValidator().isValid(loginNameOrEmail);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_user_password", produces = { "application/JSON" })
    public BooleanResponse updateUserPassword(@RequestParam(value = "loginName") final String loginNameOrEmail,
            @RequestParam(value = "oldPassword") final String oldPassword, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {
        
        logger.info("UserController.login called for loginName " + loginNameOrEmail);
        Optional<UserDTO> userContainer = null;
        if(isLoginCriteriaAnEmail(loginNameOrEmail)) {
           userContainer = userService.findUserByEmail(loginNameOrEmail);
        }else {
            userContainer = userService.findUserByLoginName(loginNameOrEmail);
        }
        if (userContainer.isPresent() && userContainer.get().getStatus()== UserStatusType.active) {
            if(compareStringWithEncodedValues(oldPassword,userContainer.get().getPassword())) {
                UserDTO updatedUser = userContainer.get();
                updatedUser.setPassword(encode(newPassword));
                updatedUser.setUpdatedDate(getSQLTime());
                userService.save(updatedUser);
                return new BooleanResponse(true, "NO_ERROR", "");
            }else {
                String errorCode = "WRONG_PASSWORD";
                String errorMessage = "The entered password is incorrect";
                return new BooleanResponse(false, errorCode, errorMessage);
            }
        }else {
            String errorCode = "USER_NOT_FOUND";
            String errorMessage = "User with given email address does not exist";
            return new BooleanResponse(false, errorCode, errorMessage);
        }
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
    public BooleanResponse forgotPassword(@RequestParam(value = "email") final String recipientEmailAddress) throws Exception {
        logger.info("UserController.forgotPassword called for email " + recipientEmailAddress);
        Optional<UserDTO> userContainer = userService.findUserByEmailAndStatus(recipientEmailAddress, UserStatusType.active);

        if (userContainer.isPresent() && userContainer.get().getStatus()== UserStatusType.active) {
            String newToken = UUID.randomUUID().toString();
            userContainer.get().setResetToken(newToken);
            // Save token to database
            userService.save(userContainer.get());
            //letLockEmailService.sendForgotPasswordEmail(recipientEmailAddress, newToken);
            emailServiceFacade.sendForgotPasswordEmail(recipientEmailAddress, newToken);
            return new BooleanResponse(true, "NO_ERROR", "");
        } else {
            String errorCode = "USER_NOT_FOUND";
            String errorMessage = "User with given email address does not exist";
            boolean result = false;
            return new BooleanResponse(result, errorCode, errorMessage);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/reset_password", produces = { "application/JSON" })
    public BooleanResponse resetPassword(@RequestParam(value = "token") final String token,
            @RequestParam(value = "loginName") final String loginName, @RequestParam(value = "newPassword") final String newPassword)
            throws Exception {

        logger.info("UserController.resetPassword called for email " + loginName);

        Optional<UserDTO> user = userService.findUserByResetToken(token);
        String encodedNewPassword =  encode(newPassword);
        if (user.isPresent()) {
            UserDTO resetUser = user.get();
            resetUser.setPassword(encodedNewPassword);
            resetUser.setResetToken(null);
            userService.save(resetUser);
            return new BooleanResponse(true, "NO_ERROR", "");

        } else {
            return new BooleanResponse(false, "INVALID_RESET_PASSWORD_TOKEN", "Token is invalid.");

        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate_reset_password_token", produces = { "application/JSON" })
    public BooleanResponse validateResetPasswordToken(@RequestParam(value = "token") final String token) throws Exception {

        logger.info("UserController.validateResetPasswordToken called");

        Optional<UserDTO> user = userService.findUserByResetToken(token);

        if (user.isPresent()) {
            return new BooleanResponse(true, "NO_ERROR", ""); 
        }else {
            return new BooleanResponse(false, "INVALID_RESET_PASSWORD_TOKEN", "Token is invalid.");
        }
    }
    
    private String encode(String value) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16); // Strength set as 16
        return encoder.encode(value);
    }
    
    private boolean compareStringWithEncodedValues(String unencodedString, String encodedString) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.matches(unencodedString, encodedString);
        
    }
    
    private java.sql.Timestamp getSQLTime() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        return new java.sql.Timestamp(now.getTime());        
    }
    
    
}
