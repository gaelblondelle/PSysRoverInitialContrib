package org.polarsys.rover.samples.webapp.internal;

import java.io.IOException;

import org.polarsys.rover.driver.motor.IMotorController;
import org.polarsys.rover.driver.servo.IServoController;
import org.polarsys.rover.samples.webapp.IRoverController;

/**
 * The default {@link IRoverController} implementation.
 * 
 * @author Ralf Ellner - Initial contribution and API.
 * 
 */
public class RoverControllerImpl implements IRoverController {

	private final IServoController panServo;
	private final IServoController tiltServo;

	private final IMotorController leftMotor;
	private final IMotorController rightMotor;

	private int desiredSpeed;
	private int desiredTurnRate;

	public RoverControllerImpl(IMotorController leftMotor, IMotorController rightMotor, IServoController panServo,
			IServoController tiltServo) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;

		this.panServo = panServo;
		this.tiltServo = tiltServo;
	}

	@Override
	public void setSpeed(int speed) throws IOException {
		desiredSpeed = speed;
		updateMotors();
	}

	private void updateMotors() throws IOException {
		final int leftSpeed = limit(desiredSpeed - desiredTurnRate);
		final int rightSpeed = limit(desiredSpeed + desiredTurnRate);

		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
	}

	private int limit(int value) {
		if (value > IMotorController.SPEED_MAX_FORWARD) {
			return IMotorController.SPEED_MAX_FORWARD;
		} else if (value < IMotorController.SPEED_MAX_BACKWARD) {
			return IMotorController.SPEED_MAX_BACKWARD;
		}

		return value;
	}

	@Override
	public void setTurnRate(int rate) throws IOException {
		desiredTurnRate = rate;
		updateMotors();
	}

	@Override
	public void setPan(int pan) throws IOException {
		panServo.setPosition(pan);
	}

	@Override
	public void setTilt(int tilt) throws IOException {
		tiltServo.setPosition(tilt);
	}

	@Override
	public void stop() throws IOException {
		desiredSpeed = 0;
		desiredTurnRate = 0;
		updateMotors();
	}

	@Override
	public void close() throws IOException {
		panServo.close();
		tiltServo.close();

		leftMotor.close();
		rightMotor.close();
	}

	@Override
	public void open() throws IOException {
		stop();
		setPan(0);
		setTilt(0);
	}
}
