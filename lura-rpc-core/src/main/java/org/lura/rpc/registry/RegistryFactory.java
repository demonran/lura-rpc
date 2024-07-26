package org.lura.rpc.registry;

public class RegistryFactory {

    private final static Registry INSTANCE = new EtcdRegistry();

    public static Registry getRegistry() {
        return INSTANCE;
    }
}
