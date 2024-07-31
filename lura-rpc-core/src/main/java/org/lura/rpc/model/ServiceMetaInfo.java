package org.lura.rpc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ServiceMetaInfo implements Serializable {

    private String serviceName;

    private String serviceAddress;

    private int servicePort;

    public ServiceMetaInfo(String serviceName, String serviceAddress, int servicePort) {
        this.serviceName = serviceName;
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
    }

    public String getServiceNodeKey() {
        return serviceName + ":" + serviceAddress + ":" + servicePort;
    }

    public String getServiceUrl() {
        return "http://" + serviceAddress + ":" + servicePort;
    }
}
