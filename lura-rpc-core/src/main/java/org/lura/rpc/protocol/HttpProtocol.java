package org.lura.rpc.protocol;

import org.lura.rpc.server.netty.NettyHttpServer;

import java.net.Inet4Address;

public class HttpProtocol implements Protocol {

    private NettyHttpServer nettyHttpServer;
    @Override
    public void bind(int port) {
        nettyHttpServer = new NettyHttpServer();
        nettyHttpServer.doStart(port);
    }

    @Override
    public void connect(Inet4Address address, int port) {

    }
}
