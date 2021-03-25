package com.landedexperts.letlock.filetransfer.backend.utils;

public class AppAccountBalance {
    private String balance = "-1";
    private String address = "";
    
    public AppAccountBalance() {
    }
    
    public AppAccountBalance(String address, String balance) {
        this.address = address;
        this.balance = balance;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public String getBalance() {
        return this.balance;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setBalance(String balance) {
        this.balance = balance;
    }
}
