#include <WiFi.h>                                                     
#include <IOXhop_FirebaseESP32.h>

#define FIREBASE_HOST "jc20-3e430.firebaseio.com"// "fir-a92d1.firebaseio.com"
#define FIREBASE_AUTH "8xWPBaMRxJJioPrNoXEYylAFzoM5Lw8Q3xxlxs3R" // "ahqhO1svDsU8ImewZTSLBFejgE0yrDkwEZSYxrxg"

#define WIFI_SSID "Sri" 
#define WIFI_PASSWORD "redminote7pro"


void setup() {

  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }

  Serial.print("\n Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is: ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.setString("User","Harry's Car");
  Firebase.setInt("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC",20);
  Firebase.setString("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Expected Time to complete charging","1 hr 50 min");
  Firebase.setBool("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Charging Status",1);
  Firebase.setBool("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Finished Charging",0);
  delay(5000);
}

void loop(){

  // Mimics charging by increasing the State of Charge at regular intervals
  
  delay(2000);
  Firebase.setInt("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC",82);
  Firebase.setString("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Expected Time to complete charging","1 hr 45 min");
  delay(5000);
  Firebase.setInt("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC",84);
  Firebase.setString("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Expected Time to complete charging","1 hr 40 min");
  delay(5000);
  Firebase.setInt("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC",86);
  Firebase.setString("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Expected Time to complete charging","1 hr 30 min");
  delay(5000);
  Firebase.setInt("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC",88);
  Firebase.setString("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Expected Time to complete charging","1 hr 20 min");
  delay(5000);
  Firebase.setInt("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC",90);
  Firebase.setString("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Expected Time to complete charging","1 hr 10 min");
  delay(5000);
  Firebase.setInt("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC",92);
  Firebase.setString("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Expected Time to complete charging","1 hr 00 min");
  delay(5000);
  Firebase.setInt("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC",100);
  Firebase.setString("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Expected Time to complete charging","-- Charging Completed --");
  Firebase.setBool("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Charging Status",0);
  Firebase.setBool("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Finished Charging",1);
}
