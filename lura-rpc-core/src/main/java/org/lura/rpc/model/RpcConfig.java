package org.lura.rpc.model;

import lombok.Data;

@Data
public class RpcConfig {

    private String endpoints;

    private String rpcFilePath = System.getProperty("user.home");
}
