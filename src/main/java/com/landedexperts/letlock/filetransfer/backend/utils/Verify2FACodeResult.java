package com.landedexperts.letlock.filetransfer.backend.utils;

public class Verify2FACodeResult {
    private Boolean valid;
    private int attempts;
    
    public Verify2FACodeResult(Boolean valid, int attempts) {
        this.valid = valid;
        this.attempts = attempts;
    }
    
    public void setValid(Boolean valid) {
        this.valid = valid;
    }
    
    public Boolean getValid() {
        return valid;
    }
    
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    
    public int getAttempts() {
        return attempts;
    }
}
