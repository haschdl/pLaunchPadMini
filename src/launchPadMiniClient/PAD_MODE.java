package launchPadMiniClient;

/***
 * You can use the pads in two ways.
 * {@link PAD_MODE#TOGGLE} means that pads work as on/off switches. Tapping on the pad turns the red led on and off.
 *  You can use this mode to attach a pad to a boolean variable.
 *
 * {@link PAD_MODE#LOOP} means that pads will loop around all the possible the colors. You can use this mode to use
 *  * the same pad to set several values to a variable.
 *
 * {@link PAD_MODE#LOOP_ARRAY} means that pads will loop around a list of pre-defined colors.
 *  With this you can customized the behavior of each pad. You must call setPadLoopArray()
 *  with the sequence of colors to use.
 *
 */
public enum PAD_MODE {
    TOGGLE,
    LOOP,
    LOOP_ARRAY,
}
