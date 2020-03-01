/** //<>//
 * This is a introduction to Processing library Novation Launch Pad Mini.
 * When a button is pressed on the Launch Pad, the column and row of that button will 
 * be printed to the console.
 *
 * Other features:
 *   - Press 'm' to display a text-based version of the matrix.
 *
 *
 * 28.02.2020, Half Scheidl
 * 
 *
 */

import launchPadMiniClient.*;
import java.util.Arrays;

LaunchPadMini controller;

void setup() {
  size(500, 500);

  try {
    controller = new LaunchPadMini(this);
    controller.LogMode = LOG_MODE.ERROR;
  }
  catch(Exception e) {
    println("It looks like Launch Pad MINI is not connected to this computer :(");
    exit();
  }
  textAlign(CENTER);
}
void draw() {
}
void launchPadMiniPadChanged(int col, int row) {
  println(String.format("Pad changed! Column: %d Row: %d", col, row));
}

void keyPressed() {
  if (key == 'm') {
    println("The current matrix is: ");
    println(controller.printMatrix());
  }

  if (key == 'o') {
    println("The list of activated pads is: ");
    ArrayList<Pad> padsOn = controller.getPadsOn();
    for (Pad p : padsOn)
      print(Arrays.toString(p.getPosition()));
  }
}
