package org.lura.rpc.loadbalance;

import org.lura.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfos) {

        if (serviceMetaInfos.size() == 1) {
            return serviceMetaInfos.get(0);
        }
        int index = ThreadLocalRandom.current().nextInt(serviceMetaInfos.size());

        return serviceMetaInfos.get(index);
    }
}
