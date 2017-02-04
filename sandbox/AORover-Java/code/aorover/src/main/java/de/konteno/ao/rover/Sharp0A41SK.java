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

import de.konteno.ao.rover.adc.Mcp3008;

/**
 * Sharp GP2Y0A41SK0F analog infrared distance sensor
 * datasheet: http://image.dfrobot.com/image/data/SEN0143/GP2Y0A41SK%20datasheet.pdf
 * or https://www.pololu.com/file/download/GP2Y0A41SK0F.pdf?file_id=0J713
 *
 */
class Sharp0A41SK
{
    private Mcp3008 adc; // the AD converter
	private short analogChannel;

    public Sharp0A41SK(Mcp3008 ad, short analogChan)
    {
        this.adc = ad;
        this.setAnalogChannel(analogChan);
    }

    public int getRawValue() throws IOException
    {
        return adc.readAnalogValue_1(getAnalogChannel());
    }

    public float getDistance() throws IOException
    {
        return (float)3600.0 / (float)getRawValue();
    }

	public short getAnalogChannel() {
		return analogChannel;
	}

	public void setAnalogChannel(short analogChannel) {
		this.analogChannel = analogChannel;
	}

}
