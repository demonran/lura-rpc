package org.lura.rpc.server.undertow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UndertowHttpServerTest
 *
 * @author Liu Ran
 */
class UndertowHttpServerTest {

    private UndertowHttpServer httpServer;

    @Test
    void doStart() {

        httpServer = new UndertowHttpServer();
        httpServer.doStart(8888);
    }
}
