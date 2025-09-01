package com.example.Chat.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @GetMapping("/api/v1/chat")
    public String greet() {
        return "Welcome to Hyunwwo, Hyunwwo.com";
    }
}
