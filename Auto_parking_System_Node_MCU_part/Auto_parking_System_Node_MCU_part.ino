///////////////ustudent___arduino
#include "FirebaseArduino.h"
#include <ESP8266WiFi.h>
#define WIFI_SSID "RM4A"
#define WIFI_PASSWORD "cuet15@@"
//#define WIFI_SSID "ONEPLUS_A6000_co_aptdoq"
//#define WIFI_PASSWORD "12346789"
#define FIREBASE_HOST "autoparkingsystem-aa778-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "d2ny59xoTT2PRhJAxJifuFqTaGsDkipNWewjpJkr"

#define buzzer D0

void setup() {
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while(WiFi.status() != WL_CONNECTED){
      Serial.print(".");
      delay(500);
    }
    Serial.println();
    Serial.print("connected: ");
    Serial.println(WiFi.localIP());
    Firebase.begin(FIREBASE_HOST,FIREBASE_AUTH);
    pinMode(buzzer,OUTPUT);
}

String gettingdata(){
  /////////getting data from nodeMCU
  char mystr[15];
  char str[15]={};
  bool assign=false;
  Serial.readBytes(mystr,15); //Read the serial data and store in var
////////////Cleaning data
    for(int i=0,j=0;i<15;i++){
      if(mystr[i]=='s' && mystr[i+1]=='l' && mystr[i+2]=='o' && mystr[i+3]=='t'){
          assign=true;
          i+=4;
        }
      if(mystr[i]=='z'){
          assign=false;
          break;
        }
      if(assign==true){
        str[j]=mystr[i];
          j++;
       }
    }
//    Serial.print("slot: ");
    Serial.println(str);
//    Serial.println();
  //////////cleaning ended
  return str;
}


String a,slt1,slt2,slt3,slt4,slt5,slt6;
char s[200];
void loop() {
  String string=gettingdata();
   char slot[6]={};
   for(int i=0;i<string.length();i++){
    slot[i]=string[i];
    }
   Serial.println(slot);
   // set string value
  if(slot[2]!='\0'){
  Firebase.setString("slot", slot);
  // handle error
  if (Firebase.failed()) {
      Serial.print("setting /message failed:");
      Serial.println(Firebase.error());  
  }delay(1000);
  }
  
  a= Firebase.getString("key");
//  Serial.println(a.length());
//  char* ptr1 = a;
 
//  Serial.println(a);
  if(Firebase.failed()){
    Serial.println(Firebase.error());
    }
    else{
      Serial.write("yy");
      writeString(a);
      Serial.write("zz");
      }
//  delay(100);

  slt1= Firebase.getString("slt1");
  if(Firebase.failed()){
    Serial.println(Firebase.error());
    }
  slt2= Firebase.getString("slt2");
  if(Firebase.failed()){
    Serial.println(Firebase.error());
    }
  slt3= Firebase.getString("slt3");
  if(Firebase.failed()){
    Serial.println(Firebase.error());
    }
  slt3= Firebase.getString("slt4");
  if(Firebase.failed()){
    Serial.println(Firebase.error());
    }
  slt4= Firebase.getString("slt5");
  if(Firebase.failed()){
    Serial.println(Firebase.error());
    }
  slt5= Firebase.getString("slt5");
  if(Firebase.failed()){
    Serial.println(Firebase.error());
    }
  slt6= Firebase.getString("slt6");
  if(Firebase.failed()){
    Serial.println(Firebase.error());
    }
    
   if(slot[0]=='0' && slt1=="c"){
    alarm();
   }else if(slot[1]=='0' && slt2=="c"){
    alarm();
   }else if(slot[2]=='0' && slt3=="c"){
    alarm();
   }else if(slot[3]=='0' && slt4=="c"){
    alarm();
   }else if(slot[4]=='0' && slt5=="c"){
    alarm();
   }else if(slot[5]=='0' && slt6=="c"){
    alarm();
   }
  

}
void alarm(){
  ///here buzzer pin is d=used for triggering arduino
  digitalWrite(buzzer,HIGH);
  delay(5000);
  digitalWrite(buzzer,LOW);
  delay(100);
  }
void writeString(String stringData) { // Used to serially push out a String with Serial.write()

  for (int i = 0; i < stringData.length(); i++)
  {
    Serial.write(stringData[i]);   // Push each char 1 by 1 on each loop pass
  }

}// end writeString
