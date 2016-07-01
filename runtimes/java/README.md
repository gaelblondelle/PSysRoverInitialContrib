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