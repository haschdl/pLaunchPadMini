/** //<>//
 * This is a introduction to Processing library Novation Launch Pad Mini.
 * 
 * 28.02.2020, Half Scheidl
 * 
 *
 */

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
void launchPadMiniPadChanged(int col, int row) {
  println(String.format("Pad changed! Column: %d Row: %d", col, row));
}
