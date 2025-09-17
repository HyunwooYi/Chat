package com.example.chat.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MessageErrorCode implements ErrorCode{

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Message not found")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
