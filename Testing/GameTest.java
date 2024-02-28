package Testing;

import org.junit.jupiter.api.*;
import BullsAndCows.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @BeforeEach
    void setUp() {
        Game game = new Game();
    }

    @Test
    void hasConsecutiveDigitTest(){
        String stimulus = "1111";
        //assertEquals(stimulus, hasConsecutiveDigitTest());
    }
    @Test
    void enterGuess(){

    }

    @Test
    void validateInput(){

    }



    @AfterEach
    void tearDown() {
    }
}