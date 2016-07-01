package org.polarsys.rover.driver.pwmgen;

import java.io.Closeable;
import java.io.IOException;

/**
 * A driver interface for PWM (pulse width modulation) generators. A PWMGenerator may provide several
 * {@link IPWMOutput}s.
 * 
 * @author Ralf Ellner - Initial contribution and API.
 *
 */
public interface IPWMGenerator extends Closeable {

	/**
	 * Initialize the PWM generator.
	 * 
	 * @throws IOException
	 */
	public void open() throws IOException;

	/**
	 * Set the PWM frequency. This frequency applies to all outputs.
	 * 
	 * @param frequency
	 *            in Hz.
	 */
	public void setFrequency(int frequency) throws IOException;

	/**
	 * Get the output of the given channel.
	 * 
	 * @param channel
	 *            The number of the channel.
	 * @return
	 */
	public IPWMOutput getOutput(int channel);

}
