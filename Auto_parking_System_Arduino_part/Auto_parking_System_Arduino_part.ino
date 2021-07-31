
#include <SoftwareSerial.h>
SoftwareSerial gsmSerial(14, 15); //SIM900 Tx & Rx is connected to Arduino #7 & #8


#include <LiquidCrystal.h> // includes the LiquidCrystal Library 
LiquidCrystal lcd(12, 11, 24, 25, 26, 27); // Creates an LC object. Parameters: (rs, enable, d4, d5, d6, d7) 
 
/////////////////////////////obstacle
int isObstacle = HIGH;  // HIGH MEANS NO OBSTACLE
char mystr[1050]; //Initialized variable to store recieved data\
char password[100];
int pass_input_time=50;
int Door=0;
int lock=1;
int net=0;
long Tim=10000;
String key="";
int errorcnt=0;

int isObstaclePin1 = 28;  // This is our input pin
int isObstaclePin2 = 29;  // This is our input pin
int isObstaclePin3 = 30;  // This is our input pin
int isObstaclePin4 = 31;  // This is our input pin
int isObstaclePin5 = 32;  // This is our input pin
int isObstaclePin6 = 33;  // This is our input pin

int isObstacleLed1 = 34;  // This is our output pin
int isObstacleLed2 = 35;  // This is our output pin
int isObstacleLed3 = 36;  // This is our output pin
int isObstacleLed4 = 37;  // This is our output pin
int isObstacleLed5 = 38;  // This is our output pin
int isObstacleLed6 = 39;  // This is our output pin
int buzzer = 22;
int buzzerinput = 13;
int door_open_sw = A4;  // This is our input pin
//int motor = A9;  // This is our input pin
///////////////////////////////////////////////////////////////Key4*4
#include <Keypad.h>


const byte ROWS = 4; //four rows
const byte COLS = 4; //three columns
char keys[ROWS][COLS] = {
  
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
};
byte rowPins[ROWS] = {6, 7, 8, 9}; //connect to the row pinouts of the keypad
byte colPins[COLS] = {2, 3, 4, 5}; //connect to the column pinouts of the keypad

Keypad keypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS );

/////////////////////////////////////////////////////////////////////////////

#include <Servo.h> 
Servo name_servo,name_servo1,name_servo2,name_servo3,name_servo4,name_servo5,name_servo6,name_servo7;
int servo_position = 0;
void setup() { 
  lcd.begin(16,2); // Initializes the interface to the LCD screen, and specifies the dimensions (width and height) of the display } 
  Serial.begin(9600);
  pinMode(isObstaclePin1, INPUT);
  pinMode(isObstaclePin2, INPUT);
  pinMode(isObstaclePin3, INPUT);
  pinMode(isObstaclePin4, INPUT);
  pinMode(isObstaclePin5, INPUT);
  pinMode(isObstaclePin6, INPUT);
  
  pinMode(isObstacleLed1, OUTPUT);
  pinMode(isObstacleLed2, OUTPUT);
  pinMode(isObstacleLed3, OUTPUT);
  pinMode(isObstacleLed4, OUTPUT);
  pinMode(isObstacleLed5, OUTPUT);
  pinMode(isObstacleLed6, OUTPUT);
  pinMode(buzzer,OUTPUT);
  pinMode(buzzerinput,INPUT);
  
//  name_servo.attach(A8);
  name_servo1.attach(A1);
  name_servo2.attach(A8);
  name_servo3.attach(A3);
  name_servo4.attach(A4);
  name_servo5.attach(A5);
  name_servo6.attach(A6);
  name_servo6.attach(A7);

  ////////////gsm
  gsmSerial.begin(19200);

  Serial.println("Initializing..."); 
  delay(1000);

  gsmSerial.println("AT"); //Handshaking with SIM900
//  updateSerial();
  
  gsmSerial.println("AT+CMGF=1"); // Configuring TEXT mode
//  updateSerial();
  gsmSerial.println("AT+CNMI=1,2,0,0,0"); // Decides how newly arrived SMS messages should be handled
//  sendsms();
  updateSerial();
  
}


