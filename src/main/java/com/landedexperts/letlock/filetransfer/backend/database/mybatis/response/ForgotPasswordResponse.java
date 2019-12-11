package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class ForgotPasswordResponse extends BooleanResponse{
    public String getResetToken() {
        return resetToken;
    }


    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }


    private String resetToken;


    public ForgotPasswordResponse(final boolean result, final String resetToken,  final String errorCode, final String errorMessage) {
        super(result, errorCode, errorMessage);
        this.resetToken = resetToken;
    }

}
