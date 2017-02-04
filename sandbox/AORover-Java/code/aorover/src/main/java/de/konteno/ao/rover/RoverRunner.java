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

import java.io.IOException;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.RaspiPinNumberingScheme;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import de.konteno.ao.rover.adc.Mcp3008;
import de.konteno.ao.rover.compass.Compass;
import de.konteno.ao.rover.compass.HMC5883L;
import de.konteno.ao.rover.compass.HMC5883LCalibration;
import de.konteno.ao.rover.compass.HMC5883LConfig;

public class RoverRunner {

	private static Rover rover;
	private static Compass compass;
	private static Mcp3008 adc;     // AD converter
	private static Sharp0A41SK ir;  // infrared distance sensor
	private static long start = System.currentTimeMillis();

	public static void main(String[] args) throws Exception {
		printCommands();

		GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
		
		GpioPinDigitalOutput led = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_17);
		led.setState(PinState.HIGH);		

		// Initialize SPI and I2C bus:
		I2CBus iic1 = I2CFactory.getInstance(I2CBus.BUS_1);

		SpiDevice spi0 = SpiFactory.getInstance(
				SpiChannel.CS0,
				SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
                SpiDevice.DEFAULT_SPI_MODE);
		

		rover = new Rover();

		// create the concrete instance of the compass chip:
		compass = new HMC5883L(iic1.getDevice(0x1e), HMC5883LConfig.Default);
		
		adc = new Mcp3008(spi0);
		ir = new Sharp0A41SK(adc, (short) 0);

		roverTest();
		
		led.setState(PinState.LOW);	
		rover.end();
		iic1.close();
	}
	
	private static void printCommands() {
		System.out.println("---------------------------");
		System.out.println("Hi - I am the Eclipse Rover!");
		System.out.println("---------------------------");
		System.out.println("Try the following comands:");
		System.out.println("    i -> drive forward");
		System.out.println("    m -> drive backward");
		System.out.println("    j -> hard-left");
		System.out.println("    J -> soft left");
		System.out.println("    k -> hard-right");
		System.out.println("    K -> soft-right");
		System.out.println("    t -> calibrate compass");
		System.out.println("    k -> read distance sensor");
		System.out.println("    s -> stop");
		System.out.println("    q -> quit");
		System.out.println("    ... some more");

		
	}

	private static void time() {
		//System.out.println(System.currentTimeMillis() - start);
		start  = System.currentTimeMillis();
	}

	
	private static void roverTest() throws Exception {
		
		int speed = Motor.MAX_SPEED;
		
		boolean repeat = true;
		while (repeat) {
			char input = (char) System.in.read();
			time();
			
			switch (input) {
			case 'a':
				turnByAngle(Motor.MAX_SPEED, 45);
				break;
			case 'n':
				goNorth(Motor.MAX_SPEED);
				break;
			case 'b':
				System.out.println(String.format("bearing: %3.1f", compass.readBearing()));
				break;
			
			case 'i':
				System.out.println("forward");
				rover.forward(Motor.MAX_SPEED);
				break;

			case 'j':
				System.out.println("hard-left");
				rover.hardLeft(Motor.MAX_SPEED);
				break;

			case 'J':
				System.out.println("soft-left");
				rover.softLeft(Motor.MAX_SPEED);
				break;

			case 'k':
				System.out.println("hard-right");
				rover.hardRight(Motor.MAX_SPEED);
				break;

			case 'K':
				System.out.println("soft-right");
				rover.softRight(Motor.MAX_SPEED);
				break;

			case 'm':
				System.out.println("backward");
				rover.backward(Motor.MAX_SPEED);
				break;

			case '+':
				speed += 40;
				System.out.println("forward at " + speed);
				rover.forward(speed);
				break;
			case '-':
				speed -= 40;
				System.out.println("forward at " + speed);
				rover.forward(speed);
				break;

			case 't':
				calibrateCompass();
				break;

				
			case 's':
				System.out.println("stop");
				rover.stop();
				break;
				
			case 'r':
				System.out.println("read IR sensor");
				for (int i=0; i<100; ++i) {
					float d = ir.getDistance();
					System.out.println(String.format("distance = %3.2f", d));
					Thread.sleep(150);
				}
				break;

			
			case 'q':
				System.out.println("quit");
				rover.end();
				repeat = false;
				break;
				
			}
		}
		
	}

	private static void calibrateCompass() throws InterruptedException {
		rover.stop();
		
		System.out.println("start calibration");
		HMC5883LCalibration calib = new HMC5883LCalibration((HMC5883L) compass);
		Thread t = new Thread(calib);
		t.start();
		rover.hardLeft(Motor.MAX_SPEED);
		
		int delay=12000;
		System.out.println(String.format("turning for %dms", delay));
		Thread.sleep(delay);
		
		calib.setDone(true);
		rover.stop();
		t.join();	
		System.out.println("end calibration");
	}	
	
	private static void turnByAngle(int speed, double angle) {
		try {
			double currentAngle = compass.readBearing();
			double stopAngle = (currentAngle + angle) % 360;
			System.out.println("*Start: " + currentAngle + "Stop: " + stopAngle);
			
			while (Math.abs(stopAngle - currentAngle) > 5) {
				
				if (angle > 0) {
					rover.hardRight(speed);
				} else {
					rover.hardLeft(speed);
				}
				Thread.sleep(30);
				currentAngle = compass.readBearing();
				System.out.println(System.currentTimeMillis() +  " - currentAngle: " + currentAngle);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			rover.stop();
		}
	}
	
	public static void goNorth(int speed) {
		
		try {
			double currentAngle = compass.readBearing();
			
			if (currentAngle <=180) {
				turnByAngle(speed, -1*currentAngle);
			} else {
				turnByAngle(speed, 360-currentAngle);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
