package launchPadMiniClient;

/**
 * A pad or button in the device. You can
 */
public class Pad {
    /**
     * The row in which the pad is located, from 0 to 9.
     */
    public int row;
    /**
     * The column in which the pad is located, from 0 to 9.
     */
    public int column;

    /**
     * The {@link LED_COLOR} for this pad.
     */
    public LED_COLOR ledColor;

    public Pad(int column, int row, LED_COLOR ledColor) {
        this.column = column;
        this.row = row;
        this.ledColor = ledColor;
    }

    /**
     * @return An array of two elements with the values of {@link Pad#column} and {@link Pad#row} of the pad, in this order.
     */
    public int[] getPosition() {
        return new int[]{row, column};
    }

    /**
     * @return True if {@link Pad#ledColor} is different than {@link LED_COLOR#OFF}
     */
    public boolean asBoolean() {
        return (ledColor != LED_COLOR.OFF);
    }

    public char toChar() {
        switch (ledColor) {
            case OFF:
                return '-';
            case AMBER_FULL:
                return 'A';
            case RED_FULL:
                return 'R';
            case GREEN_FULL:
                return 'G';
            case AMBER_LOW:
                return 'a';
            case GREEN_LOW:
                return 'g';
            case RED_LOW:
                return 'r';
            case YELLOW_FULL:
                return 'Y';
            default:
                return 'x';
        }
    }
}
