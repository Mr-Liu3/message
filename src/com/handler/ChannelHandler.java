package com.handler;
import com.data.RequestData;
import com.util.Constants;
import com.util.JsonUtil;
import com.util.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;

import java.util.List;
import java.util.Map;

public class ChannelHandler extends BaseHandler{
	private WebSocketServerHandshaker handshaker;


	/**
	 * 当客户端断开连接
	 * */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		removeRegistry(ctx);
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().writeAndFlush("success");
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg){
		if(msg instanceof FullHttpRequest) {//传统的HTTP接入
			FullHttpRequest req = (FullHttpRequest) msg;
			if (!req.decoderResult().isSuccess() || (!"websocket".equalsIgnoreCase(req.headers().get("Upgrade")))) {
				//sendHttpResponse(ctx,req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
				return;
			}

			//构造握手响应返回
			String url = "ws://127.0.0.1:5688";
			//注意，这条地址别被误导了，其实这里填写什么都无所谓，WS协议消息的接收不受这里控制
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(url, null, false);
			handshaker = wsFactory.newHandshaker(req);

			if (handshaker == null) {//不支持
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
			}
		}else if(msg instanceof WebSocketFrame){// WebSocket接入
			WebSocketFrame frame = (WebSocketFrame) msg;
			//关闭请求
			if(frame instanceof CloseWebSocketFrame){
				handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
				return;
			}
			//ping请求
			if(frame instanceof PingWebSocketFrame){
				ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
				return;
			}
			//只支持文本格式，不支持二进制消息
			if(!(frame instanceof TextWebSocketFrame)){
				//throw new Exception("仅支持文本格式");
				//也可能是一个PongWebSocketFrame心跳包
				return;
			}
			String message = ((TextWebSocketFrame) frame).text();
			try {
				RequestData requestData = (RequestData) JsonUtil.jsonToObject(message, RequestData.class);
				if(!StringUtil.isEmail(requestData.getCommand())){
					if(requestData.getCommand().equals(Constants.Command.REGISTER)){
						addRegistry(ctx,requestData.getId());
					}else if(requestData.getCommand().equals(Constants.Command.FORWARD)){
						sendMessage(requestData.getTargetIds(), requestData.getData());
					}
				}
			} catch (Exception e){
			}
		}
	}

	/**
	 * 异常关闭连接
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { // (4)
		//System.out.print(ctx);
		// 当出现异常就关闭连接
		//cause.printStackTrace();
		removeRegistry(ctx);
		ctx.close();
	}

}
