package org.lura.rpc.server.undertow;

import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;
import org.lura.rpc.server.HttpServer;

/**
 * UndertowHttpServer
 *
 * @author Liu Ran
 */
public class UndertowHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        new Thread(new Runnable(){
            public void run() {
                try {
                    Undertow server = Undertow.builder()
                                              .addHttpListener(port, "localhost")
                                              .setHandler(new BlockingHandler(new UndertowHttpServerHandler()))
                                                      .build();


                    server.start();
                    System.out.println("UndertowHttpServer started on port " + port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void main(String[] args) {
        new UndertowHttpServer().doStart(8888);
    }
}
