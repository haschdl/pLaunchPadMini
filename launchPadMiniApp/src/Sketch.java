import launchPadMiniClient.*;
import processing.core.*;
import processing.event.KeyEvent;


public class Sketch extends PApplet {


    LaunchPadMini controller;


    public static void main(String[] args) {
        PApplet.main(new String[]{"Sketch",});

    }


    public void settings() {
        size(800, 800, P2D);

    }


    public void setup() {
        prepareExitHandler();

        try {
            controller = new LaunchPadMini(this);
            controller.pad_mode = PAD_MODE.LOOP;
            controller.LogMode = LOG_MODE.VERBOSE;
        } catch (Exception e) {
            println("Unfortunately we could not detect that Launch Pad MINI is connected to this computer :(");
        }
        textAlign(CENTER);


    }


    public void draw() {

        background(0);
        translate(20, 20);
        stroke(255);
        strokeWeight(3);
        float s = width / 18; //2 times the number of buttons along the X axis
        translate(s / 2, s / 2);

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

    @Override
    public void keyTyped(KeyEvent event) {
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

