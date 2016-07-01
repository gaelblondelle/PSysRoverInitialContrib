package org.polarsys.rover.samples.webapp.internal.rpc;

import java.math.BigInteger;

/**
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public abstract class AbstractJsonRpcReply {

	private final String jsonrpc = JsonRpcSocket.JSON_RPC_VERSION;
	private final BigInteger id;

	public AbstractJsonRpcReply(BigInteger id) {
		this.id = id;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public BigInteger getId() {
		return id;
	}

}
