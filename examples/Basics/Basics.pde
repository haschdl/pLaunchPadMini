/**
 * This is a introduction to Processing library Novation Launch Pad Mini.
 * 
 * 26.10.2010, Half Scheidl
 * 
 *
 */
// Example of the library pLaunchController. This sketch is a good way to test
// the library and see the dynamics of using the controller and its effect on the screen.
// It illustrates that when the sketch starts, it is not possible to read the actual position of
// knob - values are updated as soon as you move the knob.



import launchPadMiniClient.*;

LaunchPadMini controller;

void setup() {
  size(500, 500);

  try {
    controller = new LaunchPadMini(this);
    
    controller.LogMode = LOG_MODE.VERBOSE;
  }
  catch(Exception e) {
    println("Unfortunately we could not detect that Launch Pad MINI is connected to this computer :(");
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

  drawControlButtoms(s);
  drawPads(s);
}

/**
 * Draws the control buttons (round buttons on top row and right-most column).
 */
void drawControlButtoms(float s) {
  fill(80);
  //top row
  for (int i = 0; i < 8; i++) {
    ellipse(2*s*(i%8), 0, s, s);
  }


  pushMatrix();
  translate(8*2*s, 2*s);
  for (int i = 0; i < 8; i++) {
    ellipse(0, 2*s*(i%8), s, s);
  }
  popMatrix();
}

/**
 * Draws the pads of the controller.
 */
void drawPads(float s) {
  translate(0, s * 2);
  rectMode(CENTER);
  
  
  for (int i = 0; i < 64; i++) {
    fill(255);
    float x = 2*s*(i%8);
    float y = 2*s*floor(i/8);
    
  
    rect(x, y, s, s);

    
    fill(80);
    boolean padState = controller.getPad(i);
    if (padState) 
      rect(x, y, s, s); //<>//

    fill(255);
    text(round(i), x , y + s);
  }
}
