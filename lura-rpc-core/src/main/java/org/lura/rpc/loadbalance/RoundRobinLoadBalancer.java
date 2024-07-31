package org.lura.rpc.loadbalance;

import org.lura.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RoundRobinLoadBalancer
 *
 * @author Liu Ran
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger position;

    public RoundRobinLoadBalancer(int seed) {
        position = new AtomicInteger(seed);
    }

    @Override
    public ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfos) {
        if (serviceMetaInfos.size() == 0) {
            return null;
        }
        if (serviceMetaInfos.size() == 1) {
            return serviceMetaInfos.get(0);
        }

        int pos = position.get() & Integer.MAX_VALUE;

        return serviceMetaInfos.get(pos % serviceMetaInfos.size());
    }
}
