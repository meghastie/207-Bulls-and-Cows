package BullsAndCows;

import java.util.*;

public class Game {



    private Player currentPlayer;
    private char[] Guess;
    private SecretCode codeGame;

    public Game(Player p, String codeType){
        this.currentPlayer = p;
        resetGuess();
    }

    public Game(Player p){
        this.currentPlayer = p;
    }

    public Game(){
        resetGuess();
        //default to number game
        //playGame(true);
    }

    /*
   Get a hint from the secret code and return
   */
    String getHint(){
        return "";
    }

    void loadPlayer(){

    }

    /*
    Resets the current guess for making a new guess
     */
    void resetGuess() {
        Guess = new char[]{'\0', '\0', '\0', '\0'};
    }

    /*
    Main game loop for one "round" each time function is called
    @param is the game type a number game? (True -> number game, False -> letter game)
    */
    void playGame(boolean isNumberGame) {                       // Main game loop (Noa)
        // introduce game
        System.out.println("\n\nWelcome to Bulls and Cows. Please alter and submit your guess, or type /help for instructions of how to play.\n");

        // local variables
        boolean gameOver, finishInputGuess, givenUp;
        Scanner inputScanner;
        String userInput;

        String instructions =   "\nHOW TO PLAY\n\nYou are tasked with deciphering a secret code, consisting of either 4 different numbers or " +
                                "letters.\nEach guess you make, you will be shown how many 'Bulls' and 'Cows' you managed to get, meaning how " +
                                "many numbers / letters you got correct & in the right position, and how many you just got correct, " +
                                "respectively.\nCOMMANDS\n\nYou begin with an empty guess. To set one of the characters in your guess, type the " +
                                "letter / number you wish to guess, followed by the position to put that guess (between 1 and 4), e.g. a4. To " +
                                "change more than one position at a time, simply enter a comma followed by your next guess, e.g. a4,g1,h3. Note " +
                                "there are no spaces\nOther commands are as follows:\n\n\t/hint -\tIf you are stuck, receive a hint for your " +
                                "guess\n\t/giveup -\t If you are really stuck, you can give up and see the solution\n\t/guess -\tSubmit your " +
                                "completed guess, all positions of your guess must be filled.";

        // create an instance of chosen secret code
        if (isNumberGame) {codeGame = new NumbersCode();} else {codeGame = new LettersCode();}

        gameOver = false;
        while (!gameOver) {                                     // begin game loop

            // PRINT USER COMMAND INSTRUCTIONS??

            finishInputGuess = false;
            givenUp = false;

            while (!finishInputGuess) {                          // begin loop for making guess
                inputScanner = new Scanner(System.in);
                System.out.println("\n>>> ");
                userInput = inputScanner.nextLine();

                if (userInput.charAt(0) != '/') {               // input is not a user command
                    if (inputGuessChange(userInput)) {
                        System.out.println("\nGUESS ALTERED CORRECTLY");
                    } else {
                        System.out.println("\nInput has incorrect format for changing guess. Try /help to read the instructions, and try again.\n");
                    }
                } else {                                        // input is a user command
                    switch (userInput.toLowerCase()) {          // cases for all user commands

                        case "/help":
                            System.out.println(instructions);

                        case "/guess":
                            finishInputGuess = true;

                        case "/hint":
                            System.out.println(getHint());

                        case "/giveup":
                            if (giveUpConfirmation()) {
                                // SHOW SOLUTION -> System.out.println("\nSolution: " + showSolution(codeGame)); (?)
                                givenUp = true;
                                finishInputGuess = true;
                            }
                    }
                }
            }

            // valid input has been given
            gameOver = givenUp;

            if (!givenUp) {
                submitGuess();
            }
        }

        System.out.println("GAME FINISHED");
    }

