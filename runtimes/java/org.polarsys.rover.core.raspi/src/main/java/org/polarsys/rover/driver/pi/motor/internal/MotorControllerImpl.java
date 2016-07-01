package org.polarsys.rover.driver.pi.motor.internal;

import java.io.IOException;

import org.polarsys.rover.driver.motor.IMotorController;
import org.polarsys.rover.driver.motor.IMotorControllerConfiguration;
import org.polarsys.rover.driver.pwmgen.IPWMOutput;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/**
 * Simple implementation of the {@link IMotorController} interface that uses a single I/O Port to switch motor direction
 * and an {@link IPWMOutput} to set the motor speed.
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public class MotorControllerImpl implements IMotorController {

	private final IPWMOutput output;
	private final GpioPinDigitalOutput directionPin;
	private final IMotorControllerConfiguration configuration;

	public MotorControllerImpl(IPWMOutput output, GpioPinDigitalOutput directionPin,
			IMotorControllerConfiguration configuration) {
		this.output = output;
		this.directionPin = directionPin;
		this.configuration = configuration;
	}

	@Override
	public void setSpeed(int speed) throws IOException {
		directionPin.setState(speed < 0 ^ configuration.isReversed() ? PinState.LOW : PinState.HIGH);
		int pwm = (output.getCycleCount() * Math.abs(speed)) / SPEED_MAX_FORWARD;
		output.setPWM(pwm);
	}

	@Override
	public void close() throws IOException {
		output.setPWM(0);
	}

}
