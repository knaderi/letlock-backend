package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

public class AppsSettingsVO {
    private long id;
    private String app;
    private String key;
    private String value;
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String appName) {
        this.app = appName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
