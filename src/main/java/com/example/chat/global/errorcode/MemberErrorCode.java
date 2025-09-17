package com.example.chat.global.errorcode;

import com.example.chat.global.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    INACTIVE_USER(HttpStatus.FORBIDDEN, "User is inactive"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not found"),
    CANNOT_BAN_ADMIN(HttpStatus.BAD_REQUEST, "Can not BAN admin")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
