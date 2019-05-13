package com.server.draw.controller;

import com.server.draw.model.PaintingMsg;
import com.server.draw.model.RInfo;
import com.server.draw.model.WsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private SimpMessagingTemplate template;
    @Autowired
    public WebSocketController(SimpMessagingTemplate t) {
        template = t;
    }


    /**
     * websocket接收绘画信息
     * @param drawMessage

    @MessageMapping("/topic")
    public void sendDrawMessage(PaintingMsg drawMessage)
    {
        System.out.println(" ");
        System.out.println(" kk");
        System.out.println(drawMessage);

        //此处获得roomId
        String roomId = drawMessage.getRoomId();
        WsInfo wsInfo = new WsInfo();
        wsInfo.setType("DRAW_MESSAGE");
        wsInfo.setContent(drawMessage);
        String dest = "/topic/roomId/"+roomId;
        this.template.convertAndSend(dest, wsInfo);
    }*/
    @MessageMapping("/msg")
    public void sendCanvasMessage(PaintingMsg drawMessage)
    {
        System.out.println(drawMessage);
        String roomId = drawMessage.getRoomId();
        WsInfo wsInfo = new WsInfo();
        wsInfo.setType("DRAW_MESSAGE");
        wsInfo.setContent(drawMessage);
        String dest = "/canvas/roomId/"+roomId;
        this.template.convertAndSend(dest, wsInfo);
    }
}
