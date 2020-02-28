package launchPadMiniClient.receiver;
import launchPadMiniClient.*;
import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.println;

/**
 * An implementation of @see javax.sound.midi.Receiver
 */
public class LaunchPadMiniReceiver implements Receiver {
    private List<LaunchPadListener> listeners;

    private LaunchPadMini parent;
    private MidiDevice device;

    private static final String padChangedEventName = "launchPadMiniPadChanged";

    public LaunchPadMiniReceiver(LaunchPadMini parent, MidiDevice device) {
        this.parent = parent;
        this.device = device;
        listeners = new ArrayList<>();
    }
    public void addListener(LaunchPadListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * Handles new messages coming *from* the Midi controller.
     *
     * @param message A MidiMessage from the controller.
     * @param timeStamp Long value containing the timestamp.
     */
    @Override
    public void send(MidiMessage message, long timeStamp) {
        byte[] lastMessage = message.getMessage();

        if(parent.LogMode == LOG_MODE.VERBOSE)
            System.out.println(String.format("[RECEIVER] Last message:%d,%d,%d", lastMessage[0], lastMessage[1], lastMessage[2]));

        if (lastMessage[0] == -112 && lastMessage[2] == 127) { //PAD, Note ON (lastMessage[2] = 127
            Location loc = Utils.getLocation(lastMessage[1]);
            // Notify everybody that may be interested.
            for (LaunchPadListener hl : listeners)
                hl.padPressed(loc.col,loc.row);

            if (parent.LogMode == LOG_MODE.VERBOSE) {
                println(String.format("[RECEIVER] You pressed (x,y) = (%d,%d)", loc.col, loc.row));
            }
        }
    }

    public void setLedColor(int col, int row, LED_COLOR color) {
        //updates model
        String x_coor = "" + col;
        String y_coor = "" + row;
        String yx_coor = y_coor + x_coor;
        byte note = (byte) Integer.parseInt(yx_coor, 16);

        setLedColor(note, color);
    }

    private void setLedColor(byte note , LED_COLOR color) {

        try {
            if (parent.LogMode == LOG_MODE.VERBOSE) {
                Location loc = Utils.getLocation(note);
                System.out.println(String.format("[RECEIVER] Sending LED ON message.Location: %d,%d Note: %d ", loc.col, loc.row, note));
            }
            MidiMessage ledOnMsg = new ShortMessage((byte) 0x90, note, color.code());
            device.getReceiver().send(ledOnMsg, 0);
        } catch (Exception e) {
            if (parent.LogMode == LOG_MODE.VERBOSE)
                System.out.println("[RECEIVER] Error sending Midi message: " + e);
        }
    }

    /***
     * This command sets the LEDs under the top row of round buttons, normally reserved for Automap
     * and Live features.
     * The data byte sets the LED colour, and takes exactly the same format as the velocity byte in noteon messages.
     * @param controlIx The button’s location: the leftmost button is 0,
     *                  and the controller number increases from left to right.
     * @param color The color to set the button.
     */
    public void setControlColor(int controlIx, LED_COLOR color) {
        try {
            //The controller number determines the button’s location: the leftmost button
            //(cursor up/learn) is 68h (104 in decimal), and the controller number increases from left to right.
            MidiMessage ledOnMsg = new ShortMessage((byte) 0xB0, (byte) (0x68 + controlIx), color.code());
            device.getReceiver().send(ledOnMsg, 0);

        } catch (Exception e) {
            System.out.println("Error sending Midi message: " + e);
        }
    }



    /***
     * Sends a generic 3-byte MIDI message to the controller.
     * @param data0 First byte
     * @param data1 Second byte
     * @param data2 Third byte
     */
    public void sendShortMessage(byte data0, byte data1, byte data2) {

        try {
            MidiMessage msg = new ShortMessage(data0, data1, data2);

            device.getReceiver().send(msg, 0);
        } catch (Exception e) {
            System.out.println("Error sending Midi message: " + e);
        }
    }

    /***
     * Sends a generic 3-byte MIDI message to the controller.
     * @param data An array containing three bytes.
     */
    public void sendShortMessage(byte[] data) {
        sendShortMessage(data[0],data[1],data[2]);
    }


    @Override
    public void close() {

    }


}
