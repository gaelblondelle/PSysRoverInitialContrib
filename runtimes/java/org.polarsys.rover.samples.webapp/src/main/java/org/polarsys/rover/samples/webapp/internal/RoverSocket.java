package org.polarsys.rover.samples.webapp.internal;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.polarsys.rover.samples.webapp.IRoverController;
import org.polarsys.rover.samples.webapp.internal.rpc.JsonRpcSocket;
import org.polarsys.rover.samples.webapp.internal.rpc.Watchdog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Ralf Ellner - Initial contribution and API.
 * 
 */
public class RoverSocket extends JsonRpcSocket {

	private static final long WATCHDOG_TIMEOUT_MS = 1000L;
	private static final Logger LOG = LoggerFactory.getLogger(RoverSocket.class);

	private final Watchdog watchdog;
	private final IRoverController controller;

	public RoverSocket(IRoverController controller) {
		this.controller = controller;
		watchdog = new Watchdog(WATCHDOG_TIMEOUT_MS);

		watchdog.addHandler(() -> {
			try {
				stop();
			} catch (IOException e) {
				LOG.error("Could not stop rover", e);
			}
		});
	}

	/**
	 * Immediately stop the rover.
	 * 
	 * @throws IOException
	 */
	public void stop() throws IOException {
		LOG.trace("Stopping rover");
		controller.stop();
	}

	/**
	 * Resets the communication watchdog. This method must be called regularly, at least every
	 * {@value #WATCHDOG_TIMEOUT_MS} ms. Otherwise, the rover is stopped.
	 */
	public void resetWatchdog() {
		LOG.trace("Reset watchdog");
		watchdog.reset();
	}

	/**
	 * Set the speed value.
	 * 
	 * @param speed
	 *            The speed value. Allowed rage is -1000 to 1000. A value of 1000 means maximum speed forwards. A value
	 *            of -1000 means maximum speed backwards. A value of 0 means no movement.
	 * 
	 * @throws IOException
	 */
	public void setSpeed(Number speed) throws IOException {
		final int speedValue = speed.intValue();

		LOG.trace("Set speed to {} %", speedValue / 10);
		controller.setSpeed(speedValue);
	}

	/**
	 * Set the turn rate. The turn rate may be independent of the actual speed {@link #setSpeed(Number)} of the rover,
	 * i.e., the rover may be able to turn in place. However, the turn rate may influence the maximum achievable speed,
	 * e.g., a tracked vehicle is not able to achieve its maximum speed if the turn rate is non-zero.
	 *
	 * @param turnRate
	 *            The turn rate. Allowed range is -1000 to 1000. A value of 1000 means maximum turn rate clockwise. A
	 *            value of -1000 means maximum turn rate counter-clockwise. A value of 0 means no turning.
	 * @throws IOException
	 */
	public void setTurnRate(Number turnRate) throws IOException {
		final int turnRateValue = turnRate.intValue();

		LOG.trace("Set turn rate to {} %", turnRateValue / 10);
		controller.setTurnRate(turnRateValue);
	}

	/**
	 * Set the pan angle of a camera.
	 * 
	 * @param panAngle
	 *            The desired angle. Allowed range is -1000 to 1000. A positive value means an angle in clockwise direction.
	 * @throws IOException
	 */
	public void setPan(Number panAngle) throws IOException {
		final int panAngleValue = panAngle.intValue();

		LOG.trace("Set pan to {} %", panAngleValue / 10);

		controller.setPan(panAngleValue);
	}

	/**
	 * Set the tilt angle of a camera.
	 * 
	 * @param tiltAngle
	 *             The desired angle. Allowed range is -1000 to 1000. A positive value means an angle upwards.
	 * @throws IOException
	 */
	public void setTilt(Number tiltAngle) throws IOException {
		final int tiltAngleValue = tiltAngle.intValue();

		LOG.trace("Set tilt to {} %", tiltAngleValue / 10);

		controller.setTilt(tiltAngleValue);
	}

	@Override
	public void onWebSocketConnect(Session session) {

		watchdog.start();

		try {
			controller.open();
		} catch (IOException e) {
			LOG.error("Could not init rover", e);
		}

		super.onWebSocketConnect(session);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);

		watchdog.shutdown();

		try {
			controller.close();
		} catch (IOException e) {
			LOG.error("Could not close rover", e);
		}
	}

}