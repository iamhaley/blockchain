package com.antiscam.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端
 */
public class Client {

    void connect(String host, int port) {
        EventLoopGroup group     = new NioEventLoopGroup();
        Bootstrap      bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientHandler());
        try {
            ChannelFuture cf = bootstrap.connect(host, port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Client().connect("127.0.0.1", 8887);

    }

}
