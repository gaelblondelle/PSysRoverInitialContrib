package org.polarsys.rover.driver.pi.pwmgen.pca9685;

import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.ALLCALL;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_ALL_LED_OFF_H;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_ALL_LED_ON_H;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_LED0_OFF_H;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_LED0_OFF_L;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_LED0_ON_H;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_LED0_ON_L;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_MODE1;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_MODE2;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_PRESCALE;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_SUBADR1;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_SUBADR2;
import static org.polarsys.rover.driver.pi.pwmgen.pca9685.PCA9685PWMGenerator.REG_SUBADR3;

import java.io.IOException;

import com.pi4j.io.i2c.I2CDevice;

public class I2CDeviceMock implements I2CDevice {

	final byte[] memory;

	public I2CDeviceMock() {
		memory = new byte[256];

		memory[REG_MODE1] = 0x11;
		memory[REG_MODE2] = 0x04;

		memory[REG_SUBADR1] = (byte) 0xE2;
		memory[REG_SUBADR2] = (byte) 0xE4;
		memory[REG_SUBADR3] = (byte) 0xE8;

		memory[ALLCALL] = (byte) 0xE0;

		for (int i = 0; i < 16; ++i) {
			int offset = i * 4;
			memory[REG_LED0_OFF_H + offset] = 0x10;
		}

		memory[REG_ALL_LED_ON_H] = 0x10;
		memory[REG_ALL_LED_OFF_H] = 0x10;

		memory[REG_PRESCALE] = 0x1E;

	}

	@Override
	public int read() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int read(int address) throws IOException {
		checkValidAddress(address);
		return memory[address];
	}

	private void checkValidAddress(int address) {
		if (address < 0 || (address > 69 && address < 250) || address > 255) {
			throw new IllegalStateException("Invalid address " + address);
		}
	}

	@Override
	public int read(byte[] arg0, int arg1, int arg2) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int read(int arg0, byte[] arg1, int arg2, int arg3) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int read(byte[] arg0, int arg1, int arg2, byte[] arg3, int arg4, int arg5) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(byte arg0) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(int address, byte value) throws IOException {
		checkValidAddress(address);
		memory[address] = value;
	}

	@Override
	public void write(byte[] arg0, int arg1, int arg2) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(int arg0, byte[] arg1, int arg2, int arg3) throws IOException {
		throw new UnsupportedOperationException();
	}

	public int getOnTimerValue(int channel) {
		int offset = channel * 4;
		int value = memory[REG_LED0_ON_H + offset] << 8;
		value |= memory[REG_LED0_ON_L + offset] & 0xFF;

		return value;
	}

	public int getOffTimerValue(int channel) {
		int offset = channel * 4;
		int value = memory[REG_LED0_OFF_H + offset] << 8;
		value |= memory[REG_LED0_OFF_L + offset] & 0xFF;

		return value;
	}

	@Override
	public int getAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(byte[] arg0) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(int arg0, byte[] arg1) throws IOException {
		throw new UnsupportedOperationException();
	}
}
