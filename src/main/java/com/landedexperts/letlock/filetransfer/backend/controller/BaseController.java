package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.utils.LetLockAutheticationException;


public class BaseController {

    @Autowired
    UserMapper userMapper;

    @GetMapping(value = "/authenticate", produces = { "application/JSON" })
    public BooleanResponse authenticate(@RequestParam(value = "token") final String token) throws Exception {

        String returnCode = "SUCCESS";
        String returnMessage = "";
        boolean validToken = true;
        try {
            mapToUserId(token);
        } catch (LetLockAutheticationException lae) {
            returnCode = "TOKEN_INVALID";
            returnMessage = "Invalid token";
            validToken = false;
        }
        return new BooleanResponse(validToken, returnCode, returnMessage);

    }

    @GetMapping(value = "/autheticate_for_chat_room", produces = { "application/JSON" })
    public JsonResponse<Set<String>> autheticateUserForChatRoom(
            @RequestParam(value = "token") final String token,
            @RequestParam(value = "roomKey") final String roomKey) throws Exception {
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        Set<String> roomNames = Collections.emptySet();

        JsonResponse<Set<String>> value = new JsonResponse<Set<String>>();
        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            roomNames = userMapper.getChatRoomNames(userId);
            if (roomNames.contains(roomKey)) {
                returnCode = "SUCCESS";
                returnMessage = "";
            }else {
                returnCode = "ROOM_ACCESS_DENIED";
                returnMessage = "User is not asscociated with the given chat room";
            }
        }
        value.setReturnCode(returnCode);
        value.setReturnMessage(returnMessage);
        value.setResult(roomNames);
        return value;
    }

    long mapToUserId(final String token) throws LetLockAutheticationException {
        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            return SessionManager.getInstance().getUserId(token);
        }
        throw new LetLockAutheticationException();

    }

}
