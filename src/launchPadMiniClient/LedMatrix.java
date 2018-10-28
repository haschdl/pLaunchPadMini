package launchPadMiniClient;

import java.util.ArrayList;

public class LedMatrix {

    MATRIX_MODE mode = MATRIX_MODE.MATRIX_8x8;

    private ArrayList<LED_COLOR> values = new ArrayList<LED_COLOR>();

    public LedMatrix(int capacity) {
        values = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) {
            values.add(LED_COLOR.OFF);
        }
    }


    private int index(int col, int row) {
        if (col > 8 || row > 8)
            throw new IllegalArgumentException(String.format("Both col and row must be smaller than 9 (0-8). col: %d  row:%d", col, row));

        return row * 9 + col;

    }


    public void set(int x, int y, LED_COLOR color) {
        values.set(index(x, y), color);
    }

    public void set(int index, LED_COLOR color) {
        values.set(index, color);
    }


    public LED_COLOR get(int ix) {
        return values.get(ix);
    }


    public LED_COLOR get(int col, int row) {

        return values.get(index(col,row));
    }
}
