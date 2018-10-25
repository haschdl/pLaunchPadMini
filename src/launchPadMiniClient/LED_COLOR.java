package launchPadMiniClient;

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
    protected byte code() { return code; }

    LED_COLOR(int code)
    {
        this.code = (byte)code;
    }

    /***
     * Returns a random color.
     * @return One of the eight possible colors, including {@link LED_COLOR#OFF}.
     */
    public static LED_COLOR getRandom() {
        return Extensions.randomEnum(LED_COLOR.class);
    }


    private static LED_COLOR[] list = LED_COLOR.values();

    public static LED_COLOR get(int i) {
        return list[i];
    }

}
