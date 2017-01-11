/*
   Basic Pin setup:
   ------------                                  ---u----
   ARDUINO   13|-> SCLK (pin 25)           OUT1 |1     28| OUT channel 0
             12|                           OUT2 |2     27|-> GND (VPRG)
             11|-> SIN (pin 26)            OUT3 |3     26|-> SIN (pin 11)
             10|-> BLANK (pin 23)          OUT4 |4     25|-> SCLK (pin 13)
              9|-> XLAT (pin 24)             .  |5     24|-> XLAT (pin 9)
              8|                             .  |6     23|-> BLANK (pin 10)
              7|                             .  |7     22|-> GND
              6|                             .  |8     21|-> VCC (+5V)
              5|                             .  |9     20|-> 2K Resistor -> GND
              4|                             .  |10    19|-> +5V (DCPRG)
              3|-> GSCLK (pin 18)            .  |11    18|-> GSCLK (pin 3)
              2|                             .  |12    17|-> SOUT
              1|                             .  |13    16|-> XERR
              0|                           OUT14|14    15| OUT channel 15
   ------------                                  --------

   -  Put the longer leg (anode) of the LEDs in the +5V and the shorter leg
        (cathode) in OUT(0-15).
   -  +5V from Arduino -> TLC pin 21 and 19     (VCC and DCPRG)
   -  GND from Arduino -> TLC pin 22 and 27     (GND and VPRG)
   -  digital 3        -> TLC pin 18            (GSCLK)
   -  digital 9        -> TLC pin 24            (XLAT)
   -  digital 10       -> TLC pin 23            (BLANK)
   -  digital 11       -> TLC pin 26            (SIN)
   -  digital 13       -> TLC pin 25            (SCLK)
   -  The 2K resistor between TLC pin 20 and GND will let ~20mA through each
      LED.  To be precise, it's I = 39.06 / R (in ohms).  This doesn't depend
      on the LED driving voltage.
   - (Optional): put a pull-up resistor (~10k) between +5V and BLANK so that
                 all the LEDs will turn off when the Arduino is reset.

   If you are daisy-chaining more than one TLC, connect the SOUT of the first
   TLC to the SIN of the next.  All the other pins should just be connected
   together:
       BLANK on Arduino -> BLANK of TLC1 -> BLANK of TLC2 -> ...
       XLAT on Arduino  -> XLAT of TLC1  -> XLAT of TLC2  -> ...
   The one exception is that each TLC needs it's own resistor between pin 20
   and GND.

   This library uses the PWM output ability of digital pins 3, 9, 10, and 11.
   Do not use analogWrite(...) on these pins.

*/
#include "Tlc5940.h"

#define RED_LED 0
#define BLUE_LED 1
#define GREEN_LED 2

#define LED1 0
#define LED2 3
#define LED3 6
#define LED4 9
#define LED5 12
// #define LED6 15
// #define LED7 18
// #define LED8 21

#define FULL_ON 4095
#define HALF_ON 300
#define OFF 0

#define DELAY 1000

#define White 0xffffff
#define Red 0xff0000
#define Green 0x00ff00
#define Blue 0x0000ff
#define Yellow 0xffff00
#define Magenta 0xff007f
#define OffBlue 0x00ffff

#define Amber 0xFFBF00
#define Aquamarine 0x008080
#define ArmyGreen 0x669966
#define AutumnOrange 0xff6633
#define AvocadoGreen 0x669933
#define BabyBlue 0x6699ff
#define BananaYellow 0xcccc33
#define BrickRed 0xcc3300
#define BrightGreen 0x66FF00
#define Brown 0x996633
#define Crimson 0x993366
#define DarkOrange 0xe1632e
#define DarkRed 0xce0063
#define DesertBlue 0x336699
#define DustyRose 0xcc6699
#define EasterPurple 0xcc66ff
#define ElectricBlue 0x6666ff
#define ForestGreen 0x006633
#define GrassGreen 0x009933
#define IceBlue 0x99ffff
#define KentuckyGreen 0x339966
#define LightBlue 0x009cce
#define LightGreen 0xc0dcc0
#define MossGreen 0x008000
#define Mustard 0xffce31
#define OceanGreen 0x996633
#define Plum 0x660066
#define PowderBlue 0xccccff
#define Purple 0xC231E1
#define RedBrown 0xcc6633
#define RegalRed 0xcc3366
#define SkyBlue 0x00ccff
#define SoftPink 0xff9999
#define StandardBlue 0x336699
#define TwilightBlue 0x6666cc
#define TwilightViolet 0x9966cd
#define Walnut 0x663300

