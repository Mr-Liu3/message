package com;
import com.handler.PipelineHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
public final class HttpServer
{
	private int port;

	public HttpServer(){
		port = PortFile.getPort();
	}

	public static void main(String[] args) {
		new HttpServer().init();
	}

	public void init(){
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap(); // (2)
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.TCP_NODELAY, true)// (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true) // (6)
					.childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
					.childHandler(new PipelineHandler());
			// 缁戝畾绔彛锛屽紑濮嬫帴鏀惰繘鏉ョ殑杩炴帴
			ChannelFuture future = serverBootstrap.bind(port).sync(); // (7)
			// 绛夊緟鏈嶅姟鍣�  socket 鍏抽棴 銆�
			// 鍦ㄨ繖涓緥瀛愪腑锛岃繖涓嶄細鍙戠敓锛屼絾浣犲彲浠ヤ紭闆呭湴鍏抽棴浣犵殑鏈嶅姟鍣ㄣ��
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	public void colse(){

	}
}
