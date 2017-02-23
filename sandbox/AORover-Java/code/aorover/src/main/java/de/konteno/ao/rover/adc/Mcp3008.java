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
package de.konteno.ao.rover.adc;

import java.io.IOException;

import com.pi4j.io.spi.SpiDevice;

/***
 * MCP3004/3008/3208 A/D converter on SPI channel
 *
 */
public class Mcp3008
{
    public static final int CHAN_CONFIG_SINGLE = 8;
    public static final int CHAN_CONFIG_DIFF = 0;
    public static final int SPEED = 1000000; // 1 MHz

	private SpiDevice device;


    public Mcp3008(SpiDevice dev)
    {
    	device = dev;
    }
    
    public int readAnalogValue_1(short channel) throws IOException {
    	byte channelConfig = CHAN_CONFIG_SINGLE;
    	
    	byte[] buf = new byte[] {
                (byte)1, // start bit
                (byte)((channelConfig + channel) << 4),
                (byte)0
    	};
    	
//    	System.out.println(String.format("write %02X %02X %02X to MCP3008 (SPI) channel %d", buf[0], buf[1], buf[2], channel));
    	byte[] result = device.write(buf);
    	int val = ((result[1] & 0x03) << 8) | (result[2] & 0xff); // get last 10 bits
    	
//    	System.out.println(String.format("read %02X %02X %02X (%d) from MCP3008 (SPI) channel %d", result[0], result[1], result[2], val, channel));
    	return val;
	}
    
//    /**
//     * Communicate to the ADC chip via SPI to get single-ended conversion value for a specified channel.
//     * @param channel analog input channel on ADC chip
//     * @return conversion value for specified analog input channel
//     * @throws IOException
//     */
//    public int readAnalogValue(short channel) throws IOException {
//
//        // create a data buffer and initialize a conversion request payload
//        byte data[] = new byte[] {
//                (byte) 0b00000001,                              // first byte, start bit
//                (byte)(0b10000000 |( ((channel & 7) << 4))),    // second byte transmitted -> (SGL/DIF = 1, D2=D1=D0=0)
//                (byte) 0b00000000                               // third byte transmitted....don't care
//        };
//
//        // send conversion request to ADC chip via SPI channel
//        byte[] result = device.write(data);
//
//        // calculate and return conversion value from result bytes
//        int value = (result[1]<< 8) & 0b1100000000; //merge data[1] & data[2] to get 10-bit result
//        value |=  (result[2] & 0xff);
//        return value;
//    }

}
