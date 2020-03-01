/** //<>//
 * This is a introduction to Processing library Novation Launch Pad Mini.
 * 
 * This example illustrates the options with PAD_MODE.
 * With PAD_MODE = 
 *
 * 01.03.2020, Half Scheidl
 *
 */

import launchPadMiniClient.*;

LaunchPadMini controller;

void setup() {
  size(500, 500);

  try {
    controller = new LaunchPadMini(this);
    controller.LogMode = LOG_MODE.VERBOSE;
    
    //Choice of PAD_MODE. Uncomment one of the 3 versions below
    //to see the effects.
    
    //PAD_MODE.TOGGLE is the default. Pads have two states (ON/OFF).  
    //controller.setPadMode(PAD_MODE.TOGGLE);
    
    //PAD_MODE.LOOP means that that pads will loop over all possible colors
    controller.setPadMode(PAD_MODE.LOOP);
   
  }
  catch(Exception e) {
    println("It looks like Launch Pad MINI is not connected to this computer :(");
    exit();
  }
  textAlign(CENTER);
}


void draw() {
  background(0);
  translate(20, 20);
  stroke(255);
  strokeWeight(3);
  float s = width/18; //2 times the number of buttons along the X axis
  translate(s/2, s/2);

  drawPads(s);
}
/**
 * Draws the pads of the controller.
 */
void drawPads(float s) {
  //translate(0, s * 2);
  rectMode(CENTER);

  for (Pad pad : controller.getPads()) {
    fill(255);

    float x = 2 * s * pad.column;
    float y = 2 * s * pad.row;

    if (pad.column == 8 || pad.row == 0)
      ellipse(x, y, s, s);
    else
      rect(x, y, s, s);

    if (pad.asBoolean()) {
      fill(pad.ledColor.asColor());
      if (pad.column == 8 || pad.row == 0)
        ellipse(x, y, s, s);
      else
        rect(x, y, s, s);
    }

    fill(255);
    text(round(pad.row * 9 + pad.column), x, y + s);
  }
}
