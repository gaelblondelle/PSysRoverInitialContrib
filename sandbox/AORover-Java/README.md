# A&O Rover #

This folder contains sample code written in Java for the Eclipse Rover. Please see the other documentations in this repository how to set up the raspberry pi.

## Hardware components ##

* Raspberry Pi 3
* Dagu rover 5 chassis
* Pololu MC33926 dual DC motor driver
* GY-271 / HMC8553L magnetometer / compass (I²C)
* Sharp 0A41SK IR sensor on MCP3008 ADC
* 5V + 7.2V power supply
* LED on GPIO 17

### Wiring diagram ###
![aorover.png](https://bitbucket.org/repo/MjqRde/images/4231134720-aorover.png)

## Software Requirements ##

 * JDK 8 or higher (on your machine and your raspberry pi)
 * Maven 3.3 or higher

## Building the Library/Samples from Source ##

The Java code is built using Maven (http://maven.apache.org/). Switch to the folder where the pom is located and type:

	mvn clean install 

to build the library and all samples. The created .jar files are then located in the target folder. 

For automatically transfering the .jar files to your raspi, edit the pom and configure the wagon-maven-plugin (user, pwd, ip-address, target dir). Run it with this comand:

	mvn wagon:upload

or just to build the .jar files and transfer it to the raspi:

    mvn clean package wagon:upload


## Run the samples ##

After you have built the application and set up your Raspberry Pi you are ready to run the demo applications. On your raspi change into the directory where the .jar files are located. Type:

    sudo java -jar aorover-0.0.1-SNAPSHOT-jar-with-dependencies.jar

You will get the following:
 
    Hi - I am the Eclipse Rover!
    
    Try the following comands:
    i -> drive forward
    m -> drive backward
    j -> hard-left
    J -> soft left
    k -> hard-right
    K -> soft-right
    t -> calibrate compass
    k -> read distance sensor
    s -> stop
    q -> quit
    ... some more


## Tips and tricks ##

Try one component after the other. Start with a simple LED test and then with the motor controller. In the class 'RoverRunner' you will find all the initialization code for the components.

## Trouble shooting ##

* only one motor is working -> check cables and power connection. Disconnect and re-connect the power to the motor controller
* rover is driving backward instead of forward -> change the cables of both motors
* rover is turning instead of driving forward ->  change the cables of one motor
* LED is not working -> check the GPIO pin number, use 'gpio readall' 

### SPI and/or I2C bus

SPI and/or I2C bus is not working -> check if they are enabled in the raspi.conig file

    sudo raspi-config  -> 5 Interfacing Options
    -> P4 SPI (enable)
    -> P5 I2C (enable) 

### MCP3008/MCP3208 AD-converter ###
The ADC is connected to the Pi's hardware SPI bus (MISO/MOSI/CE0/SCLK, pins 9, 10, 8, 11). The 3008 provides 10 bits, while the 3208 provides 12 bits, so you might have to change the code accordingly. 

There seems to be a bug in Pi4j (or this program, or the wiring, or the SPI driver) that causes all values read from MCP3008 AD-converter to be 0. (datasheet:  "After completing the data transfer, if further clocks are applied with CS  low, the A/D converter will output LSB first data, followed by zeros indefinitely"). 

Workaround: unload and reload the SPI driver
    
    sudo rmmod spi_bcm2835
    sudo modprobe spi_bcm2835 


## More infos, datasheets and further links ##

### Compass ###
GY-271 module with Honeywell HMC5883L magnetometer on I²C bus (Pi I2C bus 1), default bus address is 0x1e.

Lessons learned:

* the magnetometer MUST be calibrated! Otherwise the readings are meaningless.
* there might be more sophisticated calibration methods, but for simple 2D orientation, an offset/scale should do.
* calibration must be done in the final robot configuration, otherwise you get wrong offset/scale values.
* keep a safe distance (~10cm) to the motors and the raspi, because this is a magnetometer and measures magnetic fields - that means it also measures the fields of your electronic devices 

* https://cdn-shop.adafruit.com/datasheets/HMC5883L_3-Axis_Digital_Compass_IC.pdf
* https://learn.adafruit.com/adafruit-hmc5883l-breakout-triple-axis-magnetometer-compass-sensor/
* https://edwardmallon.wordpress.com/2015/05/22/calibrating-any-compass-or-accelerometer-for-arduino/
* http://www.germersogorb.de/html/kalibrierung_des_hcm5883l.html

Calibration method:

* we assume that the GY-271 is mounted horizontally (X/Y plane) and will ignore Z values
* place the device away from magnetic noise and turn it around the Z axis (at least one full circle)
* plot the (x,y) values read from the sensor. you will get an ellipse that is probably not centered at (0,0)
* shift the values so that the ellipse is centered at (0,0) and scale them to get a circle: these are your offset and scaling values for the compass!

![c1.png](https://bitbucket.org/repo/MjqRde/images/2409773002-c1.png)  ![c2.png](https://bitbucket.org/repo/MjqRde/images/3467576671-c2.png)

### Distance Controller ###
Sharp GP2Y0A41SK0F analog infrared distance sensor. The sensor needs 5V to work, but the output signal (= ADC input) is 3.3V max. So the ADC can be operated at VDD = VREF = 3.3V without the need for a signal level shifter.

* http://image.dfrobot.com/image/data/SEN0143/GP2Y0A41SK%20datasheet.pdf
* https://www.pololu.com/file/download/GP2Y0A41SK0F.pdf?file_id=0J713