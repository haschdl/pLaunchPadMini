package launchPadMiniClient;

/***
 * Defines how Processing sketches will use LaunchPad Mini pads.
 *
 * Default is MATRIX_8x8, meaning that calling {@link LaunchPadMini#setLedColor(int, int, LED_COLOR) setLedColor} will
 * only work for the pads (excluding the round control buttons on top and on the right side).
 *
 */
public enum MATRIX_MODE {

    /**
     * Treats the controller as a matrix of 8x8 cells. You can set the color of a pad
     * by using {@link LaunchPadMini#setLedColor(int, LED_COLOR)}, where ix between
     * 0 and 63 (see diagram).
     * If you want to lit the round buttons on top you must call
     * {@link LaunchPadMini#setControlColor(byte, LED_COLOR ) setControlColor}.
     *
     *      ___ ___ ___ ___ ___ ___ ___ ___
     *     | o | o | o | o | o | o | o | o |
     *     |-------------------------------|---
     *     | 0 | 1 | 2 | 3 | 4 | 4 | 6 | 7 | o |
     *     | 8 | 9 |10 |11 |12 |13 |14 |15 | o |
     *     |16 |17 |18 |19 |20 |21 |22 |23 | o |
     *     |24 |   |   |   |   |   |   |   | o |
     *     |32 |   |   |   |   |   |   |   | o |
     *     |40 |   |   |   |   |   |   |...| o |
     *     |48 |   |   |   |   |   |   |...| o |
     *     |56 |57 |58 |59 |60 |61 |62 |63 | o |
     *     |------------------------------------
     *
     *
     *
     */
    MATRIX_8x8,
    MATRIX_9x8,
    MATRIX_9x9


}
