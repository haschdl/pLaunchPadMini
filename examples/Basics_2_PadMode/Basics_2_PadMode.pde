/** //<>//
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
 * Draws the control buttons (round buttons on top row).
 */
void drawControlButtoms(float s) {
  fill(80);
  //top row
  for (int i = 0; i < 8; i++) {
    ellipse(2 * s * (i % 8), 0, s, s);
  }
}

/**
 * Draws the pads of the controller.
 */
void drawPads(float s) {
  translate(0, s * 2);
  rectMode(CENTER);

  for (int col = 0; col < 9; col++) {

    for (int row = 0; row < 8; row++) {
      fill(255);

      float x = 2 * s * col;
      float y = 2 * s * row;

      if (col < 8)
        rect(x, y, s, s);
      else
        ellipse(x, y, s, s);

      LED_COLOR pad = controller.getPad(col, row);

      if (pad.asBoolean()) {
        fill(pad.asColor());
        if (col < 8)
          rect(x, y, s, s);
        else
          ellipse(x, y, s, s);
      }

      fill(255);
      text(round(row * 9 + col), x, y + s);
    }
  }
}
