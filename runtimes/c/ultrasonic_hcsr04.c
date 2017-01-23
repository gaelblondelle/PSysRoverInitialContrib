// Ultrasonic code using wiringPi for both HCSR-04
// Added by: M.Ozcelikors  <mozcelikors@gmail.com>
// Resource: https://ninedof.wordpress.com/2013/07/16/rpi-hc-sr04-ultrasonic-sensor-mini-project/
// Can be built with:
//    g++ ultrasonic.c -lwiringPi

#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>

//If you use HCSR-04
#define TRIG 5
#define ECHO 6

void setup_HCSR04() {
    wiringPiSetup();
    pinMode(TRIG, OUTPUT);
    pinMode(ECHO, INPUT);

    //TRIG pin must start LOW
    digitalWrite(TRIG, LOW);
    delay(30);
}

int getCM_HCSR04() {
    //Send trig pulse
    digitalWrite(TRIG, HIGH);
    delayMicroseconds(20);
    digitalWrite(TRIG, LOW);

    //Wait for echo start
    while(digitalRead(ECHO) == LOW);

    //Wait for echo end
    long startTime = micros();
    while(digitalRead(ECHO) == HIGH);
    long travelTime = micros() - startTime;

    //Get distance in cm
    int distance = travelTime / 58;

    return distance;
}

int main(void) {
    setup_HCSR04();
    printf("Distance: %dcm\n", getCM_HCSR04());

    return 0;
}
