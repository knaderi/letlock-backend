package com.landedexperts.letlock.filetransfer.backend.session;

import java.util.Date;

public class UserSession {
	private static long timeout = 300000L; /*Timeout in milliseconds, 5 mins*/
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
