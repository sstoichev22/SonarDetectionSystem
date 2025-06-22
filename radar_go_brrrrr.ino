#include <Servo.h>;
Servo servo;
int trig = 11;
int echo = 12;
int servoNum = 10;

int i = 0;
int multi = 3;

void setup() {
  pinMode(trig, OUTPUT);

  pinMode(echo, INPUT);

  servo.attach(servoNum);

  Serial.begin(9600);

}

void loop() {
  if(i+multi < 0) i = 0;
  if(i+multi > 180) i = 180;
  if(i == 0 || i == 180) multi *= -1;
  i += multi;
  servo.write(i);
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);

  int distance = pulseIn(echo, HIGH) / 58.0;
  Serial.println(i);
  Serial.println(distance);
  delay(30);
}
