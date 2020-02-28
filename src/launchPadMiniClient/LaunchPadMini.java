package launchPadMiniClient;

import launchPadMiniClient.receiver.LaunchPadListener;
import launchPadMiniClient.receiver.LaunchPadMiniReceiver;
import launchPadMiniClient.receiver.MessageConstants;
import processing.core.*;

import javax.sound.midi.*;
import java.lang.reflect.*;

import static processing.core.PApplet.println;

/***
 * A wrapper for the MIDI controller Novation LaunchPad Mini.<br > The most trivial application of this
 * wrapper is to adjust and control your Processing sketch using the pads of the controller,
 * by attaching pads to variables.
 *
 */
public class LaunchPadMini implements LaunchPadListener {

    public static final int PAD_COUNT = 81;
    private static final String DEVICE_NAME = "Launchpad Mini";
    private static final String controlChangedEventName = "launchControllerChanged";
    private static final String padChangedEventName = "launchPadMiniPadChanged";
    public LOG_MODE LogMode = LOG_MODE.ERROR;
    public MATRIX_MODE matrix_mode = MATRIX_MODE.MATRIX_8x8;
    public PAD_MODE pad_mode = PAD_MODE.TOGGLE;
    private Method controllerChangedEventMethod, padChangedEventMethod;
    private MidiDevice deviceIn;
    private MidiDevice deviceOut;
    private LaunchPadMiniReceiver receiver;
    private PApplet parent;

    private LedMatrix matrix = new LedMatrix(PAD_COUNT);

    public LaunchPadMini(PApplet parent) throws LaunchPadNotConnectedException {
        this.parent = parent;

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {

            if (info.getName().equals(DEVICE_NAME)) {
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(info);
                    if (info.getClass().getName().equals("com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo")) {
                        deviceIn = device;
                        println(String.format("Connected to %s MIDI Input.", DEVICE_NAME));
                    } else {
                        println(String.format("Connected to %s MIDI Output.", DEVICE_NAME));
                        deviceOut = device;
                    }
                } catch (MidiUnavailableException e) {
                    throw new LaunchPadNotConnectedException();
                }
            }
        }

        if (deviceIn == null || deviceOut == null) {
            throw new LaunchPadNotConnectedException();
        }

        try {
            deviceIn.open();
            deviceOut.open();

            receiver = new LaunchPadMiniReceiver(this, deviceOut);
            receiver.addListener(this);

            deviceIn.getTransmitter().setReceiver(receiver);

            println("Resetting the controller...");
            reset();

            println(DEVICE_NAME + " ready!");
        } catch (MidiUnavailableException e) {
            throw new LaunchPadNotConnectedException();
        }
        try {
            controllerChangedEventMethod =
                    parent.getClass().getMethod(controlChangedEventName);
        } catch (Exception e) {
            // no such method, or an error.. which is fine, just ignore
        }

