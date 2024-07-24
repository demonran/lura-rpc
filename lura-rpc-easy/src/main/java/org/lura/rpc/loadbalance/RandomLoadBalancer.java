package org.lura.rpc.loadbalance;

import org.lura.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfos) {

        if (serviceMetaInfos.size() == 1) {
            return serviceMetaInfos.get(0);
        }

        int random = new Random().nextInt(serviceMetaInfos.size());
        return serviceMetaInfos.get(random);
    }
}
