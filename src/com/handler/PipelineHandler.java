package com.handler;
import com.coder.RequestDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.security.KeyStore;

public class PipelineHandler extends ChannelInitializer<SocketChannel>{

	private final SslContext sslContext;

	public PipelineHandler() {
		String url = System.getProperty("user.dir");
		System.out.print(url);
		String keyStoreFilePath = url+"\\SSL\\4906914_www.izeny.cn.pfx";
		String keyStorePassword = "LU6WjaTy";

		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(new FileInputStream(keyStoreFilePath), keyStorePassword.toCharArray());

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

			sslContext = SslContextBuilder.forServer(keyManagerFactory).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
		ch.pipeline()
		.addLast(new SslHandler(sslEngine))
		.addLast("decoder", new HttpRequestDecoder())
		//.addLast("decoder", new HttpRequestDecoder())
		//.addLast("decoder", new RequestDecoder())
		.addLast("encoder", new HttpResponseEncoder())
        .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
        .addLast("handler", new ChannelHandler());
	}

}
