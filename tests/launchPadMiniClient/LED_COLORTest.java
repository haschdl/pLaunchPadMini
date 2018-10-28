package launchPadMiniClient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LED_COLORTest {

    @Test
    void code() {
    }

    @Test
    void next() {
        LED_COLOR col = LED_COLOR.RED_FULL.next();
        assertTrue(col == LED_COLOR.OFF);

        assertTrue(col.previous() == LED_COLOR.RED_FULL);
    }

    @Test
    void previous() {
        LED_COLOR col = LED_COLOR.YELLOW_FULL;
        assertTrue(col.previous() == LED_COLOR.AMBER_FULL);
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