package launchPadMiniClient;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

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


}
