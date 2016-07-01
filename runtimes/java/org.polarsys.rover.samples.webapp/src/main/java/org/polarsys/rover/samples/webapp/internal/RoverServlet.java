package org.polarsys.rover.samples.webapp.internal;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.polarsys.rover.samples.webapp.IRoverController;

/**
 * 
 * @author Ralf Ellner - Initial contribution and API.
 * 
 */
@SuppressWarnings("serial")
public class RoverServlet extends WebSocketServlet {

	private final IRoverController controller;

	public RoverServlet(IRoverController controller) {
		this.controller = controller;
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.setCreator((ServletUpgradeRequest req, ServletUpgradeResponse resp) -> new RoverSocket(controller));
	}
}