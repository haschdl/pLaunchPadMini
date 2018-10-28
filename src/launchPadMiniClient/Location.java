package launchPadMiniClient;

/***
 * Location of a pad on the controller. The first button on
 * the top-left corner is located in (0,0) (row=0, col=0).
 */
public class Location {
    public int row;
    public int col;

    public Location(int col, int row) {
        this.row  = row;
        this.col = col;
    }
}
