package Testing;

import org.junit.jupiter.api.*;
import BullsAndCows.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import static java.lang.Float.isNaN;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    //<editor-fold desc="Method Tests">

    @Test
    void NonNaNAccuracyTest(){
        Player p = new Player("TestUser", 3, 5, 12, 1, 1);
        assertEquals((float) 300/12, p.bullsAccuracy());
        assertEquals((float) 500/12, p.cowsAccuracy());
    }

    @Test
    void NaNAccuracyTest(){
        Player p = new Player("TestUser", 0, 0, 0, 0, 0);
        assertTrue(isNaN(p.bullsAccuracy()));
        assertTrue(isNaN(p.cowsAccuracy()));
    }

    //</editor-fold>

}
