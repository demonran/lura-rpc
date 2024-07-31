package org.lura.rpc.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.lura.rpc.server.HttpServer;

/**
 * NettyServer
 *
 * @author Liu Ran
 */
public class NettyHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workers = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workers)
                             .channel(NioServerSocketChannel.class)
                                     .option(ChannelOption.SO_BACKLOG, 100)
                             .childHandler(new ChannelInitializer<SocketChannel>() {
                                 @Override
                                 protected void initChannel(SocketChannel ch) throws Exception {
                                     ch.pipeline()
                                       .addLast(new IdleStateHandler(60, 0, 0))
                                       .addLast(new HttpServerCodec())
                                       .addLast(new HttpObjectAggregator(5 * 1024 * 1024))
                                       .addLast(new NettyHttpServerHandler());
                                 }
                             });

            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }
}
