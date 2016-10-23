package org.polarsys.rover.driver.servo;

/**
 * Interface for the configuration of a servo controller.
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public interface IServoControllerConfiguration {

	/**
	 * <code>true</code> if the servo direction shall be reversed, <code>false</code> otherwise.
	 * 
	 * @return
	 */
	boolean reversed();

	/**
	 * The offset of the zero position from the servo's hardware neutral position.
	 * 
	 * @return
	 */
	int offset();

	/**
	 * A scale factor in percent for the angle to which a positive position (set via
	 * {@link IServoController#setPosition(int)}) corresponds. This rate may be used to reduce or extend the servo's
	 * throw.
	 * 
	 * @return
	 */
	int positiveRate();

	/**
	 * A scale factor in percent for the angle to which a negative position (set via
	 * {@link IServoController#setPosition(int)}) corresponds. This rate may be used to reduce or extend the servo's
	 * throw.
	 * 
	 * @return
	 */
	int negativeRate();

}
