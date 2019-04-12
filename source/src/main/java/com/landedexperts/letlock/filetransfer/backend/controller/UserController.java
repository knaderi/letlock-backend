package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanAnswer;
import com.landedexperts.letlock.filetransfer.backend.answer.SessionTokenAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdResult;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class UserController {
	@Autowired
	private UserMapper userMapper;

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/user_is_login_name_available",
		produces = {"application/JSON"}
	)
	public BooleanAnswer userIsLoginNameAvailable(
		@RequestParam( value="loginName" ) final String loginName
	) throws Exception
	{
		BooleanAnswer answer = userMapper.isLoginNameAvailable( loginName );

		boolean result = answer.getResult();
		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();

		return new BooleanAnswer(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/register",
		produces = {"application/JSON"}
	)
	public BooleanAnswer register(
		@RequestParam( value="loginName" ) final String loginName,
		@RequestParam( value="password" ) final String password
	) throws Exception
	{
		IdResult answer = userMapper.register(loginName, password);

		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();

		boolean result = errorCode.equals("NO_ERROR");

		return new BooleanAnswer(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/login",
		produces = {"application/JSON"}
	)
	public SessionTokenAnswer login(
		@RequestParam( value="loginName" ) final String loginName,
		@RequestParam( value="password" ) final String password
	) throws Exception
	{
		IdResult answer = userMapper.login(loginName, password);

		int userId = answer.getId();
		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();

		String token = "";
		if(errorCode.equals("NO_ERROR")) {
			token = SessionManager.getInstance().generateSessionToken(userId);
		}

		return new SessionTokenAnswer(token, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/user_change_password",
		produces = {"application/JSON"}
	)
	public BooleanAnswer userChangePassword(
		@RequestParam( value="loginName" ) final String loginName,
		@RequestParam( value="oldPassword" ) final String oldPassword,
		@RequestParam( value="newPassword" ) final String newPassword
	) throws Exception
	{
		ErrorCodeMessageResult answer = userMapper.changePassword( loginName, oldPassword, newPassword );

		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();
		boolean result = errorCode.equals("NO_ERROR");

		return new BooleanAnswer(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/logout",
		produces = {"application/JSON"}
	)
	public BooleanAnswer logout(
			@RequestParam( value="token" ) final String token
	) throws Exception
	{
		SessionManager.getInstance().cleanSession(token);
		return new BooleanAnswer(true, "NO_ERROR", "");
	}
}
