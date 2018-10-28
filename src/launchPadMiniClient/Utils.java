package launchPadMiniClient;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;


/***
 * Helper methods to communicate with MIDI controller.
 */
public class Utils {

    private static int RESET = 176;

    protected static MidiMessage getResetMessage() {
        try {
            ShortMessage reset = new ShortMessage(RESET, 0, 0);
            return reset;
        } catch (InvalidMidiDataException e) {
            return null;
        }
    }

    /***
     * Gets a MIDI note from an index (0 to 63).
     * @param ix The number of the button, from 0 (top-left corner) to 63 (bottom right).
     * @return The corresponding MIDI note of the controller.
     */
    public final static byte getNote(int ix) {
        int row = (int)Math.floor(ix / 9);
        int col = ix % 9;
        return (byte) (16 * row+col);
    }

    public final static byte getNote(int row, int col, MATRIX_MODE mode) {
        if (mode == MATRIX_MODE.MATRIX_8x8 | mode == MATRIX_MODE.MATRIX_9x8) {
            return (byte) (16 * row + col);
        }
        else
            return 0;
    }

    public final static Location getLocation(byte note) {
        String yx = Integer.toHexString(note);

        if (yx.length() == 1)
            yx = "0" + yx;

        int y = Integer.parseInt(yx.substring(0, 1));
        int x = Integer.parseInt(yx.substring(1, 2));
        return new Location(x,y);

    }


    public final static int color(int v1, int v2, int v3) {
        if (v1 > 255) {
            v1 = 255;
        } else if (v1 < 0) {
            v1 = 0;
        }

        if (v2 > 255) {
            v2 = 255;
        } else if (v2 < 0) {
            v2 = 0;
        }

        if (v3 > 255) {
            v3 = 255;
        } else if (v3 < 0) {
            v3 = 0;
        }

        return -16777216 | v1 << 16 | v2 << 8 | v3;
    }


}
