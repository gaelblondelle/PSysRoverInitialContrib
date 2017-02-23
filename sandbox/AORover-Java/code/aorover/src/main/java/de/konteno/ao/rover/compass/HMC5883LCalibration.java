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

public class HMC5883LCalibration implements Runnable{

	public boolean done = false;

	private HMC5883L compass;
	
	public HMC5883LCalibration(HMC5883L comp) {
		this.compass = comp;
	}
	
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public void run() {
		done = false;
		
		short[] startPoint = null;
		int xmin =0, xmax =0, ymin =0, ymax =0;
		
		while (!done) {
			try {
				short[] p;
				p = compass.readXzy();
			
				if (startPoint == null) {
					startPoint = p;
					xmin = xmax = p[0];
					ymin = ymax = p[1];
				}
				
				xmin = Math.min(xmin, p[0]);
				xmax = Math.max(xmax, p[0]);
				ymin = Math.min(ymin, p[1]);
				ymax = Math.max(ymax, p[1]);
	
				System.out.println(String.format("%d\t%d\t%d", p[0], p[1], p[2]));

				Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}

		}
		
		compass.offsetX = (int)((-1) * ((xmax + xmin) / 2.0));
		compass.scaleX = 1000.0/(xmax - xmin);
		
		compass.offsetY = (int)((-1) * ((ymax + ymin) / 2.0));
		compass.scaleY = 1000.0/(ymax - ymin);

		compass.offsetDegree = 0;
		double north = compass.convertToDegree(
				(startPoint[0] + compass.offsetX) * compass.scaleX, 
				(startPoint[1] + compass.offsetY) * compass.scaleY);
		compass.offsetDegree = (-1) * north;

		System.out.println(String.format("x = (x + %d) * %3.4f", compass.offsetX, compass.scaleX));
		System.out.println(String.format("y = (y + %d) * %3.4f", compass.offsetY, compass.scaleY));
		System.out.println(String.format("north offset = %3.1f", compass.offsetDegree));

	}

}
