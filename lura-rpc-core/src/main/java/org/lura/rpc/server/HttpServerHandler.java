package org.lura.rpc.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.lura.rpc.model.RpcRequest;
import org.lura.rpc.model.RpcResponse;
import org.lura.rpc.registry.LocalRegistry;
import org.lura.rpc.serializer.JdkSerializer;
import org.lura.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {

    private final Serializer serializer = new JdkSerializer();
    @Override
    public void handle(HttpServerRequest httpServerRequest) {

        httpServerRequest.bodyHandler(body -> {
            RpcResponse rpcResponse = new RpcResponse();

            try {
                RpcRequest rpcRequest = serializer.deserialize(body.getBytes(), RpcRequest.class);



                Class<?> serviceClazz = LocalRegistry.INSTANCE.lookup(rpcRequest.getServiceName());

                Method method = serviceClazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());

                Object result = method.invoke(serviceClazz.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());

                rpcResponse.setData(result);
                rpcResponse.setDataType(result.getClass());
                rpcResponse.setMessage("ok");


            } catch (Exception e) {
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            doResponse(httpServerRequest, rpcResponse);

        });
    }

    void doResponse(HttpServerRequest request, RpcResponse rpcResponse) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
