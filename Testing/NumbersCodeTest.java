package Testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import BullsAndCows.*;

import java.util.Arrays;

class NumbersCodeTest {

    NumbersCode code;
    NumbersCode setCode;

    @BeforeEach
    void getCodes(){
        code = new NumbersCode();
        setCode = new NumbersCode(new char[]{'4','5','6','7'});
    }

    @Test
    void hasDuplicateCharacter() {
        String[] stimulus = {"1111","5785","3663"};
        for(String s: stimulus) {
            assertTrue(NumbersCode.hasDuplicateCharacter(s.toCharArray()));
        }
        stimulus = new String[]{"1234", "9876", "4321"};
        for(String s: stimulus) {
            assertFalse(NumbersCode.hasDuplicateCharacter(s.toCharArray()));
        }
    }

    @Test
    void generateCode(){
        char[] word = code.generateCode();
        assertFalse(NumbersCode.hasDuplicateCharacter(word));
        //check if truly random
        char[] newWord = code.generateCode();
        code.generateCode();
        assertFalse(Arrays.equals(new int[] {4,0},code.compareCode(newWord)));
    }

    @Test
    void compareCode(){
        //valid guess
        char[] word = code.getCode();
        assertArrayEquals(new int[] {4,0}, code.compareCode(word));
        //invalid guess right format
        assertArrayEquals(new int[] {2,2}, setCode.compareCode(new char[] {'4','5','7','6'}));

    }
}