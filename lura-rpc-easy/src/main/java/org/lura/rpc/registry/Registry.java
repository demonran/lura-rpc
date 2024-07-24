package org.lura.rpc.registry;

import org.lura.rpc.model.RpcConfig;
import org.lura.rpc.model.ServiceMetaInfo;

import java.util.concurrent.ExecutionException;

public interface Registry {

    default void init(RpcConfig config) {
    }

    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    ServiceMetaInfo lookup(String serviceName);

    void unregister(ServiceMetaInfo serviceMetaInfo);

    void destroy();
}
