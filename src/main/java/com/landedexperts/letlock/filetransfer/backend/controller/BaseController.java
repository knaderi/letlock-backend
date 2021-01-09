package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SetResponse;
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

    @GetMapping(value = "/authenticate_for_chat_room", produces = { "application/JSON" })
    public BooleanResponse autheticateUserForChatRoom(
            @RequestParam(value = "token") final String token,
            @RequestParam(value = "roomKey") final String roomKey) throws Exception {
         Set<String> roomNames = Collections.emptySet();


        long userId = SessionManager.getInstance().getUserId(token);
         BooleanResponse value = new BooleanResponse(true, "Success", "");
        if (userId > 0) {
            roomNames = userMapper.getChatRoomNames(userId);
            if (!roomNames.contains(roomKey)) {
                value =  new BooleanResponse(false, "ROOM_ACCESS_DENIED", "User is not asscociated with the given chat room");
            }
        }else {
            value = new BooleanResponse(false, "TOKEN_INVALID", "Invalid token");
        }
        return value;
    }
    
    @GetMapping(value = "/get_user_rooms", produces = { "application/JSON" })
    public SetResponse<Set<String>> getUserRooms(
            @RequestParam(value = "token") final String token) throws Exception {
         Set<String> roomNames = Collections.emptySet();


        long userId = SessionManager.getInstance().getUserId(token);
        SetResponse<Set<String>> value = new SetResponse(Collections.emptySet(), "Success", "");
        if (userId > 0) {
            roomNames = userMapper.getChatRoomNames(userId);

                value =  new SetResponse(roomNames, "SUCCESS", "");

        }else {
            value = new SetResponse(roomNames, "TOKEN_INVALID", "Invalid token");
        }
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
