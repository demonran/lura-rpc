package org.lura.rpc.grpc;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.vertx.core.Vertx;
import io.vertx.grpc.server.GrpcServer;
import org.lura.rpc.server.HttpServer;

import java.io.IOException;

public class GRpcServer implements HttpServer {


    private Server server;

    @Override
    public void doStart(int port) {

        try {
            Vertx vertx = Vertx.vertx();
            GrpcServer grpcServer = GrpcServer.server(vertx);

            server = ServerBuilder.forPort(port)
                    .build()
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Runtime.getRuntime().addShutdownHook(new Thread(GRpcServer.this::stop));

    }

    private void stop() {
        server.shutdown();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
