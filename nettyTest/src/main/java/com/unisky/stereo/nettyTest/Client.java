package com.unisky.stereo.nettyTest;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


public class Client {
    
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(4));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                // do nothing
            }
        });

        int count = Integer.parseInt(args[1]);
        int internal = Integer.parseInt(args[2]);
    
    
        for (int i = 0; i < count; i++) {
            if (i % internal == 0){
                Thread.sleep(1000);
            }
            bootstrap.connect(args[0], 10000);
        }
        
        
        Thread.sleep(100000000);
    }
    
}