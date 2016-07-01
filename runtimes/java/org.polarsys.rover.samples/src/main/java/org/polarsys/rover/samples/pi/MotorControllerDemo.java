package org.polarsys.rover.samples.pi;

import java.io.IOException;

import org.polarsys.rover.driver.motor.IMotorController;
import org.polarsys.rover.driver.motor.IMotorControllerConfiguration;
import org.polarsys.rover.driver.pi.motor.internal.MotorControllerImpl;
import org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * This application demonstrates the use of two {@link MotorControllerImpl} with a PCA9685 PWM generator to control the
 * rover. See the constants defined in this class how to wire your hardware or adapt the settings to your setup.
 * 
 * The application is very simple and makes the rover drive in a zigzag course (100 zigzags).
 * 
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public class MotorControllerDemo {

	/**
	 * The I2C address of the PCA9685 PWM generator.
	 */
	private static final int PCA9685_DEVICE_I2C_ADDRESS = 0x40;

	/**
	 * The 0-based output number of the PCA9685 to which the left motor is attached.
	 */
	private static final int LEFT_MOTOR_PWM_OUTPUT = 14;

	/**
	 * The output pin to which the left motor's direction input pin is connected.
	 */
	private static final Pin LEFT_MOTOR_DIRECTION_PIN = RaspiPin.GPIO_07;

	/**
	 * The 0-based output number of the PCA9685 to which the right motor is attached.
	 */
	private static final int RIGHT_MOTOR_PWM_OUTPUT = 15;

	/**
	 * The output pin to which the right motor's direction input pin is connected.
	 */
	private static final Pin RIGHT_MOTOR_DIRECTION_PIN = RaspiPin.GPIO_00;

	public static void main(String[] args) throws IOException, InterruptedException {

		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		final GpioController gpio = GpioFactory.getInstance();

		PCA9685PWMGenerator driver = null;
		IMotorController leftMotor = null;
		IMotorController rightMotor = null;

		try {
			I2CDevice device = bus.getDevice(PCA9685_DEVICE_I2C_ADDRESS);
			driver = new PCA9685PWMGenerator(device);

			driver.open();
			driver.setFrequency(50);

			final GpioPinDigitalOutput directionPinLeft = gpio.provisionDigitalOutputPin(LEFT_MOTOR_DIRECTION_PIN,
					"Direction Left", PinState.LOW);
			directionPinLeft.setShutdownOptions(true, PinState.LOW);

			final GpioPinDigitalOutput directionPinRight = gpio.provisionDigitalOutputPin(RIGHT_MOTOR_DIRECTION_PIN,
					"Direction Right", PinState.LOW);
			directionPinRight.setShutdownOptions(true, PinState.LOW);

			leftMotor = new MotorControllerImpl(driver.getOutput(LEFT_MOTOR_PWM_OUTPUT), directionPinLeft,
					new IMotorControllerConfiguration() {
						@Override
						public boolean isReversed() {
							return false;
						}
					});
			rightMotor = new MotorControllerImpl(driver.getOutput(RIGHT_MOTOR_PWM_OUTPUT), directionPinRight,
					new IMotorControllerConfiguration() {
						@Override
						public boolean isReversed() {
							return false;
						}
					});

			for (int r = 0; r < 100; ++r) {

				leftMotor.setSpeed(500);
				rightMotor.setSpeed(500);

				Thread.sleep(2000L);

				leftMotor.setSpeed(200);
				rightMotor.setSpeed(-200);

				Thread.sleep(2000L);

				leftMotor.setSpeed(500);
				rightMotor.setSpeed(500);

				Thread.sleep(2000L);

				leftMotor.setSpeed(-200);
				rightMotor.setSpeed(200);

				Thread.sleep(2000L);
			}

		} finally {
			if (leftMotor != null) {
				leftMotor.close();
			}

			if (rightMotor != null) {
				rightMotor.close();
			}

			gpio.shutdown();

			if (driver != null) {
				driver.close();
			}

			bus.close();
		}

	}

}
