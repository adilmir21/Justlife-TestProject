package com.example.justlife.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDTO <T>{
    private T data;
    private Integer statusCode;
    private String errorMessage;
}
