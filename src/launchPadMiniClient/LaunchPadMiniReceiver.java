package launchPadMiniClient;

import javax.sound.midi.*;

import static processing.core.PApplet.println;


/**
 * An implementation of @see javax.sound.midi.Receiver
 */
class LaunchPadMiniReceiver implements Receiver {

    byte[] lastMessage;
    private LaunchPadMini parent;
    private MidiDevice device;

    /**
     *
     * Set to true if you would like to see every notification of MIDI errors, such
     * as InvalidMidiDataException: data1 out of range.
     */
    public boolean print_errors = false;

    LaunchPadMiniReceiver(LaunchPadMini parent, MidiDevice device) {
        this.parent = parent;
        this.device = device;
    }

    /**
     * Handles new messages coming from the Midi controller.
     *
     * @param message
     * @param timeStamp
     */
    @Override
    public void send(MidiMessage message, long timeStamp) {
        lastMessage = message.getMessage();
        System.out.println(String.format("last message:%d,%d,%d", lastMessage[0], lastMessage[1], lastMessage[2]));
        if (lastMessage[0] == -112) { //PAD
            String yx = Integer.toHexString(lastMessage[1]);
            int y = Integer.parseInt(yx.substring(0, 1));
            int x = Integer.parseInt(yx.substring(1, 2));

            println(String.format("You pressed (x,y) = (%d,%d)", x, y));
            /*

            if(padToChange != null) {
                parent.invertPad(padToChange);
                if (parent.getPadMode() == PADMODE.RADIO) {
                    //switch off all other pads
                    for (PADS pad : PADS.values()) {
                        if(pad.equals(padToChange))
                            sendLedOnOff(true, padToChange);
                        else
                            sendLedOnOff(false,pad);
                    }
                }
                else if ( (parent.getPadMode() == PADMODE.TOGGLE))
                    sendLedOnOff(parent.getPad(padToChange), padToChange);

            }
            */
        }
    }


    protected void setLedColor(int ix , LED_COLOR color) {

        try {
            MidiMessage ledOnMsg = new ShortMessage((byte) 0x90, (byte) ix, color.code());
            device.getReceiver().send(ledOnMsg, 0);
        } catch (Exception e) {
            if(this.print_errors)
            System.out.println("Error sending Midi message: " + e);
        }
    }

    /***
     * This command sets the LEDs under the top row of round buttons, normally reserved for Automap
     * and Live features. The controller number determines the button’s location: the leftmost button
     * (cursor up/learn) is 68h (104 in decimal), and the controller number increases from left to right.
     * The data byte sets the LED colour, and takes exactly the same format as the velocity byte in noteon messages.
     * @param controlIx
     * @param color
     */
    public void setControlColor(int controlIx, LED_COLOR color) {
        try {
            MidiMessage ledOnMsg = new ShortMessage((byte) 0xB0, (byte)(0x68 +controlIx), color.code());
            device.getReceiver().send(ledOnMsg, 0);
        } catch (Exception e) {
            System.out.println("Error sending Midi message: " + e);
        }
    }

    protected void sendLed(boolean onOff, int ix) {

        try {
            //A Launchpad MIDI message is always three bytes long.
            //Note on: 90h, Key, Velocity
            //Key = (10h x Row) + Column (=ix)

            //Where Template is 00h-07h (0-7) for the 8 user templates, and 08h-0Fh (8-15) for the 8 factory
            //templates; LED is the index of the pad/button (00h-07h (0-7) for pads, 08h-0Bh (8-11) for buttons);
            //and Value is the velocity byte that defines the brightness values of both the red and green LEDs.

            LED_COLOR color = onOff ? LED_COLOR.RED_FULL : LED_COLOR.OFF;
            //byte[] ledOn = new byte[] { (byte)0x90,(byte)ix, color };

            MidiMessage ledOnMsg = new ShortMessage((byte) 0x90, (byte) ix, color.code());

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
    protected void sendShortMessage(byte data0, byte data1, byte data2) {

        try {
            MidiMessage msg = new ShortMessage(data0, data1, data2);
            device.getReceiver().send(msg, 0);
        } catch (Exception e) {
            System.out.println("Error sending Midi message: " + e);
        }
    }


    @Override
    public void close() {

    }


}
