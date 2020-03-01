package launchPadMiniClient;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Led matrix holds the state of each pad in the controller.
 */
public class LedMatrix {

    MATRIX_MODE mode;

    private List<Pad> values;

    public LedMatrix(MATRIX_MODE mode) {
        this.mode = mode;
        this.values = new ArrayList<>();
        int capacity = 0;
        switch (mode) {
            case MATRIX_8x8:
                capacity = 63;
                break;
            case MATRIX_9x9:
                capacity = Constants.BUTTON_COUNT;
                break;
            default:
                throw new NotImplementedException();
        }

        for (int i = 0; i < capacity; i++) {
            int row = getRow(i);
            int col = getColumn(i);
            values.add(new Pad(col, row, LED_COLOR.OFF));
        }
    }

    private int getColumn(int index) {
        if (this.mode == MATRIX_MODE.MATRIX_9x9) {
            if (index < 8)
                return (index % 8);
            else
                return (index + 1) % 9;
        }
        return index % 8;
    }

    private int getRow(int index) {
        if (this.mode == MATRIX_MODE.MATRIX_9x9) {
            if (index < 8)
                return 0;
            else
                return Math.floorDiv(index + 1, 9);
        }
        return Math.floorDiv(index, 9);
    }

    private int index(int col, int row) {
        if (col > 8 || row > 8)
            throw new IllegalArgumentException(String.format("Both col and row must be smaller than 9 (0-8). col: %d  row:%d", col, row));
        return row * 9 + col;
    }

    public void set(int x, int y, LED_COLOR color) {
        Pad p = get(x, y);
        p.ledColor = color;
    }

    public void set(int index, LED_COLOR color) {
        int row = getRow(index);
        int column = getColumn(index);
        Pad p = get(column, row);
        p.ledColor = color;
    }

    public List<Pad> getAll() {
        return values;
    }

    public Pad get(int col, int row) {
        Optional<Pad> p = values.stream().filter(i -> i.row == row && i.column == col).findFirst();
        if (p.isPresent())
            return p.get();
        return null;
    }

    public Pad get(int index) {
        int row = getRow(index);
        int col = getColumn(index);
        Optional<Pad> p = values.stream().filter(i -> i.row == row && i.column == col).findFirst();
        return p.get();
    }

    /**
     * @return A string representing the matrix.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int j = 0; j < Constants.ROW_COUNT; j++) {
            String line = "|";
            for (int i = 0; i < Constants.COLUMN_COUNT; i++) {
                Pad p = get(i, j);
                if (p != null)
                    line += get(i, j).toChar() + "|";
                else
                    line += "X" + "|";
            }
            b.append(line + System.lineSeparator());
        }
        return b.toString();
    }
}
