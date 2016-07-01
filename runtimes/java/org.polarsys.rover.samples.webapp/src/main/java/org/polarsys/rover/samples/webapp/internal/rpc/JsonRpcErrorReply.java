package org.polarsys.rover.samples.webapp.internal.rpc;

import java.math.BigInteger;

/**
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public class JsonRpcErrorReply extends AbstractJsonRpcReply {

	private final JsonRpcError error;

	public JsonRpcErrorReply(BigInteger id, int code, String message, Object data) {
		super(id);
		error = new JsonRpcError(code, message, data);
	}

	public JsonRpcError getError() {
		return error;
	}

}
