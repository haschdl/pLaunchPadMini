package launchPadMiniClient;

import processing.core.*;

import javax.sound.midi.*;
import java.lang.reflect.*;

import static processing.core.PApplet.floor;
import static processing.core.PApplet.println;

/***
 * A wrapper for the MIDI controller Novation LaunchPad Mini.<br > The most trivial application of this
 * wrapper is to adjust and control your Processing sketch using the pads of the controller,
 * by attaching pads to variables.
 *
 */
public class LaunchPadMini {

    private static final String DEVICE_NAME = "Launchpad Mini";

    private Method controllerChangedEventMethod, padChangedEventMethod;
    private static final String controlChangedEventName = "launchControllerChanged";
    private static final String padChangedEventName = "launchControllerPadChanged";



    private MidiDevice deviceIn;
    private MidiDevice deviceOut;
    private LaunchPadMiniReceiver receiver;

    public static final int PAD_COUNT = 81;
    private boolean[] padStatus = new boolean[PAD_COUNT];

    public MATRIX_MODE matrix_mode = MATRIX_MODE.MATRIX_8x8;

    private PApplet parent;

    private LED_COLOR[] matrix = new LED_COLOR[PAD_COUNT];



    public LaunchPadMini(PApplet parent) throws MidiUnavailableException {
        this.parent = parent;

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {

            if (info.getName().equals(DEVICE_NAME)) {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if (info.getClass().getName().equals("com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo")) {
                    deviceIn = device;
                    println(String.format("Connected to %s MIDI Input.", DEVICE_NAME));
                } else {
                    println(String.format("Connected to %s MIDI Output.", DEVICE_NAME));
                    deviceOut = device;
                }
            }
        }

        if (deviceIn == null || deviceOut == null) {
            println(String.format("%s not connected!", DEVICE_NAME));
            return;
        }

        try {
            controllerChangedEventMethod =
                    parent.getClass().getMethod(controlChangedEventName);
        } catch (Exception e) {
            // no such method, or an error.. which is fine, just ignore
        }


        try {
            padChangedEventMethod =
                    parent.getClass().getMethod(padChangedEventName);
        } catch (Exception e) {
            // no such method, or an error.. which is fine, just ignore
        }


        deviceIn.open();
        deviceOut.open();

        receiver = new LaunchPadMiniReceiver(this, deviceOut);

        deviceIn.getTransmitter().setReceiver(receiver);


        println("Resetting the controller...");
        reset();


        println( DEVICE_NAME +  " ready!");
    }

    /***
     * Resets the controller. All LEDs are turned off, and the mapping mode,
     * buffer settings, and duty cycle are reset to their
     * default values.
     */
    public void reset() {
        try {
            deviceOut.getReceiver().send(Utils.getResetMessage(), 0);
            for(int i = 0; i< PAD_COUNT;i++) {
                matrix[i] = LED_COLOR.OFF;
            }
        }
        catch (MidiUnavailableException e)
        {
            println(String.format("%s: Error sending reset message!", DEVICE_NAME));
        }
    }
    /***
     * The status of a given pad as a boolean value.
     * Alternative, you can get the status of a pad a int value with {@link LaunchPadMini#getPadInt(int)}
     * @return True if the pad is "on", False otherwise.
     */
    public boolean getPad(int ix) {
        return padStatus[ix];
    }

    /***
     * The status of a given pad as int value.
     * @param ix The index of the pad to check.
     * @return 1 if the pad is on, 0 otherwise.
     */
    public int getPadInt(int ix ) {
        return padStatus[ix] ? 1 : 0;
    }

    public void setPad(int ix, boolean value) {
        if (padStatus[ix] != value) {
            invertPad(ix);
        }
    }


    public void turnOnAllLeds(BRIGHTNESS level)  {
        //Hex version B0h, 00h, 7D-7Fh.
        //Decimal version 176, 0, 125-127.
        //The last byte can take one of three values:
        //  Hex Decimal Meaning
        //  7Dh 125     Low brightness test.
        //  7Eh 126     Medium brightness test.
        //  7Fh 127     Full brightness test.
        receiver.sendShortMessage((byte)0xB0, (byte)0x00, level.code() );
    }

    /**
     * Inverts the value and turns the given pad on and off.
     *
     * @param row
     */
    public void invertPad(int row, int col) {
        invertPad(row * 9 + col);
    }



    protected int getNote(int ix) {
        int row = floor(ix / 9);
        int col = ix % 9;
        return 16 * row+col;
    }

    public void invertPad(int ix) {


        boolean curValue = padStatus[ix];
        padStatus[ix] = !curValue;

        receiver.sendLed(!curValue, getNote(ix));
        midiLaunchControllerChanged();
        padChanged(ix);
    }

    public void setLedColor(int ix, LED_COLOR color) {
        receiver.setLedColor(getNote(ix),color);
    }

    public void setLedColor(int x, int y, LED_COLOR color) {
        if (x>9 || y > 9)
            throw new IllegalArgumentException(String.format("Both x and y must be smaller than 9 (0-8). x: %d  y:%d",x,y));

        int ix;
        if(matrix_mode == MATRIX_MODE.MATRIX_8x8 ||matrix_mode == MATRIX_MODE.MATRIX_9x8) {
            String x_coor = "" + x;
            String y_coor = "" + y;
            String yx_coor = y_coor + x_coor;
            ix = Integer.parseInt(yx_coor, 16);

            //updates only if different.
            //TODO: improve
            if(matrix[y*8+x] != color) {
                receiver.setLedColor(ix, color);
                matrix[y*8+x] = color;
            }
        }
        else if(matrix_mode == MATRIX_MODE.MATRIX_9x9) {
            //First row is control buttons
            if (y == 0)
                setControlColor((byte)x, color);
            else
                setLedColor((byte)x, (byte)(y-1), color);
        }
    }



    /***
     * This command sets the LEDs under the top row of round buttons
     * @param controlIx A number between 0 and 7 (0 is the left-most control button on the top row).
     * @param color A color to set the control button.
     */
    public void setControlColor(byte controlIx, LED_COLOR color) {
        receiver.setControlColor(controlIx, color);
    }

    public void midiLaunchControllerChanged() {
        if (controllerChangedEventMethod != null) {
            try {
                //controllerChangedEventMethod.invoke(parent, new Object[]{this});
                controllerChangedEventMethod.invoke(parent);
            } catch (Exception e) {
                System.err.println("Disabling controllerChangedEventMethod() for " + this.getClass().getName() +
                        " because of an error.");
                e.printStackTrace();
                controllerChangedEventMethod = null;
            }
        }
    }

    public void padChanged(int ix) {
        if (padChangedEventMethod != null) {
            try {
                //controllerChangedEventMethod.invoke(parent, new Object[]{this});
                padChangedEventMethod.invoke(parent, ix);
            } catch (Exception e) {
                System.err.println("Disabling " + padChangedEventName + "() for " + this.getClass().getName() +
                        " because of an error.");
                e.printStackTrace();
                padChangedEventMethod = null;
            }
        }
    }




    /***
     * Clean-up operations executed when when
     * the parent sketch shuts down.
     */
    public void close() {
        try {
            deviceOut.getReceiver().send(Utils.getResetMessage(), 0);
        }
        catch(MidiUnavailableException e) {
            println(String.format("%s: error sending reset message. %s " ,DEVICE_NAME, e));
        }
        if (deviceIn.isOpen()) deviceIn.close();

        if (deviceOut.isOpen()) deviceOut.close();
    }
}