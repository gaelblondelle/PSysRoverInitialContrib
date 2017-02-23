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

/**
 * Configuration values for HMC5883L magnetometer
 *
 */
public class HMC5883LConfig{
	
	// configuration values
	public static final int SAMPLES_1 = 0x00;
	public static final int SAMPLES_2 = 0x01;
	public static final int SAMPLES_4 = 0x02;
	public static final int SAMPLES_8 = 0x03;
	public static final int SAMPLES_DEFAULT = SAMPLES_1;

	public static final int DATARATE_0HZ75 = 0x0;
	public static final int DATARATE_1HZ5 = 0x1;
	public static final int DATARATE_3HZ  = 0x2;
	public static final int DATARATE_7HZ5 = 0x3;
	public static final int DATARATE_15HZ = 0x4; 
	public static final int DATARATE_30HZ = 0x5;
	public static final int DATARATE_75HZ = 0x6;
	public static final int DATARATE_DEFAULT = DATARATE_15HZ; 
	
	public static final int MODE_CONTINUOUS = 0x00;
	public static final int MODE_SINGLE = 0x01;
	public static final int MODE_IDLE = 0x02;
	
	public static final int MEASURE_NORMAL = 0x00; // Normal measurement configuration (Default)
	public static final int MEASURE_POS_BIAS = 0x01; // Positive bias configuration for X, Y, and Z axes
	public static final int MEASURE_NEG_BIAS = 0x02; // Negative bias configuration for X, Y, and Z axes

	// gain/bias vales for compensating magnetic deviation 
	public static final int GAIN_0 = 0x00; // 0.88Ga
	public static final int GAIN_1 = 0x01; // 1.3Ga
	public static final int GAIN_2 = 0x02; // 1.9Ga
	public static final int GAIN_3 = 0x03; // 2.58Ga
	public static final int GAIN_4 = 0x04; // 4.0Ga
	public static final int GAIN_5 = 0x05; // 4.7Ga
	public static final int GAIN_6 = 0x06; // 5.6Ga
	public static final int GAIN_7 = 0x07; // 8.1Ga

	private int samples;
	private int datarate;
	private int mode;
	private int measurement;
	private int bias;
	
	
	public HMC5883LConfig() {
		samples = SAMPLES_DEFAULT;
//		datarate = DATARATE_DEFAULT;
		datarate = DATARATE_30HZ;
		mode = MODE_CONTINUOUS;
		measurement = MEASURE_NORMAL;
		bias = GAIN_0;
	}

	public static HMC5883LConfig Default = new HMC5883LConfig();
	
	public int getSamples() {
		return samples;
	}


	public void setSamples(int samples) {
		this.samples = samples;
	}


	public int getDatarate() {
		return datarate;
	}


	public void setDatarate(int datarate) {
		this.datarate = datarate;
	}


	public int getMode() {
		return mode;
	}


	public void setMode(int mode) {
		this.mode = mode;
	}


	public int getBias() {
		return bias;
	}


	public void setBias(int bias) {
		this.bias = bias;
	}


	public int getMeasurement() {
		return measurement;
	}


	public void setMeasurement(int measurement) {
		this.measurement = measurement;
	}
	

}
