package com.ww.mq.controller;


import com.ww.mq.consumer.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MessageController {

    @Autowired
    public MessageSender messageSender;

    //延迟插件controller
    @GetMapping("/send2")
    public String sendByPlugin() {
        messageSender.send2("wangwei", 10);
        return "ok";
    }

}