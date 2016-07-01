package org.polarsys.rover.driver.motor;

import java.io.Closeable;
import java.io.IOException;

/**
 * A driver interface for motor controllers.
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public interface IMotorController extends Closeable {

	/**
	 * Constant for stopping the motor.
	 */
	public static int SPEED_STOP = 0;

	/**
	 * Constant for maximum forward speed.
	 */
	public static int SPEED_MAX_FORWARD = 1000;

	/**
	 * Constant for maximum backward speed.
	 */
	public static int SPEED_MAX_BACKWARD = -1 * SPEED_MAX_FORWARD;

	/**
	 * Set the motor speed and direction.
	 * 
	 * @param speed
	 *            A value between {@link #SPEED_MAX_FORWARD} and {@link #SPEED_MAX_BACKWARD}. {@link #SPEED_STOP} stops
	 *            the motor.
	 * @throws IOException
	 */
	void setSpeed(int speed) throws IOException;

}
