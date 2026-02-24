package com.griffin.aicodemother.common;

import com.griffin.aicodemother.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @className: BaseResponse
 * @author: Griffin Wang
 * @date: 2026/2/11 10:31
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}

