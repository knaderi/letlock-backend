package com.landedexperts.letlock.filetransfer.backend.utils;

public class AntideoEmailValiationVO {

    private String email;
    private boolean free_provide;
    private boolean spam;
    private boolean scam;
    private boolean disposable;
    private Error error;

    public String getEmail() {
        return email;
    }

    public AntideoEmailValiationVO(String email, boolean free_provide, boolean spam, boolean scam, boolean disposable, Error error) {
        super();
        this.email = email;
        this.free_provide = free_provide;
        this.spam = spam;
        this.scam = scam;
        this.disposable = disposable;
        this.error = error;
    }

    public AntideoEmailValiationVO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFree_provide() {
        return free_provide;
    }

    public void setFree_provide(boolean free_provide) {
        this.free_provide = free_provide;
    }

    public boolean isSpam() {
        return spam;
    }

    public void setSpam(boolean spam) {
        this.spam = spam;
    }

    public boolean isScam() {
        return scam;
    }

    public void setScam(boolean scam) {
        this.scam = scam;
    }

    public boolean isDisposable() {
        return disposable;
    }

    public void setDisposable(boolean disposable) {
        this.disposable = disposable;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Error {
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        String code;
        String message;
    }

}
