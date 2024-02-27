import java.util.*;

public class Game {

    private Player currentPlayer;
    private char[] Guess;
    private SecretCode codeGame;

    public Game(Player p, String codeType){
        Guess = new char[]{'\0', '\0', '\0', '\0'};
    }

    public Game(Player p){

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
    Main game loop for one "round" each time function is called
    @param is the game type a number game? (True -> number game, False -> letter game)
    */
    void playGame(boolean isNumberGame) {       // Main game loop (Noa)
        // local variables
        boolean gameOver, validInputGiven, givenUp;
        Scanner inputScanner;
        String userInput;

        // create an instance of chosen secret code
        if (isNumberGame) {codeGame = new NumbersCode();} else {codeGame = new LettersCode();}

        gameOver = false;
        while (!gameOver) {                                     // begin game loop

            // PRINT USER COMMAND INSTRUCTIONS??

            validInputGiven = false;
            givenUp = false;

            while (!validInputGiven) {                          // begin loop for making guess
                inputScanner = new Scanner(System.in);
                System.out.println("\n>>> ");
                userInput = inputScanner.nextLine();

                if (userInput.charAt(0) != '/') {               // input is not a user command
                    // ALTER GUESS
                    System.out.println("ALTERED GUESS");
                } else {                                        // input is a user command
                    switch (userInput.toLowerCase()) {          // cases for all user commands

                        case "/guess":
                            validInputGiven = submitGuess();

                        case "/hint":
                            System.out.println(getHint());

                        case "/giveup":
                            if (giveUpConfirmation()) {
                                System.out.println("\nSolution: " + showSolution(codeGame));
                                givenUp = true;
                                validInputGiven = true;
                            }
                    }
                }
            }

            // valid input has been given
            if (givenUp) {
                gameOver = true;
            } else {
                // CHECK IF GUESS IS CORRECT
                // IF CORRECT END GAME, ELSE CONTINUE GAME
            }
        }
    }

    /*
    Helper function for confirming the user wishes to give up
     */
    private boolean giveUpConfirmation() {
        while (true) {
            Scanner giveUpScan = new Scanner(System.in);
            System.out.println("\nAre you sure you want to give up? (y or n) >>>");

            String userConfirm = giveUpScan.nextLine();

            if (userConfirm.compareTo("y") == 0) {
                return true;
            } else if (userConfirm.compareTo("n") == 0) {
                return false;
            } else {
                System.out.println("\nError: Please choose y or n\n");
            }
        }
    }

    void requestCode(){
        
    }

    void generateNumber() {
        Random rand = new Random();
        int min = 1000;
        int max = 9999;

        int numCode = rand.nextInt(max - min + 1) + min;

        while(hasConsecutiveDigit(numCode)){
            numCode = rand.nextInt(max - min + 1) + min;
        }
    }
    private boolean hasConsecutiveDigit(int numCode) {
        String numStr = Integer.toString(numCode);

        for (int i = 0; i < 3; i++) {
            if (numStr.charAt(i) == numCode.charAt(i + 1)) {
                return true;
            }
        }
        return false;
    }


    /*
    Checks if given input position and value are valid for current code-type. If so, inserts it into the
    guess array at corresponding position
    @param position the position in the Guess array the value is being entered
    @param val the value being entered into the Guess array
    */

    // NOA :- would it be better if this function didn't print anything, and only returned a certain value if the entered guess
    //       was valid or not, so then the error messages are handled in the play game method, and we only need to adapt that
    //       when we move on to a GUI? Same for validateInput method. Or are the print statements just here to test for now?
    void enterGuess(int position, char val) {

        // Checks if given position is out of range
        if (position < 0 || position > 4) {
            System.out.println("Invalid position!");
            return;
        }

        // Checks the current game type, then checks if the appropriate characters have been used
        if (codeGame.getClass() == LettersCode.class) {
            if (val >= 'a' && val <= 'z') {
                Guess[position] = val;
            } else {
                System.out.println("Must be a letter!");
            }
        } else if (codeGame.getClass() == NumbersCode.class) {
            if (val >= '0' && val <= '9') {
                Guess[position] = val;
            } else {
                System.out.println("Must be a number!");
            }
        } else{
            System.out.println("Well shit... Don't know the game type");
        }

        return;
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
            for (int j = i; j<4; j++) {
                if (Guess[i] == Guess[j]) {
                    System.out.println("Duplicate value present at positions " + i + " & " + j);
                    return false;
                }
            }
        }

        return true;
    }
}
