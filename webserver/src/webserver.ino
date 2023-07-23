#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include <HTTPClient.h>
#include "oled_u8g2.h" // oled display
#include <ArduinoJson.h>
// arduino server
const char* ssid = "swcore";
const char* password = "inha123*";

// spring serve
// your server address
String springServerAddress = "http://165.246.116.124:8080";
// HTTPClient object to connect Spring server
HTTPClient client;
OLED_U8G2 oled; // create oled object
WebServer server(80);  // create WebServer object, port

int tempSensor = A2; // temperature sensor
// formula
int Vo;
int LED_LIGHT = D2;
bool Light_status = false;
int currentTime;
double R1 = 10000;
double logR2, R2, T, Tc;
double c1 = 1.009249522e-03, c2 = 2.378405444e-04, c3 = 2.019202697e-07;
double Tf = 0;

void setup(void) {
  Serial.begin(115200);  // ESP32 baud rate
  oled.setup();
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");
  while (WiFi.status() != WL_CONNECTED) { // wait for connection
    delay(500);
    Serial.print("-> ");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP addr: ");
  Serial.println(WiFi.localIP()); // print assigned ip address

  server.on("/", handleRootEvent);  // root, event handling function
  server.on("/test", handleTestEvent);// test
  server.begin();
  Serial.println("Web server started!");
  currentTime = millis();
  pinMode(LED_LIGHT, OUTPUT);
}

void loop(void) { 
  oled.setLine(1, "Inha Factory");
  oled.setLine(2, "Web Server");
  oled.display();
  calcTempCelsius();
  server.handleClient();  // clients process
  sendTemperature();
  delay(100);
}

void handleRootEvent() {
  Serial.print("main page from ");

  String clientIP = server.client().remoteIP().toString();  // client's ip addr.

  // 192.168.15.245 clientIP
  int octet1, octet2, octet3, octet4;
  sscanf(clientIP.c_str(), "%d.%d.%d.%d", &octet1, &octet2, &octet3, &octet4);
  String maskedIP = String(octet1) + ".XXX.XXX." + String(octet4); // 2nd, 3rd masking

  String convertedTemp = ConvertTempToString();
  String message = "Welcome Inha SmartFactory WebServer!\n\n";
  message += "Your IP address: " + maskedIP;
  message = message + "\nTemperature: " + convertedTemp;
  server.send(200, "text/plain", message);  // status code 200(OK), format, message

  Serial.println(clientIP);
  Serial.println(convertedTemp);
}

void calcTempCelsius(){
  Vo = analogRead(tempSensor); // read from temperature sensing value
  R2 = R1 * (4095.0 / (float)Vo - 1.0);
  logR2 = log(R2);
  T = (1.0 / (c1 + c2*logR2 + c3*logR2*logR2*logR2));
  Tc = T - 273.15;  // celsius
}

String ConvertTempToString(){
  Tf = (Tc * 9.0/5.0) + 32.0;  // fahrenheit
  return String(Tc) + "C " + "(" + String(Tf) + "F)";
}

void sendTemperature(){
  StaticJsonDocument<200> jsonBuffer;
  String jsonStr;
  
  if(millis() - currentTime >= 60000){
    jsonBuffer["partNo"] = "PT001";
    jsonBuffer["innerTemp"] = Tc;
    jsonBuffer["outerTemp"] = Tc + 3;
    // jsonBuffer["time"] = 
    client.begin(springServerAddress + "/sendData");
    client.addHeader("Content-Type", "application/json");

    serializeJson(jsonBuffer, jsonStr);
    currentTime = millis();
    int respCode = client.POST(jsonStr);

    if(respCode > 0){
      String response = client.getString();
      Serial.println("서버 응답 코드 : " + (String)respCode);
      Serial.println("서버 응답: ");
      Serial.println(response);
    } else {
      Serial.print("요청 실패! HTTP 오류 코드: ");
      Serial.println(respCode);
    }
    client.end();
  }
  
}

void handleTestEvent(){
  Serial.println("LED Change REQ");
  server.send(200, "text/plain", "Change LED");
  Light_status = !Light_status;

  if(Light_status){
    digitalWrite(LED_LIGHT, LOW);
  } else{
    digitalWrite(LED_LIGHT, HIGH);
  }
  Serial.println("LED Changed");
}