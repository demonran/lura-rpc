package org.lura.rpc.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.lura.rpc.model.RpcRequest;
import org.lura.rpc.model.RpcResponse;
import org.lura.rpc.proxy.RpcHandler;
import org.lura.rpc.registry.ServiceRegistry;
import org.lura.rpc.serializer.JdkSerializer;
import org.lura.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {

    private final RpcHandler rpcHandler = new RpcHandler();
    @Override
    public void handle(HttpServerRequest httpServerRequest) {

        httpServerRequest.bodyHandler(body -> {

            byte[] data = rpcHandler.invoke(body.getBytes());
            doResponse(httpServerRequest, data);

        });
    }

    void doResponse(HttpServerRequest request, byte[] data) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        httpServerResponse.end(Buffer.buffer(data));
    }
}