    /*
    Check if the user input is valid for changing the guess, should be of form " <letter><number> " followed by
        a comma if more than one guess at a time
    @return True if valid change to guess, and the guess is changed, False if not valid
     */
    boolean inputGuessChange(String userInput) {
        if (userInput.contains(" ") || userInput.contains("-") || userInput.length() < 2 || (userInput.length() > 2 && userInput.length() % 3 != 0)) {
            return false;
        } else if (userInput.length() == 2) {
            alterGuess(userInput.charAt(0), (int) userInput.charAt(1));
            return true;
        }

        for (int i = 0; i < userInput.length() - 3; i += 3) {
            char inputChangeChar = userInput.toLowerCase().charAt(i);             // ARE LETTERS LOWER CASE OR UPPER CASE IN SECRET CODE??
            int inputChangePos = (int) userInput.charAt(i + 1) - 1;

            if (inputChangePos >= 0 && inputChangePos <= 3) {                     // if valid position
                // vvv valid guess characters according to game type vvv
                if (codeGame.getClass() == LettersCode.class && (int) inputChangeChar >= 97 && (int) inputChangeChar <= 122) {
                    alterGuess(inputChangeChar, inputChangePos);
                } else if ((codeGame.getClass() == NumbersCode.class && (int) inputChangeChar <= 9)) {
                    alterGuess(inputChangeChar, inputChangePos);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /*
    Alters the current guess according to the values given
    Values should always be valid BEFORE calling this function
    */
    void alterGuess(char guessChar, int guessPosition) {
        Guess[guessPosition] = guessChar;
    }

    /*
    Helper function for confirming the user wishes to give up
     */
    private boolean giveUpConfirmation() {
        while (true) {
            Scanner giveUpScan = new Scanner(System.in);
            System.out.println("\nAre you sure you want to give up? (y / n) >>>");

            String userConfirm = giveUpScan.nextLine();

            if (userConfirm.compareTo("y") == 0) {
                return true;
            } else if (userConfirm.compareTo("n") == 0) {
                return false;
            } else {
                System.out.println("\nError: Please choose y / n\n");
            }
        }
    }

    void requestCode(){
        
    }

    /*
    Checks if given input position and value are valid for current code-type. If so, inserts it into the
    guess array at corresponding position
    @param position The position in the Guess array the value is being entered
    @param val      The value being entered into the Guess array

    @throws ArrayIndexOutOfBoundsException If the given position does not exist in the Guess array
    @throws IllegalArgumentException       If the given value is not acceptable for the given code type
    @throws RuntimeException               If codeGame exists as neither types of subclass
    */

    // NOA :- would it be better if this function didn't print anything, and only returned a certain value if the entered guess
    //       was valid or not, so then the error messages are handled in the play game method, and we only need to adapt that
    //       when we move on to a GUI? Same for validateInput method. Or are the print statements just here to test for now?
    void enterGuess(int position, char val) {

        // Checks if given position is out of range
        if (position < 0 || position > 4) {
            throw new ArrayIndexOutOfBoundsException();
        }

        // Checks the current game type, then checks if the appropriate characters have been used
        if (codeGame.getClass() == LettersCode.class) {
            val = Character.toLowerCase(val);
            if (val >= 'a' && val <= 'z') {
                Guess[position] = val;
            } else {
                throw new IllegalArgumentException("Value must be a letter");
            }
        } else if (codeGame.getClass() == NumbersCode.class) {
            if (val >= '0' && val <= '9') {
                Guess[position] = val;
            } else {
                throw new IllegalArgumentException("Value must be a number");
            }
        } else{
            throw new RuntimeException("Neither word nor number base code identified");
        }
    }

    /*
    Validates input and passes guess off to be checked
    @return true if guess matches code
    */

    // NOA :- should submitGuess return whether the guess was correct in some other way? As it is now it will return
    //        True if the guess was correct, and False if anything else. Could we have it so it returns 0 if the guess
    //        was correct, 1 if it was incorrect, and -1 if it wasn't a valid input (just as an example)?
    private boolean submitGuess(){
        if(validateInput()){
            int[] bullsCows = codeGame.compareCode(Guess);
            System.out.println("Bulls: " + bullsCows[0]);
            System.out.println("Cows: " + bullsCows[1]);

            currentPlayer.updateBulls(bullsCows[0]);
            currentPlayer.updateCows(bullsCows[1]);

            if (bullsCows[0]==4) {
                return true;
            }
        }
        return false;
    }

    /*
    Sets the value at one of the given places to null
    @param position number of place to null
    */
    private void undoGuess(int position){
        Guess[position] = '\0';
    }

    void saveGuess(){

    }

    void loadGame(){

    }

    /*
    Gets solution from secret code class and returns

    @param secretCode instance
    @return solution as String
    */
    String showSolution(SecretCode secretCode){
        return "";
    }

    /*
    Checks the Guess for empty positions and duplicate values

    @return true if Guess contains none of the above
    */
    private boolean validateInput(){

        for(int i = 0; i<4; i++){

            // Checks for missing value
            if(Guess[i] == '\0'){
                System.out.println("Missing value at position " + i);
                return false;
            }

            // Checks for duplicate values
            //can use hasduplicatemethod for this
            for (int j = i + 1; j<4; j++) {
                if (Guess[i] == Guess[j]) {
                    System.out.println("Duplicate value present at positions " + i + " & " + j);
                    return false;
                }
            }
        }

        return true;
    }
}