#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <DHT.h>

#define DHTTYPE DHT11

int sensor_pin = A0; 
int moisture;
int moisture_power_pin = 14;
int light;
int light_power_pin = 4;
int temp_data_pin = 5;
float temp;
float humi;

const char* ssid     = "ssid";
const char* password = "password";

DHT dht(temp_data_pin, DHTTYPE);

WiFiClient client;
IPAddress server(172, 20, 10, 2);
String ipAddress = "172.20.10.2";

void setup() {
   Serial.begin(115200);
   pinMode(light_power_pin, OUTPUT);
   pinMode(moisture_power_pin, OUTPUT);
   pinMode(temp_data_pin, OUTPUT);
   
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  if(WiFi.status() == WL_CONNECTED){
    Serial.println("WiFi connected!");
    Serial.println("IP address: "); 
    Serial.println(WiFi.localIP()); 
  }

  Serial.println("Serial initialized");
  digitalWrite(moisture_power_pin, LOW);
   delay(2000);
}

void loop() {

    //Temperature part
  Serial.println("Light power off, Moisture power off, Temp power on");
  delay(1000);
  temp = dht.readTemperature();
  Serial.println("Temp: " + String(temp));
  delay(1000);
  humi = dht.readHumidity();
  Serial.println("Humi: " + String(humi));
  delay(1000);

   if(WiFi.status() == WL_CONNECTED){
      Serial.println("Starting connection to server...");

      HTTPClient http;
      if(http.begin(client, "http://172.20.10.2:31415/post-temp-humi/" + String(temp) + "/" + String(humi))){
        
        Serial.print("[HTTP] POST...\n");
        // start connection and send HTTP header
        int httpCode = http.POST("");
  
        // httpCode will be negative on error
        if (httpCode > 0) {
          // HTTP header has been send and Server response header has been handled
          Serial.printf("[HTTP] POST... code: %d\n", httpCode);
  
          // file found at server
          if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) {
            String payload = http.getString();
            Serial.println(payload);
          }
        }else {
          Serial.printf("[HTTP] POST... code: %d\n", httpCode);
          Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
         }

        http.end();
        
      }
   }

     delay(5000); //Delay 1 min, ready to collect temperature and humidity
  
    //Light part
  digitalWrite(light_power_pin, HIGH);
  delay(2000);
  Serial.println("Light power on, Moisture power off, Temp power off");
  light = analogRead(sensor_pin);
  Serial.println("Light: " + String(light));
  delay(1000);

   if(WiFi.status() == WL_CONNECTED){
      Serial.println("Starting connection to server...");

      HTTPClient http;
      if(http.begin(client, "http://172.20.10.2:31415/post-light/" + String(light))){
        
        Serial.print("[HTTP] POST...\n");
        // start connection and send HTTP header
        int httpCode = http.POST("");
  
        // httpCode will be negative on error
        if (httpCode > 0) {
          // HTTP header has been send and Server response header has been handled
          Serial.printf("[HTTP] POST... code: %d\n", httpCode);
  
          // file found at server
          if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) {
            String payload = http.getString();
            Serial.println(payload);
          }
        }else {
          Serial.printf("[HTTP] POST... code: %d\n", httpCode);
          Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
         }

        http.end();
        
      }
   }

   delay(1000);
   digitalWrite(light_power_pin, LOW);  //power off light sensor after finish
  delay(5000); //Delay 1 min, ready to collect soil moisture

    //Soil moisture part
  digitalWrite(moisture_power_pin, HIGH);
  delay(2000);
  Serial.println("Light power off, Moisture power on, Temp power off");
  moisture = analogRead(sensor_pin);
  moisture = map(moisture,1024,0,0,100);
  Serial.println(moisture);
  
  if(WiFi.status() == WL_CONNECTED){
      Serial.println("Starting connection to server...");

      HTTPClient http;
      if(http.begin(client, "http://172.20.10.2:31415/post-soil-moisture/" + String(moisture))){
        
        Serial.print("[HTTP] POST...\n");
        // start connection and send HTTP header
        int httpCode = http.POST("");
  
        // httpCode will be negative on error
        if (httpCode > 0) {
          // HTTP header has been send and Server response header has been handled
          Serial.printf("[HTTP] POST... code: %d\n", httpCode);
  
          // file found at server
          if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) {
            String payload = http.getString();
            Serial.println(payload);
          }
        }else {
          Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
         }

        http.end();
        
      }
   }

   delay(1000);
   digitalWrite(moisture_power_pin, LOW); //power off soil moisture sensor after finish


    delay(5000);

}
