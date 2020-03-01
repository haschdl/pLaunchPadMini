/** //<>//
 * This examples shows how to set LED colors in the Novation Launch Pad Mini.
 * The several examples alternate on mouseClick - click anywhere on the sketch to move 
 * from example to another.
 * 
 * 
 * 26.10.2010, Half Scheidl
 * 
 *
 */

import launchPadMiniClient.*;
LaunchPadMini controller;

int example;
int examples = 7;

void setup() {
  size(800, 200);
  frameRate(10);

  try {
    controller = new LaunchPadMini(this);
  }
  catch(Exception e) {
    println("It looks like Launch Pad MINI is not connected to this computer :(");
    exit();
  }
  textAlign(LEFT);
  textSize(12);
}


void draw() {
  background(0);
  String message = ""; 
  switch(example) {
  case 0:
    message = "Example 1: Setting all leds with one command. Use turnOnAllLeds with BRIGHTNESS.LOW, BRIGHTNESS.MEDIUM or BRIGHTNESS.FULL"; 
    controller.turnOnAllLeds(BRIGHTNESS.LOW);
    break;
  case 1:
    message = "Example 2: Setting all leds with one command. Same as 1, with BRIGHTNESS.FULL";
    controller.turnOnAllLeds(BRIGHTNESS.FULL);
    break;
  case 2:
    message = "Example 3: Setting one LED with setLedColor(column, row, color).";
    int x = 3;
    int y = 3;
    controller.setLedColor(x, y, LED_COLOR.YELLOW_FULL);
    controller.setLedColor(x, y+1, LED_COLOR.YELLOW_FULL);
    controller.setLedColor(x+1, y, LED_COLOR.YELLOW_FULL);
    controller.setLedColor(x+1, y+1, LED_COLOR.YELLOW_FULL);
    break;
  case 3:
    message = "Example 4: Animating...";


    x = 3;
    y = 3;
    int m = Math.round(1.5 * (1 + cos((float)frameCount*TWO_PI/10)));

    controller.setLedColor(x-m, y-m, LED_COLOR.getRandom(true));
    controller.setLedColor(x-m, y+1+m, LED_COLOR.getRandom(true));
    controller.setLedColor(x+1+m, y-m, LED_COLOR.getRandom(true));
    controller.setLedColor(x+1+m, y+1+m, LED_COLOR.getRandom(true));
    break;
  case 4:
    message = "Example 5: Looping in first row with setLedColor(column, row, color).";
    controller.reset();
    int col = frameCount % 8;
    controller.setLedColor(col, 0, LED_COLOR.GREEN_FULL);
    break;
  case 5:
    message = "Example 6: Looping across whole matrix with random color (LED_COLOR.getRandom()).";
    col = frameCount % 8;
    int row = (frameCount / 8) % 8;
    controller.setLedColor(col, row, LED_COLOR.getRandom(true));
    break;
  case 6:
    message = "Example 7: Flashing LED. Call setLedFlashing before setLedColor. Note that the flashing is based on the controller's internal clock. (no looping needed)";
    controller.setLedFlashing();
    x = 3;
    y = 3;
    controller.setLedColor(x, y, LED_COLOR.RED_FLASHING);
    controller.setLedColor(x, y+1, LED_COLOR.YELLOW_FLASHING);
    controller.setLedColor(x+1, y, LED_COLOR.AMBER_FLASHING);
    controller.setLedColor(x+1, y+1, LED_COLOR.GREEN_FLASHING);
    noLoop();


    break;
  default:
  }
  text( message, 10, 20);
}


void mousePressed() {
  controller.reset();
  example = (++example % examples );
  loop();
}
