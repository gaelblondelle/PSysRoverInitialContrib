package org.polarsys.rover.samples.webapp.internal.rpc;

import java.math.BigInteger;

/**
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public class JsonRpcReply extends AbstractJsonRpcReply {

	private final Object result;

	public JsonRpcReply(BigInteger id, Object result) {
		super(id);
		this.result = result;
	}

	public Object getResult() {
		return result;
	}

}
