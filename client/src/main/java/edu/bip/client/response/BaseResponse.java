package edu.bip.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BaseResponse {
    protected boolean success;
    protected String message;
}
