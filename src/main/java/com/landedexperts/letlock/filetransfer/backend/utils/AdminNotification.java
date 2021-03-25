package com.landedexperts.letlock.filetransfer.backend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.controller.EmailServiceFacade;

@Service
public class AdminNotification {

    @Autowired
    EmailServiceFacade emailServiceFacade;
    
    public void actionFailure(final String action, final Exception e) {
        try {
            emailServiceFacade.sendAdminFailureNotification(action, e.getMessage());
        } catch (Exception err) {
        }
    return;
    }

    public void appBalanceThresholdReached(final AppAccountBalance balanceInfo) {
        try {
            emailServiceFacade.sendAppBalanceNotification(balanceInfo.getAddress(), balanceInfo.getBalance());
        } catch (Exception err) {

        }
    }
}
