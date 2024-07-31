package org.lura.rpc.server.netty;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NettyHttpServerTest
 *
 * @author Liu Ran
 */
class NettyHttpServerTest {

    private NettyHttpServer nettyHttpServer;

    @Test
    void doStart() {
        nettyHttpServer = new NettyHttpServer();

        nettyHttpServer.doStart(8888);

    }
}
