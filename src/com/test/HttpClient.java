package com.test;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.io.UnsupportedEncodingException;
public final class HttpClient
{
	// 要请求的服务器的ip地址
	private String ip;
	// 服务器的端口
	private int port;
	
	private EventLoopGroup bossGroup;

	public HttpClient(String ip, int port){
		this.ip = ip;
		this.port = port;
	}

	// 请求端主题
	private void init() throws InterruptedException, UnsupportedEncodingException {
		bossGroup = new NioEventLoopGroup();
		Bootstrap bs = new Bootstrap();
		try {
			bs.group(bossGroup)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					// marshalling 序列化对象的解码
//              socketChannel.pipeline().addLast(MarshallingCodefactory.buildDecoder());
					// marshalling 序列化对象的编码
//              socketChannel.pipeline().addLast(MarshallingCodefactory.buildEncoder());

					// 处理来自服务端的响应信息
					socketChannel.pipeline()
					.addLast("decoder", new StringDecoder())
					.addLast("encoder", new StringEncoder())
					.addLast("aggregator", new HttpObjectAggregator(512 * 1024))
					.addLast(new ClientHandler());
				}
			});

			// 客户端开启
			ChannelFuture cf = bs.connect(ip, port).sync();
		
			String json = "{\"id\":2,\"type\":2,\"command\":\"register\"}";
		
			// 发送客户端的请求
			cf.channel().writeAndFlush(Unpooled.buffer().writeBytes(json.getBytes()));
		//  Thread.sleep(300);
		//  cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求2$_---".getBytes(Constant.charset)));
		//  Thread.sleep(300);
		//  cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求3$_".getBytes(Constant.charset)));
		
		//  Student student = new Student();
		//  student.setId(3);
		//  student.setName("张三");
		//  cf.channel().writeAndFlush(student);
			
			// 等待直到连接中断
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			bossGroup.shutdownGracefully();
		}finally{
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
		new HttpClient("127.0.0.1", 5688).init();
		/*RequestData requestData = new RequestData();
		requestData.setId(1L);
		requestData.setType(2L);
		requestData.setCommand("service");
		List<Long> ids = new ArrayList<Long>();
		ids.add(2l);ids.add(3l);ids.add(4l);ids.add(5l);ids.add(6l);ids.add(7l);ids.add(8l);
		requestData.setTargetIds(ids);
		Map<String,Object> map = new HashMap<>();
		map.put("cmd","notic");
		map.put("type",2L);
		requestData.setData(map);
		String json = JsonUtil.toJsonStr(requestData);
		System.out.print(json);*/
		/*String json = "{\"id\":1,\"type\":2,\"command\":\"service\",\"targetIds\":[2,3,4,5,6,7,8],\"data\":{\"cmd\":\"notic\",\"type\":2}}";
		RequestData RequestData = (RequestData)JsonUtil.jsonToObject(json,RequestData.class);
		System.out.print(RequestData);*/
		/*String json = "{\"id\":1,\"type\":2,\"command\":\"register\",\"targetIds\":[1,2],\"data\":{\"cmd\":\"notic\",\"type\":2}}";
		RequestData RequestData = (RequestData)JsonUtil.jsonToObject(json,RequestData.class);
		System.out.print(RequestData);*/
	}

}
