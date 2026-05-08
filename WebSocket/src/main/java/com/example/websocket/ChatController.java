package com.example.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

//Publisher 구현
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message") // /pub/chat/message 로 수신
    public void message(ChatMessage message) {
        //입장 메시지
        if (ChatMessage.MessageType.ENTER.equals(message.getType()))
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        // /sub/chat/room/{roomId} 구독자에게 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
