package org.lura.rpc.loadbalance;

import org.lura.rpc.model.ServiceMetaInfo;

import java.util.List;

public interface LoadBalancer {

    ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfos);
}
