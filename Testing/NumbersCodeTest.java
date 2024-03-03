package Testing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import BullsAndCows.*;

class NumbersCodeTest {

    @Test
    void hasDuplicateCharacter() {
        String[] stimulus = {"1111","5785","3663"};
        for(String s: stimulus) {
            assertTrue(NumbersCode.hasDuplicateCharacter(s));
        }
        stimulus = new String[]{"1234", "9876", "4321"};
        for(String s: stimulus) {
            assertFalse(NumbersCode.hasDuplicateCharacter(s));
        }
    }

    @Test
    void generateCode(){

    }

    @Test
    void compareCode(){

    }
}