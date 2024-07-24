package org.lura.rpc.provider;

import cn.hutool.core.net.NetUtil;
import org.lura.rpc.RpcApplication;
import org.lura.rpc.common.service.UserService;
import org.lura.rpc.model.RpcConfig;
import org.lura.rpc.model.ServiceMetaInfo;
import org.lura.rpc.model.ServiceRegistration;
import org.lura.rpc.registry.EtcdRegistry;
import org.lura.rpc.registry.LocalRegistry;
import org.lura.rpc.registry.Registry;
import org.lura.rpc.registry.RegistryFactory;
import org.lura.rpc.server.HttpServer;
import org.lura.rpc.server.VertxHttpServer;
import org.lura.rpc.utls.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

public class EasyProviderExample {

    public static void main(String[] args) {

        RpcApplication.init();

        final int port = 8888;

        List<ServiceRegistration> serviceRegistrations = new ArrayList<>();
        serviceRegistrations.add(new ServiceRegistration(UserService.class.getName(), UserServiceImpl.class));

        Registry registry = RegistryFactory.getRegistry();



        serviceRegistrations.forEach(serviceRegistration -> {
            LocalRegistry.INSTANCE.register(serviceRegistration.getServiceName(), serviceRegistration.getServiceClass());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceRegistration.getServiceName());
            serviceMetaInfo.setServiceAddress(NetUtil.getLocalhostStr());
            serviceMetaInfo.setServicePort(port);

            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        HttpServer httpServer = new VertxHttpServer();

        httpServer.doStart(port);
    }


}
