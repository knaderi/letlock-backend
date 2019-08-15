package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.utils.EmailValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.LoginNameValidator;

@RestController
public class UserController {

	
	private static final String LOGIN_NAME_IS_INVALID = "Login name is invalid";
	private static final String INVALID_LOGINNAME = "INVALID_LOGINNAME";
	private static final String EMAIL_IS_INVALID = "Email is invalid";
	private static final String INVALID_EMAIL = "INVALID_EMAIL";
	@Autowired
	private UserMapper userMapper;

	@RequestMapping(method = RequestMethod.POST, value = "/user_is_login_name_available", produces = {
			"application/JSON" })
	public BooleanResponse userIsLoginNameAvailable(@RequestParam(value = "loginName") final String loginName)
			throws Exception {
		BooleanVO answer = userMapper.isLoginNameAvailable(loginName);

		boolean result = answer.getValue();
		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();

		return new BooleanResponse(result, errorCode, errorMessage);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/register", produces = { "application/JSON" })
	public BooleanResponse register(@RequestParam(value = "loginName") final String loginName,
			@RequestParam(value = "email") final String email, @RequestParam(value = "password") final String password)
			throws Exception {

		String errorCode = "";
		String errorMessage = "";
		if (!new EmailValidator().isValid(email)) {
			errorCode = INVALID_EMAIL;
			errorMessage = EMAIL_IS_INVALID;
		} else if (! new LoginNameValidator().isValid(loginName)) {
			errorCode = INVALID_LOGINNAME;
			errorMessage = LOGIN_NAME_IS_INVALID;
		} else {
			IdVO answer = userMapper.register(loginName, email, password);
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		boolean result = errorCode.equals("NO_ERROR");

		return new BooleanResponse(result, errorCode, errorMessage);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/login", produces = { "application/JSON" })
	public SessionTokenResponse login(@RequestParam(value = "loginName") final String loginName,
			@RequestParam(value = "password") final String password) throws Exception {
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

	@RequestMapping(method = RequestMethod.POST, value = "/user_change_password", produces = { "application/JSON" })
	public BooleanResponse userChangePassword(@RequestParam(value = "loginName") final String loginName,
			@RequestParam(value = "oldPassword") final String oldPassword,
			@RequestParam(value = "newPassword") final String newPassword) throws Exception {
		ErrorCodeMessageVO answer = userMapper.changePassword(loginName, oldPassword, newPassword);

		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();
		boolean result = errorCode.equals("NO_ERROR");

		return new BooleanResponse(result, errorCode, errorMessage);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/logout", produces = { "application/JSON" })
	public BooleanResponse logout(@RequestParam(value = "token") final String token) throws Exception {
		SessionManager.getInstance().cleanSession(token);
		return new BooleanResponse(true, "NO_ERROR", "");
	}
}
