package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.Date;
import java.util.UUID;

public class UuidNameDate {
	public UuidNameDate(final UUID uuid, final String name, final Date create) {
		this.uuid = UUID.fromString(uuid.toString());
		this.name = name;
		this.create = create;
	}

	private final UUID uuid;

	public UUID getUuid() {
		return uuid;
	}

	private final String name;

	public String getName() {
		return name;
	}

	private final Date create;

	public Date getCreate() {
		return create;
	}
}
