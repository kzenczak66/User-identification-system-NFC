#include "SPI.h"
#include "PN532_SPI.h"
#include "snep.h"
#include "NdefMessage.h"
 void setup()
{ 
  SNEP nfc(pn532spi);
  uint8_t ndefBuf[128];
  uint8_t recordBuf[128];
  PN532_SPI pn532spi(SPI, 10);
    pinMode(3, OUTPUT);
    digitalWrite(3, LOW);
    pinMode(6, OUTPUT);
    digitalWrite(6, LOW);
    pinMode(7,OUTPUT);
     
    Serial.begin(115200);
}
 
void loop()
{
    int msgSize = nfc.read(ndefBuf, sizeof(ndefBuf));
    if (msgSize > 0) {
        NdefMessage msg  = NdefMessage(ndefBuf, msgSize);
        NdefRecord record = msg.getRecord(0);
        int recordLength = record.getPayloadLength();
        if (recordLength <= sizeof(recordBuf)) {
            record.getPayload(recordBuf);
            Serial.write(recordBuf, recordLength);
                Serial.write('\n');
            
            delay(1000);
        }
    } else {
        Serial.println("Błąd");
    }

    if(Serial.available()>0)
    {
      String check=Serial.readString();
      if(check=="wejscie")
      {
        digitalWrite(3, HIGH);
        delay(1000);
        digitalWrite(3,LOW);
        digitalWrite(7,HIGH); 
        delay(1000);   
        digitalWrite(7,LOW); 
        delay(1000);   
      }
      else if(check=="brak")
      {
        digitalWrite(6, HIGH);
        delay(1000);
        digitalWrite(6,LOW);
      }
      
    }
    delay(500);
}

 
