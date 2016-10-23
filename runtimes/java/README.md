runtimes/java
=============

This folder contains a library and samples written in Java.

Module Structure
----------------

 * org.polarsys.rover.core - Contains the driver interfaces
 * org.polarsys.rover.core.raspi - Contains driver implementations for the raspberry pi
 * org.polarsys.rover.demo	- Contains some demo applications that use the hardware drivers


Building the Library/Samples from Source
----------------------------------------

The Java code is built using Maven (http://maven.apache.org/). The top-level Maven module is located in the folder 'releng'. Switch to this folder and type

	mvn clean install 
	
to build the library and all samples. The created .jar files are then located in the target folders of the corresponding modules. Please refer to the JavaDoc 
of the sample applications how to use them and how to setup your hardware.

Setup Eclipse
-------------

You must have installed the Maven integration for Eclipse (m2e - Maven Integration for Eclipse) available from the Eclipse update site. Then, you can import 
the Maven modules as Eclipse projects using the command File -> Import -> Existing Maven Projects. Choose the folder that contains this file as 'Root Directory'.

Requirements
------------

 * JDK 8 or higher
 * Maven 3.3 or higher
 * Raspberry Pi to run the sample applications; Tests are hardware independent.
 
Setup your Raspberry Pi
-----------------------

The provided samples have been tested with your reference platform: Raspberry Pi 3 Model B running Raspbian Jessie Lite. Please
follow the instructions on the official Raspbian download site (https://www.raspberrypi.org/downloads/raspbian/) for basic 
setup instructions.

Run raspi-config to configure your Raspbian installation:

	sudo raspi-config
	
The following changes to the default configuration should be made:

 # Expand Filesystem
 # Enable Camera (If you want to use the pi camera. Optional) 
 # Enable Advanced Options | I2C (If you have attached e.g., a PCA9685-based PWM generator or any other I2C device. Optional)
 # Set Advanced Options | GPU-Memory to 16 MB (The rover runs in headless mode and thus requires no GPU)

Afterwards, a reboot is required.

Install the following software packages:

 # oracle-java8-jdk (provides java8-runtime-headless)
 # i2c-tools (tools for the I2C bus, optional)
 
	sudo apt-get install oracle-java8-jdk i2c-tools

Run the samples
-------------------

Afer you have built the application and setup your Raspberry Pi you are ready to run the demo applications. Please refer to 
the documentation of the particular application on how to run it. 





	

