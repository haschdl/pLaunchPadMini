import launchPadMiniClient.*;
import processing.core.*;
import processing.event.KeyEvent;

public class Sketch extends PApplet {

    LaunchPadMini controller;

    public static void main(String[] args) {
        PApplet.main(new String[]{"Sketch",});
    }

    public void settings() {
        size(400, 400, P2D);
    }

    public void setup() {
        prepareExitHandler();

        try {
            controller = new LaunchPadMini(this);
            controller.setPadMode(PAD_MODE.LOOP);
            controller.LogMode = LOG_MODE.VERBOSE;
        } catch (Exception e) {
            println("Unfortunately we could not detect that Launch Pad MINI is connected to this computer :(");
            exit();
        }
        textAlign(CENTER);
    }

    public void draw() {

        background(0);
        translate(20, 20);
        stroke(255);
        strokeWeight(3);
        float s = width / (18 + 1); //2 times the number of buttons along the X axis
        translate(s / 2, s / 2);
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

    @Override
    public void keyTyped(KeyEvent event) {
        println(event.getKey());
        if (event.getKey() == 'm') {
            println("The current matrix is: ");
            println(controller.printMatrix());
        }

        if (event.getKey() == 'o') {
            println("The list of activated pads is: ");
            controller.getPadsOn().forEach(p  -> print(p.getPosition()));
        }
    }

    void launchPadMiniPadChanged(int col, int row) {
        println(String.format("Pad changed! Column: %d Row: %d", col, row));
    }

    private void prepareExitHandler() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("SHUTDOWN HOOK");
                try {
                    stop();
                    if (controller != null)
                        controller.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace(); // not much else to do at this point
                }
            }
        }));
    }
}