        try {
            padChangedEventMethod =
                    parent.getClass().getDeclaredMethod(padChangedEventName, new Class[]{int.class, int.class});
        } catch (Exception e) {
            // no such method, or an error.. which is fine, just ignore
        }
    }

    /***
     * Resets the controller. All LEDs are turned off, and the mapping mode,
     * buffer settings, and duty cycle are reset to their
     * default values.
     */
    public void reset() {
        try {
            deviceOut.getReceiver().send(Utils.getResetMessage(), 0);
            for (int i = 0; i < PAD_COUNT; i++) {
                matrix.set(i, LED_COLOR.OFF);
            }
        } catch (MidiUnavailableException e) {
            println(String.format("%s: Error sending reset message!", DEVICE_NAME));
        }
    }

    /***
     * The state of a given pad as a boolean value.
     * Alternative, you can get the status of a pad a int
     * value with {@link LaunchPadMini#getPadInt(int)}
     * @return True if the pad is "on", False otherwise.
     */
    public boolean getPad(int ix) {
        return (matrix.get(ix) != LED_COLOR.OFF);
    }

    public LED_COLOR getPad(int col, int row) {

        return matrix.get(col, row);
    }

    /***
     * The state of a given pad as int value (0 or 1).
     * See also {@link LaunchPadMini#getPad(int) getPad(int)}
     * which returns the state as a boolean.
     * @param ix The index of the pad to check.
     * @return 1 if the pad is on, 0 otherwise.
     */
    public int getPadInt(int ix) {
        return matrix.get(ix).asBoolean() ? 0 : 1;
    }

    public void setPad(int col, int row, boolean value) {
        boolean currVal = matrix.get(col, row).asBoolean();

        if (currVal != value) {
            togglePad(col, row);
        }
    }

    /**
     * Turns on all LED lights of the controller, including the control buttons.
     *
     * @param level A valid brightness level (see {@link BRIGHTNESS}.
     */
    public void turnOnAllLeds(BRIGHTNESS level) {
        //Hex version B0h, 00h, 7D-7Fh.
        //Decimal version 176, 0, 125-127.
        //The last byte can take one of three values:
        //  Hex Decimal Meaning
        //  7Dh 125     Low brightness test.
        //  7Eh 126     Medium brightness test.
        //  7Fh 127     Full brightness test.
        receiver.sendShortMessage((byte) 0xB0, (byte) 0x00, level.code());
    }

    public void setLedFlashing() {
        receiver.sendShortMessage(MessageConstants.FLASHING_ON);
    }

    /**
     * Inverts the value and turns the given pad on and off.
     *
     * @param row
     */
    public void togglePad(int col, int row) {
        boolean currVal = matrix.get(col, row).asBoolean();
        LED_COLOR newColor = currVal ? LED_COLOR.OFF : LED_COLOR.RED_FULL;
        setLedColor(col, row, newColor);
        midiLaunchControllerChanged();
        //padChanged(ix);
    }

    public void setLedColor(int x, int y, LED_COLOR color) {
        if (x > 9 || y > 9)
            throw new IllegalArgumentException(String.format("Both x and y must be smaller than 9 (0-8). x: %d  y:%d", x, y));

        if (matrix.get(x, y) == color)
            return;

        if (matrix_mode == MATRIX_MODE.MATRIX_8x8 || matrix_mode == MATRIX_MODE.MATRIX_9x8) {
            receiver.setLedColor(x, y, color);
            matrix.set(x, y, color);
        } else if (matrix_mode == MATRIX_MODE.MATRIX_9x9) {
            //First row is control buttons
            matrix.set(x, y, color);

            if (y == 0)
                setControlColor((byte) x, color);
            else
                receiver.setLedColor((byte) x, (byte) (y - 1), color);
        }
    }

    /***
     * Irrespective of the mapping chosen, this will update the 8x8 grid in left-to-right, top-to-bottom
     * order, then the eight scene launch buttons in top-to-bottom order, and finally the eight
     * Automap/Live buttons in left-to-right order (these are otherwise inaccessible using note-on
     * messages). Overflowing data will be ignored.
     *
     * @param colors
     */
    public void rapidLedUpdate(LED_COLOR[] colors) {
        /***
         * Rapid LED update: Sending a MIDI channel 3 note-on message enters a special
         * LED update mode. All eighty LEDs may be set using only forty consecutive
         * instructions, without having to send any key addresses.
         */

        //MIDI channel 3 note-on message
        ShortMessage msg = new ShortMessage();
        try {
            msg.setMessage(ShortMessage.NOTE_ON, 3, 0, 0);
        } catch (InvalidMidiDataException e) {
        }
        receiver.send(msg, 0);

        //Expectes colors.lenght to be 80!
        //Note that this method will update 2 leds at a time
        for (int i = 0, l = colors.length; i < l; i += 2) {
            receiver.sendShortMessage((byte) 0x92, colors[i].code(), colors[i + 1].code());
        }

        //80h, 90h, or B0h.
        receiver.sendShortMessage((byte) 0x80, (byte) 0, (byte) 0);
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

    public void padChanged(int col, int row) {
        if (padChangedEventMethod != null) {
            try {
                //controllerChangedEventMethod.invoke(parent, new Object[]{this});
                padChangedEventMethod.invoke(parent, col, row);
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
    public void dispose() {
        try {
            deviceOut.getReceiver().send(Utils.getResetMessage(), 0);
        } catch (MidiUnavailableException e) {
            println(String.format("%s: error sending reset message. %s ", DEVICE_NAME, e));
        }
        if (deviceIn.isOpen()) deviceIn.close();

        if (deviceOut.isOpen()) deviceOut.close();
    }

    @Override
    public void padPressed(int col, int row) {
        if (this.LogMode == LOG_MODE.VERBOSE)
            println(String.format("Pad pressed at (%d,%d)! Changing color...", col, row));

        padChanged(col, row);

        if (pad_mode == PAD_MODE.TOGGLE)
            togglePad(col, row);
        else if (pad_mode == PAD_MODE.LOOP)
            updatePadToNext(col, row);
    }

    public void updatePadToNext(int col, int row) {
        setLedColor(col, row, matrix.get(col, row).next());
        midiLaunchControllerChanged();
        //padChanged(ix);
    }
}