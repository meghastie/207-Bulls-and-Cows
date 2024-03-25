package Testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import BullsAndCows.LettersCode;

import java.util.Arrays;

class LettersCodeTest {
    LettersCode code;
    LettersCode setCode;
    @BeforeEach
    void getCodes(){
        code = new LettersCode(8);
        setCode = new LettersCode(new char[] {'b','l','i','m','y'});
    }

    @Test
    void generateCode() {
        //see if code is coming from the list
        String word = new String(code.generateCode(8));
        assertTrue(code.getWord().contains(word));

        //see if the code is random
        String newWord = new String(code.generateCode(8));
        assertNotEquals(newWord, word);
    }

    @Test
    void compareCode(){
        //valid guess
        char[] word = code.getCode();
        assertArrayEquals(code.compareCode(word), new int[]{4, 0});
        //invalid guess right format
        assertArrayEquals(setCode.compareCode("blmi".toCharArray()), new int[]{2, 2});
    }

    @Test
    void getHint(){
        String word = Arrays.toString(code.getCode());
        var hint = code.getHint();
        assertEquals(word.indexOf(code.getLastHintPos()), hint.indexOf(hint.length() -3));
        assertTrue(code.getHintsUsed()[code.getLastHintPos()]);
    }
    @Test
    void getHintMultiple(){
        String word = Arrays.toString(code.getCode());
        var beforeHint = code.getHint();
        var beforeHintPos = code.getLastHintPos();
        assertEquals(word.indexOf(beforeHintPos), beforeHint.indexOf(beforeHint.length() -3));
        assertTrue(code.getHintsUsed()[beforeHintPos]);

        var afterHint = code.getHint();
        var afterHintPos = code.getLastHintPos();
        assertNotEquals(beforeHintPos, afterHintPos);
        assertNotEquals(beforeHint, afterHint);
        assertEquals(word.indexOf(afterHintPos), afterHint.indexOf(afterHint.length() -3));
        assertTrue(code.getHintsUsed()[afterHintPos]);


    }
}