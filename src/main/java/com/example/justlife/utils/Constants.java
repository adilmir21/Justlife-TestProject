package com.example.justlife.utils;

import org.springframework.http.HttpStatus;

public class Constants {
    public static final Integer STATUS_SUCCESS = HttpStatus.OK.value();
    public static final Integer STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST.value();
    public static final Integer STATUS_NOT_FOUND = HttpStatus.NOT_FOUND.value();
    public static final Integer STATUS_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
    public static final Integer STATUS_ALREADY_EXISTS = HttpStatus.CONFLICT.value();
    public static final String EXCEPTION = "Exception : ";
}
