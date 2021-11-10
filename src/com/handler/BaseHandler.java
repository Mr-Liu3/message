package com.handler;
import com.data.RequestData;
import com.util.JsonUtil;
import com.util.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseHandler extends SimpleChannelInboundHandler<Object> {

	public static final Registry SETTING_REGISTRY = new Registry(){
		private Map<Long,List<ChannelHandlerContext>> registryChannelContext = Collections.synchronizedMap(new LinkedHashMap<Long, List<ChannelHandlerContext>>());
		@Override
		public synchronized void add(Long id, ChannelHandlerContext ctx) {
			boolean isExist = false;
			for (List<ChannelHandlerContext> channelContexts : registryChannelContext.values()) {
				if (channelContexts.contains(ctx)) {
					isExist = true;
				}
			}
			if (!isExist) {
				if (registryChannelContext.containsKey(id)) {
					List<ChannelHandlerContext> channelContextList = registryChannelContext.get(id);
					channelContextList.add(ctx);
					registryChannelContext.put(id, channelContextList);
				} else {
					List<ChannelHandlerContext> channelContextList = new ArrayList<ChannelHandlerContext>();
					channelContextList.add(ctx);
					registryChannelContext.put(id, channelContextList);
				}
			}
		}

		@Override
		public synchronized List<ChannelHandlerContext> get(List<Long> targetIds) {
			if(StringUtil.isEmpty(targetIds) || targetIds.size() == 0){
				return null;
			}
			List<ChannelHandlerContext> handlerContextList = null;
			for (Long targetId : targetIds){
				if(registryChannelContext.containsKey(targetId)){
					if(handlerContextList == null){
						handlerContextList = new ArrayList<ChannelHandlerContext>();
					}
					handlerContextList.addAll(registryChannelContext.get(targetId));
				}
			}
			return handlerContextList;
		}

		@Override
		public synchronized void remove(ChannelHandlerContext ctx) {
			boolean isExist = false;
			List<Long> channelId = new ArrayList<Long>();
			for (Map.Entry<Long,List<ChannelHandlerContext>> channelContextsEntry : registryChannelContext.entrySet()) {
				List<ChannelHandlerContext> channelContexts = channelContextsEntry.getValue();
				if (channelContexts.contains(ctx)) {
					channelId.add(channelContextsEntry.getKey());
					isExist = true;
				}
			}
			if(isExist){
				for (Long aLong : channelId) {
					List<ChannelHandlerContext> channelContexts = registryChannelContext.get(aLong);
					if(channelContexts.size() > 1){
						channelContexts.remove(ctx);
						registryChannelContext.put(aLong,channelContexts);
					}else{
						registryChannelContext.remove(aLong);
					}
				}
			}
		}
	};

	public static final Forward SEND_FORWARD = new Forward(){
		private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		@Override
		public synchronized void send(List<ChannelHandlerContext> channelHandlerContexts, Map<String,Object> data) {
			channelGroup.clear();
			for (ChannelHandlerContext channelHandlerContext : channelHandlerContexts) {
				channelGroup.add(channelHandlerContext.channel());
			}
			if(!channelGroup.isEmpty()){
				String json = JsonUtil.toJsonStr(data);
				TextWebSocketFrame socketJson = new TextWebSocketFrame(json);
				channelGroup.writeAndFlush(socketJson);
			}
		}
	};

	private Registry registry = SETTING_REGISTRY;

	private Forward forward = SEND_FORWARD;

	public void addRegistry(ChannelHandlerContext ctx, Long id){
		if(!StringUtil.isEmpty(id)){
			registry.add(id,ctx);
		}
	}

	public void sendMessage(List<Long> targetIds, Map<String,Object> data){
		List<ChannelHandlerContext> channelHandlerContexts = registry.get(targetIds);
		if(!StringUtil.isEmpty(channelHandlerContexts)){
			forward.send(channelHandlerContexts, data);
		}
	}

	public void removeRegistry(ChannelHandlerContext ctx){
		registry.remove(ctx);
	}

	public interface Registry {
		void add(Long id, ChannelHandlerContext ctx);
		List<ChannelHandlerContext> get(List<Long> targetIds);
		void remove(ChannelHandlerContext ctx);
	}

	public interface Forward {
		void send(List<ChannelHandlerContext> channelHandlerContexts, Map<String,Object> data);
	}
}
