// Ultrasonic code using wiringPi for Groove Ultrasonic Ranger v2.0
// Author: M.Ozcelikors  <mozcelikors@gmail.com>
// Migrated from Groove Ultrasonic Ranger Python version
// to C/C++
// Can be built with:
//    g++ ultrasonic.c -lwiringPi

#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>

//If you use GrooveUltrasonicRanger
#define SIG 6

void setup_GrooveUltrasonicRanger() {
    wiringPiSetup();
}

int getCM_GrooveUltrasonicRanger()
{
    long startTime, stopTime, elapsedTime, distance;
		pinMode(SIG, OUTPUT);
		digitalWrite(SIG, LOW);
		delayMicroseconds(2000);
		digitalWrite(SIG, HIGH);
		delayMicroseconds(5000);
		digitalWrite(SIG,LOW);
		startTime = micros();
		pinMode(SIG,INPUT);
		while (digitalRead(SIG) == LOW)
			startTime = micros();
		while (digitalRead(SIG) == HIGH)
			stopTime = micros();
		elapsedTime = stopTime - startTime;
		distance = elapsedTime * 34300;
		distance = distance / 1000000;
		distance = distance / 2;
		return distance;
}

int main(void) {
    setup_GrooveUltrasonicRanger();
    printf("Distance: %dcm\n", getCM_GrooveUltrasonicRanger());

    return 0;
}
