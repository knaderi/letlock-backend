package com.landedexperts.letlock.noec2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.noec2.answer.BooleanAnswer;

@RestController
public class AskFundsController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/ask_funds",
		produces = {"application/JSON"}
	)
	public BooleanAnswer askFunds(
			@RequestParam( value="file_transfer_uuid" ) String file_transfer_uuid,
			@RequestParam( value="signed_transaction_hex" ) String signed_transaction_hex,
			@RequestParam( value="step" ) String step
	) throws Exception
	{
		Boolean result = false;



		return new BooleanAnswer(result, "");
	}
}
