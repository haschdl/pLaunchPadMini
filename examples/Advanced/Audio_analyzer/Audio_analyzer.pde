import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;


import launchPadMiniClient.*;

// Declare the processing sound variables
Minim minim;  
AudioPlayer soundFile;

FFT fftLin;



// Declare a scaling factor
float scale = .05;

// Define how many FFT bands we want
int bands = 8;
int samples = (int)pow(2, bands); //2^8

// declare a drawing variable for calculating rect width
float r_width;

// Create a smoothing vector
float[] sum = new float[samples];

// Create a smoothing factor
float smooth_factor = 0.08f;
PGraphics backgroundBuffer;

LaunchPadMini controller;

public void settings() {
  size(800, 600, P2D);
}

public void setup() {
  backgroundBuffer = createGraphics(width, height);

  try {
    controller = new LaunchPadMini(this);
    //controller.turnOnAllLeds(BRIGHTNESS.LOW);
    controller.setLedColor(3, 0, LED_COLOR.RED_FULL);
    controller.reset();
  } 
  catch(Exception e) {
    println("It looks like Launch Pad MINI is not connected to this computer :(");
    exit();
  }

  // Calculate the width of the rects depending on how many bands we have
  r_width = width / (float) bands;

  //Load and play a soundfile and loop it. This has to be called
  // before the FFT is created.
  minim = new Minim(this);
  soundFile = minim.loadFile("beat.aiff", 1024); 

  // loop the file

  soundFile.loop();
  soundFile.skip(22000);

  fftLin = new FFT( soundFile.bufferSize(), soundFile.sampleRate() );

  // calculate the averages by grouping frequency bands linearly. use 30 averages.
  fftLin.linAverages( 32 );
}

public void draw() {

  surface.setTitle(String.format("%d fps, frames: %d", round(frameRate), frameCount));
  clear();
  fftLin.forward( soundFile.mix );

  for (int ix = 0; ix < bands; ix++) {
    int i = (int)pow(ix, 2);
    // smooth the FFT data by smoothing factor
    sum[i] += (fftLin.getBand(i) - sum[i]) * smooth_factor;

    // draw the rects with a scale factor
    float x = i * r_width;
    float y = height;
    float h = sum[i] *scale  ;
    rect(x, y, r_width, -h*height);

    float ledHeight = constrain(round(h  * 7), 0, 7);
    for (int j = 0; j < 8; j++) { // 0..7
      if (ledHeight == 0 || ledHeight < j) {
        controller.setLedColor(ix, 7-j, LED_COLOR.OFF);
        continue;
      }
      controller.setLedColor(ix, 7-j, LED_COLOR.get(j==0 ? 1 : j));
    }
  }
}
