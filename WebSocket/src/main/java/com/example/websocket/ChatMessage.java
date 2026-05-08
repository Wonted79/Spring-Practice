package com.example.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    // 메시지 타입: 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK, LEAVE
    }

    private MessageType type;  // 메시지 타입
    private String roomId;     // 채팅방 ID
    private String sender;     // 보낸 사람
    private String message;    // 내용
}