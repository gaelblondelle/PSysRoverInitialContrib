package org.polarsys.rover.samples.webapp;

import java.io.IOException;

import org.polarsys.rover.samples.webapp.internal.DummyRoverControllerImpl;

/**
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public class DummyServer extends AbstractServer {

	public static void main(String[] args) throws IOException {
		try (DummyServer server = new DummyServer()) {
			server.run();
		}
	}

	@Override
	protected IRoverController createController() {
		return new DummyRoverControllerImpl();
	}

	@Override
	public void close() throws IOException {
	}
}