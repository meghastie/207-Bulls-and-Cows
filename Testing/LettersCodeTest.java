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
        assertEquals(new int[] {4,0}, code.compareCode(word));
        //invalid guess right format
        assertEquals(new int[] {2,2}, setCode.compareCode(new char[] {'b','l','m','i'}));
        //invalid guess wrong format

    }
}