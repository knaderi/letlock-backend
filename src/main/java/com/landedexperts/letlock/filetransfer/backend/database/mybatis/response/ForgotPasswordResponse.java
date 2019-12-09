package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class ForgotPasswordResponse extends ErrorCodeMessageResponse{
    public String getResetToken() {
        return resetToken;
    }


    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }


    private String resetToken;


    public ForgotPasswordResponse(final String resetToken,  final String errorCode, final String errorMessage) {
        super(errorCode, errorMessage);
        this.resetToken = resetToken;
    }

}
