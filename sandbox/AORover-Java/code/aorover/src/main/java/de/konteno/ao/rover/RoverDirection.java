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

// TODO delete this enum and make constants
public enum RoverDirection {


	FORWARD(1),
	BACKWARD(-1);

	private final int value;

	private RoverDirection(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

}
