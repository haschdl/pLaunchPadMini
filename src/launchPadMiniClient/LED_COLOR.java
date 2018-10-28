package launchPadMiniClient;

import processing.core.PApplet;

/**
 * Hexadecimal values representing the possible combinations of color and intensity of
 * pads in the MIDI controller.
 * Values were extracted from the manufacturer documentation.
 */
public enum LED_COLOR {
    //Bit   Name    Meaning
    //6             Must be 0.
    //5..4  Green   Green LED brightness.
    //3     Clear   If 1: clear the other buffer’s copy of this LED.
    //2     Copy    If 1: write this LED data to both buffers.
    //              Note: this behaviour overrides the Clear behaviour
    //                when both bits are set.
    //1..0  Red     Red LED brightness.

    //Launchpad is able to set the brightness of green and red LEDs to one of four values:
    // Brightness   Meaning
    // 0            Off.
    // 1            Low brightness.
    // 2            Medium brightness.
    // 3            Full brightness.

    OFF(0x0C),
    GREEN_LOW(0x1C),
    GREEN_FULL(0x3C),
    AMBER_LOW(0x1D),
    AMBER_FULL(0x3F),
    YELLOW_FULL(0x3E),
    RED_LOW(0x0D),
    RED_FULL(0x0F);


    private final byte code;

    protected byte code() {
        return code;
    }

    LED_COLOR(int code) {
        this.code = (byte) code;
    }

    /***
     * Returns a random color.
     * @return One of the eight possible colors, including {@link LED_COLOR#OFF}.
     */
    public static LED_COLOR getRandom() {
        return Extensions.randomEnum(LED_COLOR.class);
    }

    /***
     * Returns the next color. If the current color is {@link LED_COLOR#RED_FULL RED_FULL},
     * the next color is {@link LED_COLOR#OFF OFF}.
     * @return The next color, from OFF to RED_FULL.
     */
    public LED_COLOR next() {
        return list[(this.ordinal() + 1) % list.length];
    }

    public LED_COLOR previous() {
        int ix = this.ordinal() == 0 ? list.length - 1 : this.ordinal() - 1;
        return list[ix];
    }

    /***
     * The color as boolean. Any color different than {@link LED_COLOR#OFF} is
     * returned as True.
     * @return True if color is not {@link LED_COLOR#OFF}.
     */
    public boolean asBoolean() {
        return this != LED_COLOR.OFF;
    }

    /**
     * Returns the LED_COLOR as a Processing color. Note that the colors are a
     * simple approximation, and meant for illustration purposes only.
     * @return
     */
    public int asColor() {
        switch (this) {
            case OFF:
                return 0;
            case GREEN_LOW:
                return Utils.color(0,125,0);
            case GREEN_FULL:
                return Utils.color(0,255,0);
            case AMBER_LOW:
                return Utils.color(178, 134, 0);
            case AMBER_FULL:
                return Utils.color(231,194,81);
            case YELLOW_FULL:
                return Utils.color(0,255,255);
            case RED_LOW:
                return Utils.color(178, 33, 25);
            case RED_FULL:
                return Utils.color(255, 28, 15);
        }
        return 0;
    }
    private static LED_COLOR[] list = LED_COLOR.values();

    public static LED_COLOR get(int i) {
        return list[i];
    }

}
