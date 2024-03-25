package Testing;

import org.junit.jupiter.api.*;
import BullsAndCows.*;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest{
    Game game;

    @BeforeEach
    void setUp(){
        game = new Game(4);
        game.getPlayerList().clearSaves();
    }

    @AfterEach
    void resetStates(){
        game.getPlayerList().clearSaves();
    }

    //<editor-fold desc="Sprint 1 Tests">
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

    private void genericTestingBody(String stim){
        InputStream testInput = new ByteArrayInputStream(stim.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(4);
        }
        finally {
            System.setIn(old);
        }
        System.out.println("Simulated Input for this test\n" + stim);
    }


    //<editor-fold desc="Main1 Tests">

    @Test
    void requestCodeLetter(){
        String stimulus = "game\nletter\n/quit";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new Player(), 4);
            //game.playGame();
            assertEquals("BullsAndCows.LettersCode",game.getCodeGame().getClass().getName());
        }
        finally {
            System.setIn(old);
        }
    }
    @Test
    void requestCodeNumber(){
        String stimulus = "game\nnumber\n/quit";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new Player(), 4);
            //game.playGame();
            assertEquals("BullsAndCows.NumbersCode",game.getCodeGame().getClass().getName());
            System.out.println("Code Generated - " + Arrays.toString(game.getCodeGame().getCode()));
        }
        finally {
            System.setIn(old);
        }
    }
    @Test
    void playerEntersGuess(){
        String stimulus = "game\nnumber\n11,22,33,44\n/guess\n/quit";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new Player(), 4);
            //game.playGame();
            assertArrayEquals(new char[]{'1', '2', '3', '4'}, game.getGuess());
        }
        finally {
            System.setIn(old);
        }
    }

    @Test
    void playerEntersSuccessfulGuess(){
        String stimulus = "game\nnumber\n91,82,73,64\n/guess\n/quit";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            SecretCode code = new NumbersCode(new char[]{'9','8','7','6'});
            game = new Game(new Player(),code, 4);
            //game.playGame();
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
            game = new Game(new Player(),4);
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



    //</editor-fold>

    //<editor-fold desc="Main2 Tests">

    @Test
    void requestCodeLetter2(){
        String stimulus = "3\n1\n2\n/giveup\ny\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(4);
            game.playGame2();
            assertEquals("BullsAndCows.LettersCode",game.getCodeGame().getClass().getName());
        }
        finally {
            System.setIn(old);
        }
    }

    @Test
    void requestCodeNumber2(){
        String stimulus = "3\n1\n1\n/giveup\ny\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(4);
            game.playGame2();
            assertEquals("BullsAndCows.NumbersCode",game.getCodeGame().getClass().getName());
            System.out.println("Code Generated - " + Arrays.toString(game.getCodeGame().getCode()));
        }
        finally {
            System.setIn(old);
        }
        System.out.println("Simulated Input for this test\n" + stimulus);
    }
    @Test
    void playerEntersGuess2(){
        String stimulus = "11,22,33,44\n/submit\n/giveup\ny";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(new NumbersCode(4), 4);
            game.testGuessOptions();
            assertArrayEquals(new char[]{'1', '2', '3', '4'}, game.getGuess());
        }
        finally {
            System.setIn(old);
        }
    }

    @Test
    void playerEntersSuccessfulGuess2(){
        String stimulus = "91,82,73,64\n/submit\n";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            SecretCode code = new NumbersCode(new char[]{'9','8','7','6'});
            game = new Game(code,4);
            game.testGuessOptions();
            assertArrayEquals(new int[]{4,0},game.getCodeGame().compareCode(code.getCode()));
        }
        finally {
            System.setIn(old);
        }
    }

    @Test
    void playerEntersInvalidLengthInput2(){
        String stimulus = "3\n1\n1\n55,66,77,88\n/giveup\ny";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            game = new Game(4);
        }
        finally {
            System.setIn(old);
        }
        System.out.println("Simulated Input for this test\n" + stimulus);

    }
    @Test
    void playerEntersInvalidInputLettersGame2(){
        String stimulus = "3\n1\n2\n51,62,73,84\n/giveup\ny";
        genericTestingBody(stimulus);
    }
    @Test
    void playerEntersInvalidInputNumbersGame2(){
        String stimulus = "number\na1,b2,c3,d4\n/giveup\ny";
        genericTestingBody(stimulus);
    }
    @Test
    void undoSingleCharacter2(){
        String stimulus = "3\n1\n1\n51,62,73,84\n/undo\n2\n/giveup\ny";
        genericTestingBody(stimulus);
    }
    @Test
    void undoWithoutGuessAttempt2(){
        String stimulus = "3\n1\n1\n/undo\n2\n/giveup\ny";
        genericTestingBody(stimulus);
    }
    @Test
    void undoInInvalidPosition2(){
        String stimulus = "3\n1\n1\n/undo\n42\n\n/giveup\ny";
        genericTestingBody(stimulus);
    }

    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="Sprint 2 Tests">

    void checkInFile(String details){
        File saveFile = new File("./BullsAndCows/players.txt");
        try {
            Scanner fileRead = new Scanner(saveFile);
            assertEquals(details, fileRead.nextLine());
        }
        catch(FileNotFoundException e){
            fail();
        }
    }

    @Test
    void newAccountTest(){
        String stimulus = "2\nBarry\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        game = new Game(4);
        game.playGame2();

        File saveFile = new File("./BullsAndCows/players.txt");
        try {
            Scanner fileRead = new Scanner(saveFile);
            assertEquals("Barry,0,0,0,0,0", fileRead.nextLine());
            fileRead.close();
        }
        catch(FileNotFoundException e){
            fail();
        }
    }

    @Test
    void saveGameTest(){
        String stimulus = "2\nBarry\n1\n1\n/save\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        game = new Game(4);
        game.playGame2();

        String code = Arrays.toString(game.getCodeGame().getCode());

        File saveFile = new File("./BullsAndCows/playerSaves/Barry.txt");
        try {
            Scanner fileRead = new Scanner(saveFile);
            String codeDetails = fileRead.nextLine();
            assertEquals(code, codeDetails.split(";")[2]);
        }
        catch(FileNotFoundException e){
            fail();
        }
    }

    @Test
    void overwriteSaveGame(){
        String stimulus = "2\nBarry\n1\n1\n/save\ny\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);
        File saveFile = new File("./BullsAndCows/playerSaves/Barry.txt");
        String before = "";

        game = new Game(4);
        game.playGame2();

        try {
            Scanner fileRead = new Scanner(saveFile);
            String codeDetails = fileRead.nextLine();
            before = codeDetails.split(";")[2];
        }
        catch(FileNotFoundException ignored) {
        }

        stimulus = "1\nBarry\n1\n1\n/save\ny\n6";
        testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        game = new Game(4);
        game.playGame2();

        String after = Arrays.toString(game.getCodeGame().getCode());
        assertNotEquals(before,after);
    }

    @Test
    void loadExistingGame(){
        String stimulus = "/save\ny";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        Player Barry = new Player("Barry",0,0,0,0,0);

        game = new Game(Barry, new NumbersCode(new char[]{'1','1','1','1'}), 4);
        game.testGuessOptions();
        game.getPlayerList().saveUpdatedPlayers();

        stimulus = "1\nBarry\n1\n3\n1\n/giveup\ny\n6";
        testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        game = new Game(4);
        game.playGame2();

        assertArrayEquals(new char[]{'1','1','1','1'}, game.getCodeGame().getCode());
    }

    @Test
    void loadNonexistingGame(){
        Player Barry = new Player("Barry",0,0,0,0,0);
        Players playerList = new Players();
        playerList.addPlayer(Barry);
        playerList.saveUpdatedPlayers();

        String stimulus = "1\nBarry\n1\n3\nback\nback\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        game = new Game(4);
        game.playGame2();

        assertNull(game.getCodeGame());
    }

    @Test
    void trackDecipheredTest(){
        String stimulus = "91,82,73,64\n/submit\n";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try {
            System.setIn(testInput);
            SecretCode code = new NumbersCode(new char[]{'9','8','7','6'});
            Player Barry = new Player("Barry",0,0,0,0,0);
            game = new Game(Barry, code, 4);
            game.testGuessOptions();
            assertArrayEquals(new int[]{4,0},game.getCodeGame().compareCode(code.getCode()));
            Barry.incrementCodesAttempted();
            game.getPlayerList().saveUpdatedPlayers();
        }
        finally {
            System.setIn(old);
        }

        checkInFile("Barry,4,0,4,1,1");
    }

    @Test
    void dontIncrementAttemptWhenLoading(){
        final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd;HH:mm:ss");
        final String playerSavePath = "./BullsAndCows/playerSaves/" + "Barry"+ ".txt";

        try {
            FileOutputStream fileOut = new FileOutputStream(playerSavePath, false);
            fileOut.write(LocalDateTime.now().format(CUSTOM_FORMATTER).getBytes());
            fileOut.write((";" + "abcd" + "\n").getBytes());
            fileOut.close();

        }
        catch (IOException e) {
            System.err.println("\nFatal IO error; this shouldn't happen");
            System.exit(0);
        }
        String stimulus = "2\nBarry\n1\n3\n1\n/giveup\ny\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        InputStream old = System.in;
        try{
            System.setIn(testInput);
            game =new Game(4);
            game.playGame2();
        }
        finally{
            System.setIn(old);
        }
        checkInFile("Barry,0,0,0,0,0");
    }

    @Test
    void trackAttemptedTest(){
        Players playerList = new Players();
        playerList.addPlayer(new Player("Barry",0,0,0,0,0));
        playerList.saveUpdatedPlayers();

        String stimulus = "1\nBarry\n1\n1\n/save\ny\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        game = new Game(4);
        game.playGame2();

        checkInFile("Barry,0,0,0,1,0");


        stimulus = "1\nBarry\n1\n3\n1\n/giveup\ny\n6";
        testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);
        game = new Game(4);
        game.playGame2();

        checkInFile("Barry,0,0,0,1,0");
    }

    @Test
    void invalidPlayerLoad(){
        String stimulus = "1\nHarold\nback\n4";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        game = new Game(4);
        game.playGame2();

        assertNull(game.getCurrentPlayer());
    }

    @Test
    void detailsWithNoGuesses(){
        assertFalse(game.print_player_details_testing(new Player("Barry",0,0,0,0,0)));
    }

    @Test
    void detailsWithGuesses(){
        assertTrue(game.print_player_details_testing(new Player("Barry",0,0,1,0,0)));
    }

    //</editor-fold>

    //<editor-fold desc="Sprint 3">
    @Test
    void giveupTest(){
        String stimulus = "3\n1\n1\n/giveup\ny\n6";
        InputStream testInput = new ByteArrayInputStream(stimulus.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);

        game = new Game(8);
        game.playGame2();

        assertEquals(8, game.getCodeGame().getCode().length);
    }

    //top 10 scores testing within players class
    //</editor-fold>
}