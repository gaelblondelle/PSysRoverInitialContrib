package org.polarsys.rover.driver.pi.servo.internal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.rover.driver.pwmgen.IPWMOutput;
import org.polarsys.rover.driver.servo.IServoController;
import org.polarsys.rover.driver.servo.IServoControllerConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class ServoControllerTest {

	@Mock
	IPWMOutput pwmOutput;

	@Mock
	IServoControllerConfiguration configuration;

	ServoControllerImpl servoController;

	@Before
	public void setUp() throws Exception {
		when(pwmOutput.getFrequency()).thenReturn(50);
		when(pwmOutput.getCycleCount()).thenReturn(4096);

		servoController = new ServoControllerImpl(pwmOutput, configuration);
	}

	@After
	public void tearDown() throws Exception {
	}

	private void setConfig(boolean reversed, int offset, int posRate, int negRate) {
		when(configuration.reversed()).thenReturn(reversed);
		when(configuration.offset()).thenReturn(offset);
		when(configuration.positiveRate()).thenReturn(posRate);
		when(configuration.negativeRate()).thenReturn(negRate);
	}

	@Test
	public void testSetPositionNeutral() throws IOException {
		setConfig(false, 0, 100, 100);

		servoController.setPosition(IServoController.POS_NEUTRAL);

		verify(pwmOutput).setPWM(375);
	}

	@Test
	public void testSetPositionMax() throws IOException {
		setConfig(false, 0, 100, 100);

		servoController.setPosition(IServoController.POS_MAX);

		verify(pwmOutput).setPWM(500);
	}

	@Test
	public void testSetPositionMin() throws IOException {
		setConfig(false, 0, 100, 100);

		servoController.setPosition(IServoController.POS_MIN);

		verify(pwmOutput).setPWM(250);
	}

	@Test
	public void testSetPositionNeutralReversed() throws IOException {
		setConfig(true, 0, 100, 100);

		servoController.setPosition(IServoController.POS_NEUTRAL);

		verify(pwmOutput).setPWM(375);
	}

	@Test
	public void testSetPositionMaxReversed() throws IOException {
		setConfig(true, 0, 100, 100);

		servoController.setPosition(IServoController.POS_MAX);

		verify(pwmOutput).setPWM(250);
	}

	@Test
	public void testSetPositionMinReversed() throws IOException {
		setConfig(true, 0, 100, 100);

		servoController.setPosition(IServoController.POS_MIN);

		verify(pwmOutput).setPWM(500);
	}

	@Test
	public void testSetPositionNeutralOffset() throws IOException {
		setConfig(false, 500, 100, 100);

		servoController.setPosition(IServoController.POS_NEUTRAL);

		verify(pwmOutput).setPWM(437);
	}

	@Test
	public void testSetPositionMaxOffset() throws IOException {
		setConfig(false, 500, 100, 100);

		servoController.setPosition(IServoController.POS_MAX);

		verify(pwmOutput).setPWM(550); // Limited
	}

	@Test
	public void testSetPositionMinOffset() throws IOException {
		setConfig(false, 500, 100, 100);

		servoController.setPosition(IServoController.POS_MIN);

		verify(pwmOutput).setPWM(313);
	}

}
