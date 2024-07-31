package org.lura.rpc.registry;

import org.lura.rpc.loadbalance.LoadBalancer;
import org.lura.rpc.loadbalance.RandomLoadBalancer;
import org.lura.rpc.model.RpcConfig;
import org.lura.rpc.model.ServiceMetaInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LocalRegisty
 *
 * @author Liu Ran
 */
public class LocalRegistry implements Registry {

    private final Map<String, List<ServiceMetaInfo>> map = new HashMap<>();
    private final LoadBalancer lb = new RandomLoadBalancer();

    @Override
    public void init(RpcConfig config) {

    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {

        List<ServiceMetaInfo> serviceMetaInfos = map.getOrDefault(serviceMetaInfo.getServiceName(), new ArrayList<>());
        serviceMetaInfos.add(serviceMetaInfo);
    }

    @Override
    public ServiceMetaInfo lookup(String serviceName) {
        List<ServiceMetaInfo> serviceMetaInfos = map.get(serviceName);
        return lb.select(serviceMetaInfos);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        List<ServiceMetaInfo> serviceMetaInfos = map.getOrDefault(serviceMetaInfo.getServiceName(), new ArrayList<>());
        serviceMetaInfos.remove(serviceMetaInfo);
    }

    @Override
    public void destroy() {

    }
}
