package org.lura.rpc.registry;

public class RegistryFactory {

    private final static Registry INSTANCE = new LocalFileRegistry();

    public static Registry getRegistry() {
        return INSTANCE;
    }
}
