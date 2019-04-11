package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanAnswer;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class LogoutController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/logout",
		produces = {"application/JSON"}
	)
	public BooleanAnswer order_create(
			@RequestParam( value="token" ) String token
	) throws Exception
	{
		SessionManager.getInstance().cleanSession(token);
		return new BooleanAnswer(true, "");
	}
}
