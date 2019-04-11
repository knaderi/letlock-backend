package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.SessionTokenAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.request.mapper.LoginMapper;
import com.landedexperts.letlock.filetransfer.backend.database.request.result.Login;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class LoginController {
	@Autowired
	private LoginMapper loginMapper;

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
		Login answer = loginMapper.login(loginName, password);

		Integer userId = answer.getUserId();
		String errorCode = answer.getErrorCode();
		String errorMessage = answer.getErrorMessage();

		String token = "";
		if(errorCode.equals("NO_ERROR")) {
			token = SessionManager.getInstance().generateSessionToken(userId);
		}

		return new SessionTokenAnswer(token, errorCode, errorMessage);
	}
}
