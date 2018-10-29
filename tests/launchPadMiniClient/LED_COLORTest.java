package launchPadMiniClient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LED_COLORTest {

    @Test
    void code() {
    }

    @Test
    void next() {
        LED_COLOR col = LED_COLOR.GREEN_FLASHING.next();
        assertSame(col, LED_COLOR.OFF);

        assertSame(col.previous(), LED_COLOR.GREEN_FLASHING);
    }

    @Test
    void previous() {
        LED_COLOR col = LED_COLOR.YELLOW_FULL;
        assertTrue(col.previous() == LED_COLOR.AMBER_FULL);
    }

    @Test
    void get() {
        LED_COLOR c0 = LED_COLOR.get(0);
        LED_COLOR c1 = LED_COLOR.get(1);
        LED_COLOR c2 = LED_COLOR.get(2);
        LED_COLOR c3 = LED_COLOR.get(3);
        LED_COLOR c4 = LED_COLOR.get(4);
        LED_COLOR c5 = LED_COLOR.get(5);
        LED_COLOR c6 = LED_COLOR.get(6);
        LED_COLOR c7 = LED_COLOR.get(7);


        assertSame(c0, LED_COLOR.OFF);
        assertSame(c1, LED_COLOR.GREEN_LOW);
        assertSame(c2, LED_COLOR.GREEN_FULL);
        assertSame(c3, LED_COLOR.AMBER_LOW);
        assertSame(c4, LED_COLOR.AMBER_FULL);
        assertSame(c5, LED_COLOR.YELLOW_FULL);
        assertSame(c6, LED_COLOR.RED_LOW);
        assertSame(c7, LED_COLOR.RED_FULL);
    }




    @Test
    void getRandom() {
        LED_COLOR col = LED_COLOR.getRandom();
        assertNotNull(col);
    }
    @Test
    void asBoolean() {

        assertTrue(LED_COLOR.RED_FULL.asBoolean());
        assertTrue(LED_COLOR.YELLOW_FULL.asBoolean());
        assertTrue(LED_COLOR.AMBER_FULL.asBoolean());
        assertTrue(LED_COLOR.GREEN_FULL.asBoolean());
        assertFalse(LED_COLOR.OFF.asBoolean());
    }
}