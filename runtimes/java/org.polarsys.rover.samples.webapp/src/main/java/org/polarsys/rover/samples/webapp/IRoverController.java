package org.polarsys.rover.samples.webapp;

import java.io.Closeable;
import java.io.IOException;

/**
 * An interface to control the rover and its camera.
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public interface IRoverController extends Closeable {

	/**
	 * Set the speed value.
	 * 
	 * @param speed
	 *            The speed value. Allowed rage is -1000 to 1000. A value of 1000 means maximum speed forwards. A value
	 *            of -1000 means maximum speed backwards. A value of 0 means no movement.
	 * 
	 * @throws IOException
	 */
	void setSpeed(int speed) throws IOException;

	/**
	 * Set the turn rate. The turn rate may be independent of the actual speed {@link #setSpeed(Number)} of the rover,
	 * i.e., the rover may be able to turn in place. However, the turn rate may influence the maximum achievable speed,
	 * e.g., a tracked vehicle is not able to achieve its maximum speed if the turn rate is non-zero.
	 *
	 * @param turnRate
	 *            The turn rate. Allowed rage is -1000 to 1000. A value of 1000 means maximum turn rate clockwise. A
	 *            value of -1000 means maximum turn rate counter-clockwise. A value of 0 means no turning.
	 * @throws IOException
	 */
	void setTurnRate(int rate) throws IOException;

	/**
	 * Set the pan angle of the camera.
	 * 
	 * @param pan
	 *            The desired pan angle. Allowed rage is -1000 to 1000. A positive value means an angle in clockwise
	 *            direction.
	 * @throws IOException
	 */
	void setPan(int pan) throws IOException;

	/**
	 * Set the tilt angle of the camera.
	 * 
	 * @param tilt
	 *            The desired tilt angle. Allowed rage is -1000 to 1000. A positive value means an angle upwards.
	 * @throws IOException
	 */
	void setTilt(int tilt) throws IOException;

	/**
	 * Stop the rover.
	 * 
	 * @throws IOException
	 */
	void stop() throws IOException;

	/**
	 * Initialize the controller.
	 * 
	 * @throws IOException
	 */
	void open() throws IOException;
}