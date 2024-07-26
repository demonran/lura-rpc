package org.lura.rpc;

import org.lura.rpc.model.RpcConfig;
import org.lura.rpc.registry.Registry;
import org.lura.rpc.registry.RegistryFactory;
import org.lura.rpc.utls.ConfigUtils;

public class RpcApplication {



    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        Registry registry = RegistryFactory.getRegistry();
        registry.init(newRpcConfig);

        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

}
