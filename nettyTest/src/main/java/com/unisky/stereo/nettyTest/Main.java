package com.unisky.stereo.nettyTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {
    
    public static void main1(String[] args) {
    
        Set<Channel> channelSet = new HashSet<>();
        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println("channel: " + channelSet.size());
            }
        }).start();
        
        EventLoopGroup tcpConnectGroup = new EpollEventLoopGroup(4);
        EventLoopGroup bizWorkerGroup = new EpollEventLoopGroup(4);

        ServerBootstrap nettyManager = new ServerBootstrap();

        nettyManager.option(ChannelOption.SO_BACKLOG, 5000)
            .group(tcpConnectGroup, bizWorkerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                channelSet.add(ch);
                ch.closeFuture().addListener((ChannelFutureListener) future -> channelSet.remove(ch));
            }
        });

        nettyManager.bind(10000);
    
    
        Scanner scanner = new Scanner(System.in);
        
        byte[] data = new byte[200];
        while (true){
            String cmd = scanner.next();
            if (cmd == null || "".equals(cmd) || cmd.trim().equals("")){
                System.out.println("nothing input");
                continue;
            }
            
            if ("send".equals(cmd)){

                long start = System.nanoTime();
    
                int size = channelSet.size();
                for (Channel channel : channelSet) {
                    channel.write(data);
                }
                
                long cost = System.nanoTime() - start;
                
                System.out.println("count: " + size + ", cost: " + cost + ", ava: " + (cost / size));
            }
            
            System.out.println("no support");
        }
    }
}