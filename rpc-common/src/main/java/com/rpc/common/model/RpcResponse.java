package com.rpc.common.model;

import lombok.Data;

/**
 * 封装请求结果
 * @Author gu
 * @Version V1.0
 * @Date 17/12/29 下午4:25
 */
@Data
public class RpcResponse {
    private String requestId;
    private Exception exception;
    private Object result;

    public boolean hasException() {
        return exception != null;
    }
}
