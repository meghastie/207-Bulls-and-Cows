package Testing;

import org.junit.jupiter.api.*;
import BullsAndCows.Game;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    @BeforeEach
    void setUp() {
        Game game = new Game();
    }

    @Test
    void hasDuplicateCharacter(){
        String[] stimulus = {"1111","5785","3663"};
        for(String s: stimulus) {
            assertTrue(Game.hasDuplicateCharacter(s));
        }
        stimulus = new String[]{"1234", "9876", "4321"};
        for(String s: stimulus) {
            assertFalse(Game.hasDuplicateCharacter(s));
        }
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