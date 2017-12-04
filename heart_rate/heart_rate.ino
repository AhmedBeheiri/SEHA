int pulsepin=0;
int LED13=13;

int Signal;
int Threshold=550;
int count=0;
double heartrate=0;
double result;


void setup() {
  // put your setup code here, to run once:
  pinMode(LED13,OUTPUT);
  Serial.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:
  delay(1000);
  Signal=analogRead(pulsepin);
  //Serial.println(Signal);
  

  if(Signal>Threshold){
    digitalWrite(LED13,HIGH);
    count ++;
    heartrate+=Signal;
    if(count==15){
      result=4*heartrate;
      Serial.print("heart rate ");
      Serial.print(result/Threshold);
      Serial.println("BPM");
      count=0;
      heartrate=0;
      result=0;
    }
    
  }else{
    digitalWrite(LED13,LOW);
  }
  

  

}