void open_door(char slotnum){
  //lcd.print(lock);
  Serial.println("Door Opening!!!");
  Serial.println(slotnum);
  lcd.clear();
  lcd.setCursor(1,0);
  lcd.print("Door Opening!!!");
  digitalWrite(buzzer,HIGH);
  for (servo_position=180; servo_position >= 90; servo_position -=3){
 
    name_servo1.write(servo_position);
    delay(10);
  }
  if(slotnum=='1'){name_servo=name_servo1;}else if(slotnum=='2'){name_servo=name_servo2;}else if(slotnum=='3'){name_servo=name_servo3;}else if(slotnum=='4'){name_servo=name_servo4;}else if(slotnum=='5'){name_servo=name_servo5;}else if(slotnum=='6'){name_servo=name_servo6;}else {name_servo=name_servo7;}
  for (servo_position=180; servo_position >= 90; servo_position -=3){
 
    name_servo.write(servo_position);
    delay(10);
  }
  digitalWrite(buzzer,LOW);
  delay(200);
  lcd.clear();
  lcd.setCursor(1,0);
  lcd.print("UNLOCKED!!!"); 
  //lcd.print(lock);
  Serial.println("UNLOCKED!!!");
  lock=0;
  delay(4000);



  delay(Tim);/////////////////////////////////////////obstacle pauar por koto khon por motor ghura suru korbe..
    close_door(slotnum);
}
void close_door(char slotnum){
  //lcd.print(lock);
  Serial.println("Door Closing...");
  lcd.clear();
  lcd.setCursor(1,0);
  lcd.print("Door Closing...");
  delay(100);
  digitalWrite(buzzer,HIGH);
  for (servo_position = 90; servo_position <=180; servo_position +=3){
    
    name_servo1.write(servo_position);
    delay(10);
  }
  if(slotnum=='1'){name_servo=name_servo1;}else if(slotnum=='2'){name_servo=name_servo2;}else if(slotnum=='3'){name_servo=name_servo3;}else if(slotnum=='4'){name_servo=name_servo4;}else if(slotnum=='5'){name_servo=name_servo5;}else if(slotnum=='6'){name_servo=name_servo6;}else {name_servo=name_servo7;}
  for (servo_position = 90; servo_position <=180; servo_position +=3){
    
    name_servo.write(servo_position);
    delay(10);
  }
  digitalWrite(buzzer,LOW);
  lock=1;
//  c=0;
  delay(300);
  lcd.clear();
  lcd.setCursor(1,0);
  lcd.print("LOCKED!!!");
  //lcd.print(lock);
//  tim=millis();

      
}

String gettingdata(){
  /////////getting data from nodeMCU
  char str[1050]={};
  bool assign=false;
  Serial.readBytes(mystr,50); //Read the serial data and store in var
////////////Cleaning data
    for(int i=0,j=0;i<50;i++){
      if(mystr[i]=='y' && mystr[i+1]=='y'){
          assign=true;
          i+=2;
        }
      if(mystr[i]=='z'&& mystr[i+1]=='z'){
          assign=false;
          break;
        }
      if(assign==true){
        str[j]=mystr[i];
          j++;
       }
    }
//    Serial.print("data: ");
//    Serial.println(str);
//    Serial.println();
  //////////cleaning ended
  return str;
}


