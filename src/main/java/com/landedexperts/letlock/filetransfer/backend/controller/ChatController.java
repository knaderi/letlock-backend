package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SetResponse;

@RestController
public class ChatController {

    @Autowired
    UserMapper userMapper;
    
    @GetMapping(value = "/validate_token", produces = { "application/JSON" })
    public BooleanResponse validateToken(HttpServletRequest request) throws Exception {

        return new BooleanResponse(!(request.getAttribute("user.id") == null), "SUCCESS", "");
    }

    @GetMapping(value = "/authenticate_for_chat_room", produces = { "application/JSON" })
    public BooleanResponse autheticateUserForChatRoom(
            @RequestParam(value = "roomKey") final String roomKey,
            HttpServletRequest request) throws Exception {
         BooleanResponse value = new BooleanResponse(true, "Success", "");
         long userId = (long) request.getAttribute("user.id");
         Set<String> roomNames = userMapper.getActiveFileTransferUUIDs(userId);
         if (!roomNames.contains(roomKey)) {
             value =  new BooleanResponse(false, "ROOM_ACCESS_DENIED", "User is not asscociated with the given chat room");
         }
        return value;
    }
    
    @GetMapping(value = "/get_user_rooms", produces = { "application/JSON" })
    public SetResponse<String> getUserRooms(HttpServletRequest request) throws Exception {
         long userId = (long) request.getAttribute("user.id");
         Set<String> roomNames = userMapper.getActiveFileTransferUUIDs(userId);
         SetResponse<String> value = new SetResponse<String>(roomNames, "SUCCESS", "");
         return value;
    }

}
