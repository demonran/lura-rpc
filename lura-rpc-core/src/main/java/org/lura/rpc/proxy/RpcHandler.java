package org.lura.rpc.proxy;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.lura.rpc.model.RpcRequest;
import org.lura.rpc.model.RpcResponse;
import org.lura.rpc.registry.ServiceRegistry;
import org.lura.rpc.serializer.JdkSerializer;
import org.lura.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * RpcHandler
 *
 * @author Liu Ran
 */
public class RpcHandler {

    private final Serializer serializer = new JdkSerializer();

    public byte[] invoke(byte[] bytes) {

        RpcResponse rpcResponse = new RpcResponse();

        try {
            RpcRequest rpcRequest = serializer.deserialize(bytes, RpcRequest.class);



            Class<?> serviceClazz = ServiceRegistry.INSTANCE.lookup(rpcRequest.getServiceName());

            Method method = serviceClazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());

            Object result = method.invoke(serviceClazz.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());

            rpcResponse.setData(result);
            rpcResponse.setDataType(result.getClass());
            rpcResponse.setMessage("ok");

        } catch (Exception e) {
            rpcResponse.setMessage(e.getMessage());
            rpcResponse.setException(e);
        }


        byte[] serialized;
        try {
            serialized = serializer.serialize(rpcResponse);

        } catch (IOException e) {
            e.printStackTrace();
            serialized = new byte[0];
        }

        return serialized;

    }

}
