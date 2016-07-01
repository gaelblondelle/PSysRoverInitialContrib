package org.polarsys.rover.samples.pi.webapp.internal;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.rover.driver.motor.IMotorController;
import org.polarsys.rover.driver.servo.IServoController;
import org.polarsys.rover.samples.webapp.internal.RoverControllerImpl;

@RunWith(MockitoJUnitRunner.class)
public class RoverControllerImplTest {

	@Mock
	IMotorController leftMotor;

	@Mock
	IMotorController rightMotor;

	@Mock
	IServoController panServo;

	@Mock
	IServoController tiltServo;

	RoverControllerImpl fixture;

	@Before
	public void setUp() {
		fixture = new RoverControllerImpl(leftMotor, rightMotor, panServo, tiltServo);
	}

	@After
	public void tearDown() throws Exception {
		fixture.close();
	}

	@Test
	public void testForward() throws IOException {
		fixture.setSpeed(500);

		verify(leftMotor).setSpeed(500);
		verify(rightMotor).setSpeed(500);
	}

	@Test
	public void testBackward() throws IOException {
		fixture.setSpeed(-500);

		verify(leftMotor).setSpeed(-500);
		verify(rightMotor).setSpeed(-500);
	}

	@Test
	public void testTurnCW() throws IOException {
		fixture.setTurnRate(500);

		verify(leftMotor).setSpeed(-500);
		verify(rightMotor).setSpeed(500);
	}

	@Test
	public void testTurnCCW() throws IOException {
		fixture.setTurnRate(-500);

		verify(leftMotor).setSpeed(500);
		verify(rightMotor).setSpeed(-500);
	}

	@Test
	public void testLimitUpper() throws IOException {
		fixture.setSpeed(800);
		reset(leftMotor);
		reset(rightMotor);

		fixture.setTurnRate(500);
		verify(leftMotor).setSpeed(300);
		verify(rightMotor).setSpeed(1000);
	}

	@Test
	public void testLimitLower() throws IOException {
		fixture.setSpeed(-800);
		reset(leftMotor);
		reset(rightMotor);

		fixture.setTurnRate(500);
		verify(leftMotor).setSpeed(-1000);
		verify(rightMotor).setSpeed(-300);
	}
}