char slotnum='7';
char preslotnum='7';
char unparkedslotnum='7';
char preunparkedslotnum='0';
String ss="";
void loop() {
//  getkey("AAAAA");



//  if (lock==0){
//    delay(Tim);/////////////////////////////////////////obstacle pauar por koto khon por motor ghura suru korbe..
//    close_door();  
//  }
//  Serial.println("g");
  if(int cnt=0;lock==1 && key.length()>1 && net==1){getkey(key);}
    
   String string="";
   
   for(int kk=0;kk<5;kk++){
    
    string=gettingdata();
    
    if(string.length()>0 && ss!=string){
      break;
    }else{ss=string;}
   }
   ss=string;
   char str[1050]={};
   for(int i=0;i<string.length();i++){
    if(string[i]==':'){
      slotnum=string[i+1];
      break;
      }else if(string[i]==','){
        unparkedslotnum=string[i+1];
        i=i+1;
        }else{
        str[i]=string[i];
        }
   }
//   Serial.print("unparkedslotnum");
//    Serial.println(unparkedslotnum);
//    Serial.print("slotnum");
//    Serial.println(slotnum);
    if(slotnum=='1' && slotnum!=preslotnum){
      open_door('7');
      preslotnum=slotnum;
      }
    if(unparkedslotnum!='7' && unparkedslotnum!=preunparkedslotnum){
      open_door(unparkedslotnum);
      preunparkedslotnum=unparkedslotnum;
      }
    
//    Serial.println(str);
    lcd.clear();
    lcd.setCursor(1,0);
    lcd.print("Smart Parking..");
    if(str[3]!='\0'){
      key=str;
//      lcd.clear();
//      lcd.setCursor(1,0);
//      lcd.print(key);
       errorcnt=0;
       net=1;
    }else{
      errorcnt+=1;
      if(errorcnt>=10){
      lcd.setCursor(8,2);
      lcd.print("No Net!");
      net=0;
      }
      }
//   String ss= String(str);
    
//    isObstacle = digitalRead(isObstaclePin1);
    char slot[]={};
    if (digitalRead(isObstaclePin1) == LOW){
      slot[0]='1';digitalWrite(isObstacleLed1,LOW);
    }else {
      slot[0]='0';digitalWrite(isObstacleLed1,HIGH);}
    if (digitalRead(isObstaclePin2) == LOW){
      slot[1]='1';digitalWrite(isObstacleLed2,LOW);
    }else{
      slot[1]='0';digitalWrite(isObstacleLed2,HIGH);}
    if (digitalRead(isObstaclePin3) == LOW){
      slot[2]='1';digitalWrite(isObstacleLed3,LOW);
    }else{
      slot[2]='0';digitalWrite(isObstacleLed3,HIGH);}
    if (digitalRead(isObstaclePin4) == LOW){
      slot[3]='1';digitalWrite(isObstacleLed4,LOW);
    }else {
      slot[3]='0';digitalWrite(isObstacleLed4,HIGH);}
    if (digitalRead(isObstaclePin5) == LOW){
      slot[4]='1';digitalWrite(isObstacleLed5,LOW);
    }else{
      slot[4]='0';digitalWrite(isObstacleLed5,HIGH);}
    if (digitalRead(isObstaclePin6) == LOW){
      slot[5]='1';digitalWrite(isObstacleLed6,LOW);
    }else{
      slot[5]='0';digitalWrite(isObstacleLed6,HIGH);}
    Serial.write("slot");
    writeString(slot);
    Serial.write("z");
    Serial.println();

    if(slot[2]!='\0'){
//    lcd.clear();
//    lcd.setCursor(1,0);
//    lcd.print("Smart Parking..");
    lcd.setCursor(1,2);
    lcd.print(slot);
//    if(string[3]=='\0'){
//      lcd.setCursor(5,2);
//      lcd.print("No network");
//    }
    }
//    while(true){
//      char key = keypad.getKey();
//      if(key!=NO_KEY){Serial.println(key);}
//      }   

 if(digitalRead(buzzerinput)==HIGH){
  //////////////////////////////churi hocche
  sendalertsms();
  
  for(int i=0;i<20;i++){
        digitalWrite(buzzer,HIGH);
        delay(400);
        digitalWrite(buzzer,LOW);
        if(i%3==0){
          delay(1000);}else{delay(200);}
        }
  }
//  slotnum='7';
//  unparkedslotnum='7';
}
void writeString(String stringData) { // Used to serially push out a String with Serial.write()

  for (int i = 0; i < stringData.length(); i++)
  {
    Serial.write(stringData[i]);   // Push each char 1 by 1 on each loop pass
  }

}// end writeString
void getkey(String password){
  /////////////////////////////////////////////////////Key4*4
//  Serial.println("KEY");
//  Serial.println(password);
//   if((isObstacle == LOW && lock==1)  ||(isObstacle == HIGH && lock==1) && (isObstacle == LOW)){
   int T=2000;
   while(T-- && lock==1){
//    Serial.print(T);
    delay(1);
   char key = keypad.getKey(); 
  if (key != NO_KEY){
    int match=0;
    int j=0;
    char key_pass[20]={};
    int sum=0;
    key_pass[0]=key;
    
    if(key_pass[j]==password[j]){sum+=1;j++;}
    int i=pass_input_time;
    int len=password.length();
    lcd.clear();
    lcd.print("Key_Pressed..");
    
    while(i--){
        
       for(int i=0;i<pass_input_time;i++){
        Serial.print(key_pass);
              Serial.println();
              key_pass[j]=keypad.getKey();
              if(key_pass[j]!=NO_KEY){
                if(key_pass[j]==password[j]){
                  sum+=1;}
                  j++;
                  break;}
//              for(int k=1;k<=j;k++){
//              Serial.print(key_pass[k]);
//              }
//              Serial.print(key_pass);
              if(lock==1){
              lcd.setCursor(1,2);
              lcd.print(key_pass);}
              }
          if(sum==len && lock==1){lcd.clear();lcd.print("Key_Pressed..");lcd.setCursor(1,2);lcd.print(key_pass);delay(300);lcd.setCursor(1,2);lcd.print("Matched!!");delay(600);/*Serial.println("matched.");*/open_door(slotnum);break;}/////////servo start hoiche
                
              }
              if(sum!=len){lcd.setCursor(2,1);lcd.print("Not matched!!");/*Serial.println("Not matched!");*/delay(2000);}
  }}
// }
////////////////////////////////////////////////////////////////////////////////////////door_open_sw
//  if(digitalRead(door_open_sw)==HIGH){Open_Door();}
  }
void sendalertsms(){
  gsmSerial.println("AT+CMGS=\"+8801710444862\"");//change ZZ with country code and xxxxxxxxxxx with phone number to sms
  updateSerial();
  gsmSerial.print("Attention!!! someone try to steal your car."); //text content
  updateSerial();
  gsmSerial.write(26);
  }

void updateSerial()
{
  delay(500);
  while (Serial.available()) 
  {
    gsmSerial.write(Serial.read());//Forward what Serial received to Software Serial Port
  }
  while(gsmSerial.available()) 
  {
    Serial.write(gsmSerial.read());//Forward what Software Serial received to Serial Port
  }
}
