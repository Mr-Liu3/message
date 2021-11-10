package com.coder;
import com.data.RequestData;
import com.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.internal.ObjectUtil;
import java.nio.charset.Charset;
import java.util.List;
public class RequestDecoder extends MessageToMessageDecoder<ByteBuf>{

	// TODO Use CharsetDecoder instead.
	private final Charset charset;

	/**
	 * Creates a new instance with the current system character set.
	 */
	public RequestDecoder() {
		this(Charset.defaultCharset());
	}

	/**
	 * Creates a new instance with the specified character set.
	 */
	public RequestDecoder(Charset charset) {
		this.charset = ObjectUtil.checkNotNull(charset, "charset");
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out){
		String message = msg.toString(charset);
		try {
			RequestData requestData = (RequestData) JsonUtil.jsonToObject(message, RequestData.class);
			out.add(requestData);
		} catch (Exception e){
			out.add(message);
		}
	}
}
