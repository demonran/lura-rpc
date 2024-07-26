package org.lura.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceRegistration {

    private String serviceName;

    private Class<?> serviceClass;
}
