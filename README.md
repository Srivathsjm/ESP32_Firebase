# ESP32_Firebase
This project ensures IOT enabled monitoring of EV charging stations using ESP32 (which acts as the IOT node) and Google Firebase (which acts as remote database). The data from Firebase is processed using Google Scripts and an invoice is generated (after calculating the total energy consumed) which is mailed to the user.

## Device supported
ESP32

## Libraries needed
ESP32 is programmed in Arduino IDE and the following libraries are needed:

[IOXhop_FirebaseESP32](https://github.com/ioxhop/IOXhop_FirebaseESP32)

[ArduinoJson v5.13.3](https://github.com/bblanchon/ArduinoJson/releases/tag/v5.13.3)
