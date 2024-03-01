package Testing;

import org.junit.jupiter.api.*;
import BullsAndCows.*;

class GameTest extends Game{
    Game game;
    @BeforeEach
    void setUp() {
        Game game = new Game();
    }

    @Test
    void hasDuplicateCharacter(){

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