/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.utils.LetLockBackendEnv;

@Service
public class LetLockEmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(final Email email) {
        LetLockBackendEnv letLockEnv = LetLockBackendEnv.getInstance();
        if (!letLockEnv.isLocalEnv()) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(email.getSubject());
            simpleMailMessage.setFrom(email.getFrom());
            simpleMailMessage.setTo(email.getTo());
            simpleMailMessage.setText(email.getMessageText());
            javaMailSender.send(simpleMailMessage);
        }
    }

    public void sendHTMLMail(final Email email) throws Exception {
        LetLockBackendEnv letLockEnv = LetLockBackendEnv.getInstance();
        if (!letLockEnv.isLocalEnv()) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(email.getSubject());
            helper.setFrom(email.getFrom());
            helper.setTo(email.getTo());
            helper.setText(email.getMessageText(), true);
            javaMailSender.send(mimeMessage);
        }
    }

    public void sendMailWithAttachment(final Email email, String filePath) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setFrom(email.getFrom());
        mimeMessageHelper.setTo(email.getTo());
        mimeMessageHelper.setText(email.getMessageText());
        FileSystemResource file = new FileSystemResource(new File(filePath));
        mimeMessageHelper.addAttachment("Sample File", file);
        javaMailSender.send(mimeMessage);
    }

}
