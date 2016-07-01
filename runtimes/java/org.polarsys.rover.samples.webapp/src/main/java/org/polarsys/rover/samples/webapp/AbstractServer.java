package org.polarsys.rover.samples.webapp;

import java.io.Closeable;
import java.io.IOException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.polarsys.rover.samples.webapp.internal.RoverServlet;
import org.polarsys.rover.samples.webapp.pi.PiServer;

public abstract class AbstractServer implements Closeable {

	private static final int HTTP_PORT = 8000;

	protected abstract IRoverController createController() throws IOException;

	public void run() {

		IRoverController controller;

		try {
			controller = createController();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(HTTP_PORT);
		server.addConnector(connector);

		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		server.setHandler(servletContextHandler);

		// Add a websocket to a specific path spec
		ServletHolder holder = new ServletHolder("ws-events", new RoverServlet(controller));
		servletContextHandler.addServlet(holder, "/events/*");

		// client folder on classpath
		String clientDir = PiServer.class.getClassLoader().getResource("client").toExternalForm();

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });

		resourceHandler.setResourceBase(clientDir);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, servletContextHandler, new DefaultHandler() });
		server.setHandler(handlers);

		try {
			server.start();
			server.dump(System.err);
			server.join();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}

}
