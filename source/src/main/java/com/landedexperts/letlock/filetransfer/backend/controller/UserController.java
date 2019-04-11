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
import com.landedexperts.letlock.filetransfer.backend.database.result.LoginResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.RegisterResult;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class UserController {
	@Autowired
	private UserMapper userMapper;

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/register",
		produces = {"application/JSON"}
	)
	public BooleanAnswer register(
		@RequestParam( value="loginName" ) String loginName,
		@RequestParam( value="password" ) String password
	) throws Exception
	{
		RegisterResult answer = userMapper.register(loginName, password);

		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();

		Boolean result = errorCode.equals("NO_ERROR");

		return new BooleanAnswer(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/login",
		produces = {"application/JSON"}
	)
	public SessionTokenAnswer login(
		@RequestParam( value="loginName" ) String loginName,
		@RequestParam( value="password" ) String password
	) throws Exception
	{
		LoginResult answer = userMapper.login(loginName, password);

		Integer userId = answer.getUserId();
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
		@RequestParam( value="loginName" ) String loginName,
		@RequestParam( value="oldPassword" ) String oldPassword,
		@RequestParam( value="newPassword" ) String newPassword
	) throws Exception
	{
		ErrorCodeMessageResult answer = userMapper.changePassword( loginName, oldPassword, newPassword );

		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();
		Boolean result = errorCode.equals("NO_ERROR");

		return new BooleanAnswer(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/logout",
		produces = {"application/JSON"}
	)
	public BooleanAnswer logout(
			@RequestParam( value="token" ) String token
	) throws Exception
	{
		SessionManager.getInstance().cleanSession(token);
		return new BooleanAnswer(true, "NO_ERROR", "");
	}
}
