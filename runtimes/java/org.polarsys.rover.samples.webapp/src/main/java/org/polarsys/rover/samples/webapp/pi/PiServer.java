package org.polarsys.rover.samples.webapp.pi;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.reload.strategy.PeriodicalReloadStrategy;
import org.polarsys.rover.driver.motor.IMotorController;
import org.polarsys.rover.driver.motor.IMotorControllerConfiguration;
import org.polarsys.rover.driver.pi.motor.internal.MotorControllerImpl;
import org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator;
import org.polarsys.rover.driver.pi.servo.internal.ServoControllerImpl;
import org.polarsys.rover.driver.pwmgen.IPWMGenerator;
import org.polarsys.rover.driver.servo.IServoController;
import org.polarsys.rover.driver.servo.IServoControllerConfiguration;
import org.polarsys.rover.samples.webapp.AbstractServer;
import org.polarsys.rover.samples.webapp.IRoverController;
import org.polarsys.rover.samples.webapp.internal.RoverControllerImpl;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public class PiServer extends AbstractServer {

	/**
	 * The 0-based output number of the PCA9685 to which the tilt servo is attached.
	 */
	private static final int PWM_OUTPUT_TILT_SERVO = 0;

	/**
	 * The 0-based output number of the PCA9685 to which the tilt servo is attached.
	 */
	private static final int PWM_OUTPUT_PAN_SERVO = 1;

	/**
	 * The 0-based output number of the PCA9685 to which the right motor's motor driver PWM input is attached.
	 */
	private static final int PWM_OUTPUT_RIGHT_MOTOR = 15;

	/**
	 * The pin to which the right motor's motor driver direction input is attached.
	 */
	private static final Pin DIRECTION_PIN_RIGHT_MOTOR = RaspiPin.GPIO_00;

	/**
	 * The 0-based output number of the PCA9685 to which the left motor's motor driver PWM input is attached.
	 */
	private static final int PWM_OUTPUT_LEFT_MOTOR = 14;

	/**
	 * The pin to which the left motor's motor driver direction input is attached.
	 */
	private static final Pin DIRECTION_PIN_LEFT_MOTOR = RaspiPin.GPIO_07;

	/**
	 * The I2C address of the PCA9685 PWM generator.
	 */
	private static final int PCA9685_I2C_ADDRESS = 0x40;

	public static void main(String[] args) throws IOException {
		try (PiServer server = new PiServer()) {
			server.run();
		}
	}

	private I2CBus bus;

	private IRoverController controller;

	private IPWMGenerator pwmGenerator;

	@Override
	protected IRoverController createController() throws IOException {

		try {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);
		} catch (UnsupportedBusNumberException e) {
			throw new IOException(e);
		}

		I2CDevice device = bus.getDevice(PCA9685_I2C_ADDRESS);
		pwmGenerator = new PCA9685PWMGenerator(device);

		pwmGenerator.open();
		pwmGenerator.setFrequency(50);

		GpioController gpio = GpioFactory.getInstance();
		GpioPinDigitalOutput directionPinLeft = gpio.provisionDigitalOutputPin(DIRECTION_PIN_LEFT_MOTOR,
				"Direction Left", PinState.LOW);
		directionPinLeft.setShutdownOptions(true, PinState.LOW);

		GpioPinDigitalOutput directionPinRight = gpio.provisionDigitalOutputPin(DIRECTION_PIN_RIGHT_MOTOR,
				"Direction Right", PinState.LOW);
		directionPinRight.setShutdownOptions(true, PinState.LOW);

		ConfigurationSource source = new FilesConfigurationSource(
				() -> Collections.singletonList(Paths.get(System.getProperty("rover.cfg", "rover.properties"))));

		ConfigurationProvider provider = new ConfigurationProviderBuilder().withConfigurationSource(source)
				.withReloadStrategy(new PeriodicalReloadStrategy(5, TimeUnit.SECONDS)).build();

		IMotorController leftMotor = new MotorControllerImpl(pwmGenerator.getOutput(PWM_OUTPUT_LEFT_MOTOR),
				directionPinLeft, provider.bind("motorLeft", IMotorControllerConfiguration.class));
		IMotorController rightMotor = new MotorControllerImpl(pwmGenerator.getOutput(PWM_OUTPUT_RIGHT_MOTOR),
				directionPinRight, provider.bind("motorRight", IMotorControllerConfiguration.class));
		IServoController panServo = new ServoControllerImpl(pwmGenerator.getOutput(PWM_OUTPUT_PAN_SERVO),
				provider.bind("servoPan", IServoControllerConfiguration.class));
		IServoController tiltServo = new ServoControllerImpl(pwmGenerator.getOutput(PWM_OUTPUT_TILT_SERVO),
				provider.bind("servoTilt", IServoControllerConfiguration.class));

		controller = new RoverControllerImpl(leftMotor, rightMotor, panServo, tiltServo);

		return controller;

	}

	public void close() throws IOException {
		controller.close();
		pwmGenerator.close();
		bus.close();
	}
}