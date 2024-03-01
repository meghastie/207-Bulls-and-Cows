package BullsAndCows;

import java.util.*;

public class Game {

    private Player currentPlayer;
    private char[] Guess;
    private SecretCode codeGame;

    public Game(Player p, String codeType){
        resetGuess();
    }

    public Game(Player p){

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
    Formats the array as a string to output
    @return formatted string of Guess array
     */
    String showGuess() {
        return "[ " + Guess[0] + " ] [ " + Guess[1] + " ] [ " + Guess[2] + " ] [ " + Guess[3] + " ]";
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

        String[] guessErrors = {
                "GUESS ALTERED",
                "\nGuess contains an invalid position, all other guess changes processed.\n",
                "\nGuess contains an invalid guess value, all other guess changes processed.\n",
                "\nGuess contains more than one error, all other guess changes processed.\n",
                "\nGuess contains incorrect formatting, guess change processing stopped.\n"
        };

        String instructions =   "\nHOW TO PLAY\n\nYou are tasked with deciphering a secret code, consisting of either 4 different numbers or " +
                                "letters.\nEach guess you make, you will be shown how many 'Bulls' and 'Cows' you managed to get, meaning how " +
                                "many numbers / letters you got correct & in the right position, and how many you just got correct, " +
                                "respectively.\nCOMMANDS\n\nYou begin with an empty guess. To set one of the characters in your guess, type the " +
                                "letter / number you wish to guess, followed by the position to put that guess (between 1 and 4), e.g. a4. To " +
                                "change more than one position at a time, simply enter a comma followed by your next guess, e.g. a4,g1,h3. Note " +
                                "there are no spaces\nOther commands are as follows:\n\n\t/hint -\tIf you are stuck, receive a hint for your " +
                                "guess\n\t/giveup -\t If you are really stuck, you can give up and see the solution\n\t/guess -\tSubmit your " +
                                "completed guess, all positions of your guess must be filled.\n\t/undo -\t If you want to remove a " +
                                "change you've made to your guess.";

        // create an instance of chosen secret code
        if (isNumberGame) {codeGame = new NumbersCode();} else {codeGame = new LettersCode();}

        gameOver = false;
        while (!gameOver) {                                     // begin game loop

            finishInputGuess = false;
            givenUp = false;

            while (!finishInputGuess) {                          // begin loop for making guess
                inputScanner = new Scanner(System.in);
                System.out.println("Current Guess: " + showGuess() + "\n>>> ");
                userInput = inputScanner.nextLine();            // receive input

                if (userInput.charAt(0) != '/') {               // input is not a user command
                    int guessReturn = inputGuessChange(userInput);
                    if (guessReturn == -1) {
                        System.out.println("\nFATAL ERROR");    // error with classes, or other error
                    } else {
                        System.out.println(guessErrors[guessReturn]);
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

                        case "/undo":
                            if (undoConfirmation()) {
                                System.out.println("\nUNDO CONFIRMED");
                            } else {
                                System.out.println("\nUNDO CANCELLED");
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
    Check if the user input is valid for changing the guess, any invalid inputs are skipped
    @return -1 if classes contain errors, 0 if input is a valid change, 1 if has invalid position, 2 if has invalid val, 3 if more than one error total, 4 if invalid format
     */
    int inputGuessChange(String userInput) {
        if (userInput.contains(" ") || userInput.contains("-") || userInput.length() < 2 || (userInput.length() > 2 && userInput.length() % 3 != 0)) {
            return 4;
        }

        int returnValue = 0;

        for (int i = 0; i < userInput.length() - 3; i += 3) {
            char inputChangeChar = userInput.charAt(i);
            int inputChangePos = (int) userInput.charAt(i + 1) - 1;
            int guessValidation = enterGuess(inputChangePos, inputChangeChar);

            if (guessValidation != 0) {
                if (returnValue == 0) {
                    returnValue = guessValidation;
                } else {
                    returnValue = 3;
                }
            }
        }

        return returnValue;
    }

    /*
    Helper function for confirming the user wishes to give up
     */
    boolean giveUpConfirmation() {
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

    /*
    Helper function for confirming the user wishes to give up
     */
    boolean undoConfirmation() {
        while (true) {
            Scanner undoScan = new Scanner(System.in);
            System.out.println("\nWhat position of your guess do you wish to remove? >>>");

            String undoPos = undoScan.nextLine();
            if (undoPos.compareTo("") == 0) {
                return false;
            }

            if (Integer.parseInt(undoPos) >= 1 && Integer.parseInt(undoPos) <= 4) {
                undoGuess(Integer.parseInt(undoPos) - 1);
                return true;
            } else {
                System.out.println("\nError: Please choose a position between 1 and 4, or press ENTER to exit\n");
            }
        }
    }

    void requestCode(){
        
    }

    /*
    Checks if given input position and value are valid for current code-type. If so, inserts it into the
    guess array at corresponding position
    @return 0 if valid change, 1 if invalid position, 2 if invalid val
    @param position the position in the Guess array the value is being entered
    @param val the value being entered into the Guess array
    */
    int enterGuess(int position, char val) {
        // Checks if given position is out of range
        if (position < 0 || position > 4) {
            return 1;
        }

        // Checks the current game type, then checks if the appropriate characters have been used
        if (codeGame.getClass() == LettersCode.class) {
            val = Character.toLowerCase(val);
            if (val >= 'a' && val <= 'z') {
                Guess[position] = val;
                return 0;
            }
            return 2;

        } else if (codeGame.getClass() == NumbersCode.class) {
            if (val >= '0' && val <= '9') {
                Guess[position] = val;
                return 0;
            }
            return 2;
        }

        return -1; // big error, cant find the game type
    }

    /*
    Validates input and passes guess off to be checked
    @return true if guess matches code
    */
    private boolean submitGuess(){
        if(validateInput()){
            // check if guess matches code
            // Get number of cows and bulls
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