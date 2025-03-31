package com.ltdd.mobile_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;
    private String token;

    public MessageResponse(String message) {
        this.message = message;
    }
}