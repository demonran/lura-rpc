package org.lura.rpc.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {

    private String serviceName;

    private String methodName;


    private Class<?>[] parameterTypes;

    private Object[] args;
}
