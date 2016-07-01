runtimes/java
=============

This module contains a sample application that allows you to control the rover using a web browser.

Hardware Setup
--------------

Please refer to the constants defined in org.polarsys.rover.samples.webapp.pi.PiServer on how to wire your hardware.


Building the Sample from Source
----------------------------------------

The Java code is built using Maven (http://maven.apache.org/). The top-level Maven module is located in the folder '../releng' . Switch to this folder and type

	mvn clean install 
	
to build the library and all samples. The created .jar file is then located in the target folder of this module 
(rover-samples-webapp-<version>-jar-with-dependencies.jar). This is an executable self-contained jar that includes all required dependencies.


Running the Sample
------------------

Copy the rover-samples-webapp-<version>-jar-with-dependencies.jar file to your raspberry pi. Copy the file src/main/resources/templates/rover.properties to the same folder. This java properties file is used to configure your hardware (e.g., servo direction and offsets). You may adapt the settings to your needs.

Run the application as root (required to access the IO ports) using the command

	sudo java -jar rover-samples-webapp-<version>-jar-with-dependencies.jar
	
This will start a Jetty HTTP server on Port 8000 that can be accessed from a HTML5 capable browser using the URL http://<ip-of-your-raspi>:8000. You can then control your rover using the buttons on that website. You may also adapt the rover.properties file while the application is running. The configuration is automatically reloaded every 5 seconds.

Requirements
------------

 * JDK 8 or higher (build only)
 * Maven 3.3 or higher (build only )
 
 * Java 8 VM or higher (runtime)
 * Raspberry Pi (runtime) 