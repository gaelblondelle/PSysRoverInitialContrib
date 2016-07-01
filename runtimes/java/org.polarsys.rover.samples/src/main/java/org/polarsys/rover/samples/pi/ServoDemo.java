package org.polarsys.rover.samples.pi;

import java.io.IOException;

import org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator;
import org.polarsys.rover.driver.pi.servo.internal.ServoControllerImpl;
import org.polarsys.rover.driver.pwmgen.IPWMOutput;
import org.polarsys.rover.driver.servo.IServoController;
import org.polarsys.rover.driver.servo.IServoControllerConfiguration;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * This application demonstrates the use of {@link ServoControllerImpl}s with a PCA9685 PWM generator. See the constants
 * defined in this class how to wire your hardware or adapt the settings to your setup.
 * 
 * The application sets the servo each second to a random position (100 times).
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public class ServoDemo {

	/**
	 * The I2C address of the PCA9685 PWM generator.
	 */
	private static final int PCA9685_DEVICE_I2C_ADDRESS = 0x40;

	/**
	 * The 0-based output number of the PCA9685 to which the servo is attached.
	 */
	private static final int SERVO_PWM_OUTPUT = 0;

	public static void main(String[] args) throws IOException, InterruptedException {

		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		PCA9685PWMGenerator driver = null;

		try {
			I2CDevice device = bus.getDevice(PCA9685_DEVICE_I2C_ADDRESS);
			driver = new PCA9685PWMGenerator(device);

			driver.open();
			driver.setFrequency(50);

			IPWMOutput output = driver.getOutput(SERVO_PWM_OUTPUT);
			IServoController controller = new ServoControllerImpl(output, new IServoControllerConfiguration() {

				@Override
				public boolean isReversed() {
					return false;
				}

				@Override
				public int getPositiveRate() {
					return 100;
				}

				@Override
				public int getOffset() {
					return 0;
				}

				@Override
				public int getNegativeRate() {
					return 100;
				}
			});

			for (int r = 0; r < 100; ++r) {

				int position = (int) (Math.random() * 1000.0 - 1.0);
				controller.setPosition(position);
				System.out.println("Position -> " + position);
				Thread.sleep(1000L);

			}

		} finally {
			if (null != driver) {
				driver.close();
			}
			bus.close();
		}

	}

}