boolean finished = false;
// message array to hold incoming data
String msgs[10] = { "", "", "", "", "", "", "", "", "", ""};
boolean msgAvailable = false;  // whether the msg is complete
int lastMsg = 0;
const String MSG_HEADER = "{%%SETSTATE ";
const int MSG_HEADER_SIZE = MSG_HEADER.length();

void setup()
{
  /* Call Tlc.init() to setup the tlc.
     You can optionally pass an initial PWM value (0 - 4095) for all channels.*/
  Tlc.init();
  Serial.begin(19200);

  for (int i = 0; i < 10; i++) {
    msgs[i].reserve(20);
  }

  clearLEDs();
}

void setRGBLED(int LED, long colour)
{
  Serial.println("colour = [" + String(colour, HEX) + "]");
  int red = (colour & 0xFF0000) >> 16;
  Serial.println("red = [" + String(red, HEX) + "]");
  int green = (colour & 0x00FF00) >> 8;
  Serial.println("green = [" + String(green, HEX) + "]");
  int blue = (colour & 0x0000FF);
  Serial.println("blue = [" + String(blue, HEX) + "]");

  Tlc.set(LED + RED_LED, map(red, 0, 0xff, 0, 4095));
  Tlc.set(LED + GREEN_LED, map(green, 0, 0xff, 0, 4095));
  Tlc.set(LED + BLUE_LED, map(blue, 0, 0xff, 0, 4095));
}

int getNumber(String data)
{
  int result = 0;
  char carray[10];
  data.toCharArray(carray, sizeof(carray));
  result = atoi(carray);

  return result;
}

/* Set one in a group of LEDs to high value */
/* Message format  '{%%SETSTATE 1 WARNING }' excluding quotes and ending with a cr
*/
boolean processStatus()
{
  boolean result = false;

  if (msgAvailable) {
    int msgPointer = lastMsg;

    if (msgPointer == 0) {
      msgPointer = 9;
    }
    else {
      msgPointer --;
    }

    String msg = msgs[msgPointer];

    if (( msg.length() > MSG_HEADER_SIZE) && msg.startsWith(MSG_HEADER)) {
      String data = msg.substring(MSG_HEADER_SIZE);

      String numb = data.substring(0, data.indexOf(" ") + 1);

      int led = getNumber(numb);

      String status = data.substring(data.indexOf(" ") + 1);
      long colour = Red;

      if (status.startsWith("S")) {
        colour = Green;
      }
      else if (status.startsWith("W")) {
        colour = Amber;
      }
      else if (status.startsWith("F")) {
        colour = Red;
      }

      int setLED = 0;

      switch (led)
      {
        case 1:
          setLED = LED1;
          break;
        case 2:
          setLED = LED2;
          break;
        case 3:
          setLED = LED3;
          break;
        case 4:
          setLED = LED4;
          break;
        case 5:
          setLED = LED5;
          break;
      }

      setRGBLED(setLED, colour);

      result = true;
    }
  }

  return result;
}

void clearLEDs()
{
  Tlc.clear();
  for (int channel = 0; channel < NUM_TLCS * 16; channel ++) {
    Tlc.set(channel, 0);
  }
  /* Tlc.update() sends the data to the TLCs.  This is when the LEDs will actually change. */
  Tlc.update();
}

void loop()
{
  if (!finished)
  {
    clearLEDs();
    delay(DELAY);

    if (processStatus()) {
      /* Tlc.update() sends the data to the TLCs.  This is when the LEDs will actually change. */
      Tlc.update();
    }

    delay(DELAY);
  }
  else {
    clearLEDs();
  }
}

/*
  SerialEvent occurs whenever a new data comes in the
  hardware serial RX.  This routine is run between each
  time loop() runs, so using delay inside loop can delay
  response.  Multiple bytes of data may be available.
*/
void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      msgAvailable = true;
      //      Serial.print(msgs[lastMsg]);

      lastMsg += 1;
      if (lastMsg == 10) {
        lastMsg = 0;
      }
    }
    else {
      // else add it to the inputString:
      msgs[lastMsg] += inChar;
    }
  }
}

