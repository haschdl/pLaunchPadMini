package launchPadMiniClient.receiver;

public class MessageConstants {

    public static final byte[] RESET  = {(byte)176, (byte)0, (byte)0};

    /***
     * Turns on automatic flashing, which lets
     * Launchpad use its own flashing speed.
     */
    public static final byte[] AUTO_FLASHING_ON  = {(byte)176, (byte)0, (byte)40};

    /***
     * Turn flashing LEDs on.
     * If an external timeline is required to make the LEDs flash at a determined rate, the following
     * sequence is suggested: {@link MessageConstants#FLASHING_ON FLASHING_ON}, {@link MessageConstants#FLASHING_OFF FLASHING_OFF}
     */
    public static final byte[] FLASHING_ON  = {(byte)0xB0, (byte)0x00, (byte)0x20};

    /***
     * Turn flashing LEDs off.
     * If an external timeline is required to make the LEDs flash at a determined rate, the following
     * sequence is suggested: {@link MessageConstants#FLASHING_ON FLASHING_ON}, {@link MessageConstants#FLASHING_OFF FLASHING_OFF}
     */
    public static final byte[] FLASHING_OFF = {(byte)0xB0, (byte)0x00, (byte)0x21};

}

