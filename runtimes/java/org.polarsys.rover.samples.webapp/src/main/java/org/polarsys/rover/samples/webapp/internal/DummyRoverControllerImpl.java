package org.polarsys.rover.samples.webapp.internal;

import java.io.IOException;

import org.polarsys.rover.samples.webapp.IRoverController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link IRoverController} implementation that logs all method calls to a {@link Logger}. This implementation is
 * hardware independent and can be used for testing on a developer machine.
 * 
 * @author Ralf Ellner - Initial contribution and API.
 * 
 */
public class DummyRoverControllerImpl implements IRoverController {

	private static final Logger LOG = LoggerFactory.getLogger(DummyRoverControllerImpl.class);

	@Override
	public void close() throws IOException {
		LOG.info("RoverController#close()");
	}

	@Override
	public void setSpeed(int speed) throws IOException {
		LOG.info("RoverController#setSpeed({})", speed);
	}

	@Override
	public void setTurnRate(int rate) throws IOException {
		LOG.info("RoverController#setTurnRate({})", rate);
	}

	@Override
	public void setPan(int pan) throws IOException {
		LOG.info("RoverController#setPan({})", pan);
	}

	@Override
	public void setTilt(int tilt) throws IOException {
		LOG.info("RoverController#setTilt({})", tilt);
	}

	@Override
	public void stop() throws IOException {
		LOG.info("RoverController#stop()");
	}

	@Override
	public void open() {
		LOG.info("RoverController#open()");
	}

}
