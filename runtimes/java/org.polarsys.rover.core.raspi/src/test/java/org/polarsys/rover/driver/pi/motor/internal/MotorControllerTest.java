package org.polarsys.rover.driver.pi.motor.internal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.rover.driver.motor.IMotorController;
import org.polarsys.rover.driver.motor.IMotorControllerConfiguration;
import org.polarsys.rover.driver.pwmgen.IPWMOutput;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

@RunWith(MockitoJUnitRunner.class)
public class MotorControllerTest {

	private static final int CYCLE_COUNT = 4096;

	@Mock
	GpioPinDigitalOutput directionPin;

	@Mock
	IPWMOutput pwmOutput;

	@Mock
	IMotorControllerConfiguration configuration;

	@InjectMocks
	MotorControllerImpl motorController;

	@Before
	public void setUp() {
		when(pwmOutput.getCycleCount()).thenReturn(CYCLE_COUNT);
	}

	@Test
	public void testSetSpeedStop() throws IOException {
		when(configuration.isReversed()).thenReturn(false);

		motorController.setSpeed(IMotorController.SPEED_STOP);

		verify(directionPin).setState(PinState.HIGH);
		verify(pwmOutput).setPWM(0);
	}

	@Test
	public void testSetSpeedMaxForward() throws IOException {
		when(configuration.isReversed()).thenReturn(false);

		motorController.setSpeed(IMotorController.SPEED_MAX_FORWARD);

		verify(directionPin).setState(PinState.HIGH);
		verify(pwmOutput).setPWM(CYCLE_COUNT);
	}

	@Test
	public void testSetSpeedMaxBackward() throws IOException {
		when(configuration.isReversed()).thenReturn(false);

		motorController.setSpeed(IMotorController.SPEED_MAX_BACKWARD);

		verify(directionPin).setState(PinState.LOW);
		verify(pwmOutput).setPWM(CYCLE_COUNT);
	}

	@Test
	public void testSetSpeedStopReversed() throws IOException {
		when(configuration.isReversed()).thenReturn(true);

		motorController.setSpeed(IMotorController.SPEED_STOP);

		verify(directionPin).setState(PinState.LOW);
		verify(pwmOutput).setPWM(0);
	}

	@Test
	public void testSetSpeedMaxForwardReversed() throws IOException {
		when(configuration.isReversed()).thenReturn(true);

		motorController.setSpeed(IMotorController.SPEED_MAX_FORWARD);

		verify(directionPin).setState(PinState.LOW);
		verify(pwmOutput).setPWM(CYCLE_COUNT);
	}

	@Test
	public void testSetSpeedMaxBackwardReversed() throws IOException {
		when(configuration.isReversed()).thenReturn(true);

		motorController.setSpeed(IMotorController.SPEED_MAX_BACKWARD);

		verify(directionPin).setState(PinState.HIGH);
		verify(pwmOutput).setPWM(CYCLE_COUNT);
	}

}
