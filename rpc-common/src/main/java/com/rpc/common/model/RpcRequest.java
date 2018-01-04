package com.rpc.common.model;

import lombok.Data;

/**
 * 封装请求
 * @Author gu
 * @Version V1.0
 * @Date 17/12/29 下午4:31
 */
@Data
public class RpcRequest {
    private String requestId;
    private String interfaceName;
    private String version;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}
