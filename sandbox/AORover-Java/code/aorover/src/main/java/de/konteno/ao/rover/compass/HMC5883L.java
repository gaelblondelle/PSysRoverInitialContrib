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
package de.konteno.ao.rover.compass;

import java.io.IOException;

import com.pi4j.io.i2c.I2CDevice;

/**
 * compass
 * GY-271 module with Honeywell HMC5883L magnetometer on I²C bus
 * default bus address is 0x1e
 * 
 * datasheet:
 * https://cdn-shop.adafruit.com/datasheets/HMC5883L_3-Axis_Digital_Compass_IC.pdf
 * https://learn.adafruit.com/adafruit-hmc5883l-breakout-triple-axis-magnetometer-compass-sensor/
 * 
 * Lessons learned:
 * - the magentometer MUST be calibrated! Otherwise the readings are meaningless.
 * - there might be sophisticated calibration methods, but for simple 2D orientation, an offset/scale should do.
 * - calibration must be done in the final robot configuration, otherwise you get wrong offset/scale values.
 * - keep a safe distance (~10cm) to the motors and the raspi, because this is a magnetometer and measures magnetic fields - that means
 *   it also measures the fields of your electronic devices 
 * 
 * see 
 * https://edwardmallon.wordpress.com/2015/05/22/calibrating-any-compass-or-accelerometer-for-arduino/
 * http://www.germersogorb.de/html/kalibrierung_des_hcm5883l.html
 * 
 */
public class HMC5883L implements Compass {
	// registers
	static final int REG_CONFIG_A = 0x00;
	static final int REG_CONFIG_B = 0x01;
	static final int REG_MODE     = 0x02;
	static final int REG_MSB_X    = 0x03;
	static final int REG_LSB_X    = 0x04;
	static final int REG_MSB_Z    = 0x05;
	static final int REG_LSB_Z    = 0x06;
	static final int REG_MSB_Y    = 0x07;
	static final int REG_LSB_Y    = 0x08;
	static final int REG_STATUS   = 0x09;
	static final int REG_ID_A     = 0x0a;
	static final int REG_ID_B     = 0x0b;
	static final int REG_ID_C     = 0x0c;


	static final int OVERFLOW = -4096;
	
	private I2CDevice device;

	public int offsetX = -89;
	public int offsetY = -99;
	public double scaleX = 1.5432;
	public double scaleY = 1.5748;
	public double offsetDegree = -348.8;


	public HMC5883L(I2CDevice device, HMC5883LConfig cfg) throws IOException, InterruptedException {
		this.device = device;
		configure(cfg);
	}
	

	/**
	 * should return "H43"
	 * @return
	 * @throws IOException
	 */
	public String readId() throws IOException  {
		String id = String.valueOf(
				new char[] {  
					(char)(device.read(REG_ID_A)),
					(char)(device.read(REG_ID_B)),
					(char)(device.read(REG_ID_C)),
					});
		return id;
	}
	
	private void configure(HMC5883LConfig cfg) throws IOException, InterruptedException {
		
		// configuration register A
		device.write(REG_CONFIG_A, (byte) (cfg.getSamples() << 5 | cfg.getDatarate() << 2 | cfg.getMeasurement()));
		
		// configuration register B
		device.write(REG_CONFIG_B, (byte) (cfg.getBias() << 5));

		// mode register - set to continuous measurement for now
		device.write(REG_MODE, (byte) cfg.getMode());
		
		// wait 1000/15(Hz) = 67ms for first measurement
		//int waitMillis = (int)(3100.0 * Math.exp((cfg.getDatarate() + 1) * (-0.765)));
		int waitMillis = 67;
		Thread.sleep(waitMillis);
	}
	
	
	/**
	 * Read three signed 16bit values (x, z, y) in 2's complement from the 6 value registers.
	 * Value range on each axis is 0xF800 (-2048) to 0x7FF (2047). 
	 * @return the magnetic field vector
	 * @throws IOException
	 */
	public short[] readXzy() throws IOException {
		int msb, lsb;
		msb = device.read(REG_MSB_X);
		lsb = device.read(REG_LSB_X);
		short x = (short) (msb << 8 | lsb);
		
		msb = device.read(REG_MSB_Z);
		lsb = device.read(REG_LSB_Z);
		short z = (short) (msb << 8 | lsb);

		msb = device.read(REG_MSB_Y);
		lsb = device.read(REG_LSB_Y);
		short y = (short) (msb << 8 | lsb);

		// TODO detect overflow - any register contains -4096
//		boolean overflow = (x == OVERFLOW) || (y == OVERFLOW) || (z == OVERFLOW);

		return new short[] {x, y, z};
	}
	
	
	public double readBearing() throws IOException {
		
		short[] coords = readXzy();
		double x = coords[0];
		double y = coords[1];
		double z = coords[2];

		// apply offset/scaling from calibration before calculating the bearing angle
		 x = (x + offsetX) * scaleX;
		 y = (y + offsetY) * scaleY;
		
		double bearing = convertToDegree(x,y);
		
		return bearing;
	}
	
	public double convertToDegree(double x, double y) {
		return (Math.atan2(y, x) * (180.0 / Math.PI) + 720 + offsetDegree) % 360.0;
	}
	
	
	// selftest described in datasheet 
	public void selfTest() throws IOException, InterruptedException {
		System.out.println("self test");
		device.write(REG_CONFIG_A, (byte) 0x71); // 0111 0001 = 8 samples, 15Hz, pos. bias
		device.write(REG_CONFIG_B, (byte) 0xA0); // 1010 0000 = GAIN_5 4.7Ga
		device.write(REG_MODE, (byte) 0x00);
		
		Thread.sleep(6);
		for (int i=0; i<10; ++i) {
			short[] coords = readXzy();
			System.out.println(String.format("%d\t%d\t%d", coords[0], coords[1], coords[2]));
			device.write((byte) 0x03);

			Thread.sleep(67);
		}
		// TODO for GAIN_5, all values should be in range 243..575
		
		// exit self test mode
		device.write(REG_CONFIG_A, (byte) 0x70);
		
	}

}
