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
        code = new LettersCode("./BullsAndCows/words.txt");
        setCode = new LettersCode(new char[] {'b','l','i','m'});
    }

    @Test
    void generateCode() {
        //see if code is coming from the list
        String word = new String(code.generateCode());
        assertTrue(code.getWord().contains(word));

        //see if the code is random
        String newWord = new String(code.generateCode());
        assertNotEquals(newWord, word);
    }

    @Test
    void compareCode(){
        //valid guess
        char[] word = code.generateCode();
        assertArrayEquals(code.compareCode(word), new int[]{4, 0});
        //invalid guess right format
        assertArrayEquals(setCode.compareCode("blmi".toCharArray()), new int[]{2, 2});
        //invalid guess wrong format

    }
}