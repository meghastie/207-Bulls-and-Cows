import java.util.*;

public class Game {

    // New type with limited selection. To change the value of a derived object, use '[object].[value]'
    private enum GameType{
        TEXT, NUMBER
    }

    private Player currentPlayer;
    private char[] Guess;
    private GameType gameType;

    public Game(Player p, String codeType){
        Guess = new char[]{'\0', '\0', '\0', '\0'};
    }

    public Game(Player p){

    }

    void getHint(){

    }

    void loadPlayer(){

    }

    void playGame() {       // Main game loop (Noa)
        /* SUDO CODE FOR GAME LOOP

        CREATE INSTANCE OF secretCode
        BEGIN GAME LOOP
            SET validInputGiven AS FALSE
            SET givenUp AS FALSE

            WHILE validInputGiven IS FALSE
                GET userInput FROM TERMINAL
                IF FIRST CHAR IN userInput IS "/"
                  FOR character IN userInput (might change so user inputs one character and one position each time, or they can do both)
                      enterGuess(characterPosition, character)
                  END FOR
                ELSE
                    SWITCH STATEMENT (?) FOR userInput
                          CASE "/guess"
                                SET validInputGiven TO submitGuess()
                                PRINT CONFIRMATION OR ERROR
                          CASE "/hint"
                                PRINT getHint()
                          CASE "/giveup"
                                GET "are you sure" FROM KEYBOARD
                                IF SURE PRINT showSolution
                                SET givenUp TO TRUE
                                SET validInputGiven TO TRUE
                END IF
            END WHILE

         */
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
        if (gameType.equals(GameType.TEXT)) {
            if (val >= 'a' && val <= 'z') {
                Guess[position] = val;
            } else {
                System.out.println("Must be a letter!");
            }
        } else if (gameType.equals(GameType.NUMBER)) {
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
    void showSolution(SecretCode secretCode){
        return;
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
