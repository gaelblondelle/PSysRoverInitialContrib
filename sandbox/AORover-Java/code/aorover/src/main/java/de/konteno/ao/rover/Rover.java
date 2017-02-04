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

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiBcmPin;


public class Rover {

	private GpioController gpio;
	private Motor motorLeft;
	private Motor motorRight;
	private RoverDirection direction;


	public Rover() {

		gpio = GpioFactory.getInstance();
		// setShutdownOptions() on (all of, entire) gpio, not just e.g. the enableGpioPin
		gpio.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

		// motors attached to MC33926 Pololu board
		motorRight = new Motor(RaspiBcmPin.GPIO_12, RaspiBcmPin.GPIO_24, RaspiBcmPin.GPIO_22);
		motorLeft = new Motor(RaspiBcmPin.GPIO_13, RaspiBcmPin.GPIO_25, RaspiBcmPin.GPIO_23);
	}

	public void forward(int speed) {
		drive(speed, RoverDirection.FORWARD);
	}

	public void backward(int speed) {
		drive(speed , RoverDirection.BACKWARD);
	}

	private void drive(int speed, RoverDirection direction) {
		this.direction = direction;
		motorRight.setSpeed(speed * direction.value());
		motorLeft.setSpeed(speed * direction.value());
	}

	public void stop() {
		forward(0);
	}

	public void end() {
		forward(0);
		gpio.shutdown();
	}


	public void hardLeft(int speed) {
		turnLeft(speed, true);
	}

	public void hardRight(int speed) {
		turnRight(speed, true);
	}

	public void softLeft(int speed) {
		turnLeft(speed, false);
	}

	public void softRight(int speed) {
		turnRight(speed, false);
	}


	public void turnLeft(int speed, boolean hard) {
		int f = hard ? -1 : 0;
		int mot = direction.value() * speed;
		if (RoverDirection.FORWARD.equals(direction)) {
			turn(f * mot, mot);	
		} else {
			turn(mot, f * mot);	
		}
	}

	public void turnRight(int speed, boolean hard) {
		int f = hard ? -1 : 0;
		int mot = direction.value() * speed;
		if (RoverDirection.FORWARD.equals(direction)) {
			turn(mot, f * mot);	
		} else {
			turn(f * mot, mot);	
		}	
	}


	private void turn(int speedLeft, int speedRight) {
		motorLeft.setSpeed(speedLeft);
		motorRight.setSpeed(speedRight);		
	}

}
