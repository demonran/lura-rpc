package org.lura.rpc.registry;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {

    public static final ServiceRegistry INSTANCE = new ServiceRegistry();

    private final Map<String, Class<?>> services = new HashMap<>();

    public void register(String serviceName, Class<?> serviceClazz) {
        services.put(serviceName, serviceClazz);
    }

    public Class<?> lookup(String serviceName) {
        return services.get(serviceName);
    }

    public void unregister(String serviceName) {
        services.remove(serviceName);
    }
}
