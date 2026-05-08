package com.example.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 목록 화면
    @GetMapping("/room")
    public String rooms() {
        return "/chat/room";
    }

    // 채팅방 목록 조회 (JSON)
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(@PathVariable String roomId) {
        return "/chat/roomdetail";
    }

    // 특정 채팅방 조회 (JSON)
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}