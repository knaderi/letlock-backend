/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.session;

import java.util.Date;

public class UserSession {
	private static long timeout = 3000000L; /*Timeout in milliseconds, 50 mins*/
	private static Date newExpire() {
		return new Date((new Date()).getTime() + UserSession.timeout);
	}

	private long userId;
	private Date expires;

	public UserSession(final long userId) {
		this.userId = userId;
		this.expires = UserSession.newExpire();
	}

	public long getUserId() {
		return this.userId;
	}

	public Boolean isActive() {
		/*
		 * The UserSession is active when the
		 * current time is before the expiring date
		 * */
		return (new Date()).before(this.expires);
	}

	public void extend() {
		this.expires = UserSession.newExpire();
	}
}
