package org.polarsys.rover.driver.motor;

/**
 * Interface for the configuration of a servo controller.
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public interface IMotorControllerConfiguration {

	/**
	 * <code>true</code> if the motor direction shall be reversed, <code>false</code> otherwise.
	 * 
	 * @return
	 */
	boolean isReversed();

}
