package Testing;

import org.junit.jupiter.api.*;
import BullsAndCows.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest{
    Game game;

    @Test
    void requestCodeLetter(){
        String stimulus = "letter\n/giveup\ny";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new Player());
            game.playGame();
            assertEquals("BullsAndCows.LettersCode",game.getCodeGame().getClass().getName());
        }
        finally {
            System.setIn(old);
        }
    }
    @Test
    void requestCodeLetterWithoutFile(){
        try {
            System.out.println("Calling static method with invalid path");
            LettersCode.populateWordList("random");
        }
        catch (FileNotFoundException e) {
            assertEquals(FileNotFoundException.class,e.getClass());
        }
    }
    @Test
    void requestCodeNumber(){
        String stimulus = "number\n/giveup\ny";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new Player());
            assertEquals("BullsAndCows.NumbersCode",game.getCodeGame().getClass().getName());
            System.out.println("Code Generated - " + Arrays.toString(game.getCodeGame().getCode()));
        }
        finally {
            System.setIn(old);
        }
    }
    @Test
    void playerEntersGuess(){
        String stimulus = "number\n11,22,33,44\n/guess\n/giveup\ny";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new Player());
            game.playGame();
            assertArrayEquals(new char[]{'1', '2', '3', '4'}, game.getGuess());
        }
        finally {
            System.setIn(old);
        }
    }

    @Test
    void playerEntersSuccessfulGuess(){
        String stimulus = "number\n91,82,73,64\n/guess\n/giveup\ny";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            SecretCode code = new NumbersCode(new char[]{'9','8','7','6'});
            game = new Game(new Player(),code);
            game.playGame();
            assertArrayEquals(new int[]{4,0},game.getCodeGame().compareCode(code.getCode()));
        }
        finally {
            System.setIn(old);
        }
    }

    @Test
    void playerEntersInvalidLengthInput(){
        String stimulus = "number\n55,66,77,88\n/giveup\ny";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new Player());
        }
        finally {
            System.setIn(old);
        }
        System.out.println("Simulated Input for this test\n" + stimulus);

    }
    @Test
    void playerEntersInvalidInputLettersGame(){
        String stimulus = "letter\n51,62,73,84\n/giveup\ny";
        genericTestingBody(stimulus);
    }
    @Test
    void playerEntersInvalidInputNumbersGame(){
        String stimulus = "number\na1,b2,c3,d4\n/giveup\ny";
        genericTestingBody(stimulus);
    }
    @Test
    void undoSingleCharacter(){
        String stimulus = "number\n51,62,73,84\n/undo\n2\n/giveup\ny";
        genericTestingBody(stimulus);
    }
    @Test
    void undoWithoutGuessAttempt(){
        String stimulus = "number\n/undo\n2\n/giveup\ny";
        genericTestingBody(stimulus);
    }
    @Test
    void undoInInvalidPosition(){
        String stimulus = "number\n/undo\n42\n\n/giveup\ny";
        genericTestingBody(stimulus);
    }

    private void genericTestingBody(String stim){
        InputStream testInput = new ByteArrayInputStream(stim.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new Player());
        }
        finally {
            System.setIn(old);
        }
        System.out.println("Simulated Input for this test\n" + stim);
    }
}