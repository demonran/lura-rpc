package org.lura.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.lura.rpc.model.RpcRequest;
import org.lura.rpc.model.RpcResponse;
import org.lura.rpc.model.ServiceMetaInfo;
import org.lura.rpc.registry.Registry;
import org.lura.rpc.registry.RegistryFactory;
import org.lura.rpc.serializer.JdkSerializer;
import org.lura.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Registry registry = RegistryFactory.getRegistry();

        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = new RpcRequest();

        rpcRequest.setServiceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setArgs(args);

        byte[] bytes = serializer.serialize(rpcRequest);

        ServiceMetaInfo serviceMetaInfo = registry.lookup(rpcRequest.getServiceName());


        try (HttpResponse response = HttpRequest.post(serviceMetaInfo.getServiceUrl())
                .body(bytes)
                .execute()) {
            RpcResponse rpcResponse = serializer.deserialize(response.bodyBytes(), RpcResponse.class);
            if (rpcResponse.getException() != null) {
                throw new RuntimeException(rpcResponse.getException());
            }
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
