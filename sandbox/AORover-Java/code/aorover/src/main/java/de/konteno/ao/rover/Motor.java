/*
 * Copyright (c) 2017 Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Authors:
 *    Angelika Wittek, Oliver Springauf
 * Contributors:
*/
package de.konteno.ao.rover;

import static com.pi4j.wiringpi.Gpio.PWM_MODE_MS;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;

public class Motor {

	public static final int MAX_SPEED = 480; // 19.2 MHz / 2 / 480 = 20 kHz
	public static final int MID_SPEED = 380; 
	public static final int MIN_SPEED = 240; 
	
	private GpioPinPwmOutput pwmPin;
	private GpioPinDigitalOutput directionPin;
	private GpioPinDigitalOutput enablePin;

	private boolean enabled;
	
	private int motorSpeed;
	
	public Motor(Pin pwm, Pin direction, Pin enable) {
		GpioController gpio = GpioFactory.getInstance();

		Gpio.pwmSetMode(PWM_MODE_MS);
		Gpio.pwmSetRange(MAX_SPEED);
		Gpio.pwmSetClock(2);

		pwmPin = gpio.provisionPwmOutputPin(pwm, "pwm");
		pwmPin.setPwmRange(MAX_SPEED);

		directionPin = gpio.provisionDigitalOutputPin(direction, "direction");
		enablePin = gpio.provisionDigitalOutputPin(enable, "enable", PinState.LOW);
	}
	
	public void setEnabled(boolean ena) {		
		if (ena != enabled)
		{
			enabled = ena;
			enablePin.setState(enabled ? PinState.HIGH : PinState.LOW);
		}
	}

	public void setSpeed(int speed) {
		setEnabled(speed != 0);
		
		if (motorSpeed != speed) {
			
			motorSpeed = speed;
			
			speed = Math.abs(speed);
			speed = Math.min(MAX_SPEED, speed);
			pwmPin.setPwm(speed);
			
			// 0: forward, 1: backward
			directionPin.setState(motorSpeed < 0);
		}

	}

	public int getMotorSpeed() {
		return motorSpeed;
	}

}
