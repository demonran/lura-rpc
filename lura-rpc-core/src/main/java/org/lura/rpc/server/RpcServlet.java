package org.lura.rpc.server;

import cn.hutool.core.io.IoUtil;
import org.lura.rpc.proxy.RpcHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RpcServlet
 *
 * @author Liu Ran
 */
public class RpcServlet extends HttpServlet {

    private RpcHandler rpcHandler = new RpcHandler();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] bytes = IoUtil.readBytes(req.getInputStream());
        byte[] data = rpcHandler.invoke(bytes);

        resp.addHeader("content-type", "application/json");

        resp.getOutputStream().write(data);
        resp.flushBuffer();
    }
}
