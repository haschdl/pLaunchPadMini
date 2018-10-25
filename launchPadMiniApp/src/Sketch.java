import launchPadMiniClient.BRIGHTNESS;
import launchPadMiniClient.LED_COLOR;
import launchPadMiniClient.LaunchPadMini;
import processing.core.*;
import processing.event.KeyEvent;

import java.io.File;
import java.util.ArrayList;


import processing.sound.*;


public class Sketch extends PApplet {

    // Declare the processing sound variables
    SoundFile sample;
    FFT fft;
    AudioDevice device;
    AudioIn audioIn;
    // Declare a scaling factor
    int scale = 10;

    // Define how many FFT bands we want
    int bands = 8;
    int samples = (int)pow(2,bands); //2^8

    // declare a drawing variable for calculating rect width
    float r_width;

    // Create a smoothing vector
    float[] sum = new float[samples];

    // Create a smoothing factor
    float smooth_factor = 0.05f;
    PGraphics backgroundBuffer;

    LaunchPadMini controller;
    ArrayList<PImage> images;

    public static void main(String[] args) {
        PApplet.main(new String[]{"Sketch",});

    }


    public void settings() {
        size(800, 600, P2D);

    }


    public void setup() {
        prepareExitHandler();
        backgroundBuffer = createGraphics(width, height);
        images = loadImages("c:/temp/emoji");

        //frameRate(10);
        try {
            controller = new LaunchPadMini(this);

            for (int i = 0; i < controller.PAD_COUNT; i++) {
                controller.setPad(i, true);
            }
            for (int i = 0; i < controller.PAD_COUNT; i++) {
                controller.setPad(i, false);
            }
            controller.turnOnAllLeds(BRIGHTNESS.LOW);
            controller.turnOnAllLeds(BRIGHTNESS.MEDIUM);
            controller.turnOnAllLeds(BRIGHTNESS.FULL);

        } catch (Exception e) {
            println(e);
            System.exit(0);
        }

        // If the Buffersize is larger than the FFT Size, the FFT will fail
        // so we set Buffersize equal to bands
        device = new AudioDevice(this, 44000, samples);

        // Calculate the width of the rects depending on how many bands we have
        r_width = width / (float) bands;

        //Load and play a soundfile and loop it. This has to be called
        // before the FFT is created.
        sample = new SoundFile(this, "c:/temp/emoji/orchestra.mp3");
        sample.rate(.5f);
        sample.loop();

        // Create and patch the FFT analyzer
        fft = new FFT(this, samples);

        // Create the Input stream
        audioIn= new AudioIn(this, 0);
        //audioIn.start();
        //fft.input(audioIn);
        fft.input(sample);


    }


    public ArrayList<PImage> loadImages(String dir) {
        File[] files = new File(dir).listFiles();
        ArrayList<PImage> images = new ArrayList<PImage>();
        for (File file : files) {
            if (file.isDirectory()) {
                continue;

            } else {
                System.out.println("File: " + file.getAbsolutePath());
                images.add(loadImage(file.getAbsolutePath()));

            }
        }
        return images;
    }

    public void draw() {

        surface.setTitle(String.format("%d fps, frames: %d", round(frameRate), frameCount));
        clear();
        translate(0,-100);

        // controller.setLedColor(ix, launchPadMiniClient.LED_COLOR.getRandom());
        controller.reset();




/*
 int ix = (frameCount - 1) % controller.PAD_COUNT;
int j = frameCount % images.size();
 controller.setControlColor((byte)(j % 8), launchPadMiniClient.LED_COLOR.RED_FULL);

        PImage img = images.get(j);
        img.resize(8, 8);
        img.loadPixels();

        for (int i = 0; i < img.pixels.length; i++) {
            int c = img.pixels[i];
            image(img,0,0);
            int A = (c >> 24) & 0xFF;
            int R = (c >> 16) & 0xFF;
            int G = (c >> 8) & 0xFF;
                    int B = c & 0xFF;
            double bright = 255-A; //ImageUtils.perceivedL(R , G, B);

            //println(String.format("Color(ARGB): %d,%d,%d,%d, Brightness: %.2f", A,R,G,B, bright));
            int x = i % 8;
            int y = i / 8;
            controller.setLedColor(x, y, launchPadMiniClient.LED_COLOR.OFF);
            if (bright < 50)
                controller.setLedColor(x, y, launchPadMiniClient.LED_COLOR.AMBER_LOW);
            else if (bright < 100)
                controller.setLedColor(x, y, launchPadMiniClient.LED_COLOR.AMBER_FULL);
            else if (bright < 180)
                controller.setLedColor(x, y, launchPadMiniClient.LED_COLOR.YELLOW_FULL);
            else if (bright < 255)
                controller.setLedColor(x, y, launchPadMiniClient.LED_COLOR.RED_FULL);
        }
        */
        fft.analyze();

        for (int ix = 0; ix < bands; ix++) {
            int i = (int)pow(ix,2);
            // smooth the FFT data by smoothing factor
            sum[i] += (fft.spectrum[i] - sum[i]) * smooth_factor;

            // draw the rects with a scale factor
            float x = i * r_width;
            float y = height;
            float h = sum[i] *scale  ;
            rect(x, y, r_width, -h*height);

            float map = h * 5 * 8;

            for (int j = 0; j < map; j++) {
                controller.setLedColor(ix, 8 - j, LED_COLOR.RED_FULL);

            }
        }


    }


    @Override
    public void keyTyped(KeyEvent event) {

    }


    private void prepareExitHandler() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("SHUTDOWN HOOK");
                try {
                    stop();
                    if (controller != null)
                        controller.close();
                } catch (Exception ex) {
                    ex.printStackTrace(); // not much else to do at this point
                }

            }
        }));
    }
}

