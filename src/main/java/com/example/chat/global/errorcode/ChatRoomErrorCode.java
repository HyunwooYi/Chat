package com.example.chat.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode implements ErrorCode{

    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Room Not Found")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
