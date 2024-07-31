package org.lura.rpc.server.undertow;

import cn.hutool.core.io.IoUtil;
import io.undertow.server.BlockingHttpExchange;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import org.lura.rpc.proxy.RpcHandler;

import java.nio.ByteBuffer;

/**
 * UndertowHttpServerHandler
 *
 * @author Liu Ran
 */
public class UndertowHttpServerHandler implements HttpHandler {

    private RpcHandler rpcHandler = new RpcHandler();

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        byte[] data = rpcHandler.invoke(IoUtil.readBytes(exchange.getInputStream()));
        exchange.getResponseHeaders().put(io.undertow.util.Headers.CONTENT_TYPE, "application/json");
        exchange.setStatusCode(StatusCodes.OK);
        exchange.getResponseSender().send(ByteBuffer.wrap(data)); // 发送响应体
        // 设置状态码为200 OK

    }
}
