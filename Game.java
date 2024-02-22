public class Game {
    private Player currentPlayer;
    private char[] Guess;

    public Game(Player p, String codeType){
        Guess = new char[]{'\0', '\0', '\0', '\0'};
    }

    public Game(Player p){

    }

    void getHint(){

    }

    void loadPlayer(){

    }

    void playGame(){

    }

    void requestCode(){

    }

    /*
    Checks if given input position and value are valid for current code-type. If so, inserts it into the
    guess array at corresponding position
    @param position the position in the Guess array the value is being entered
    @param val the value being entered into the Guess array
    */
    void enterGuess(int position, char val){

        // Checks if given position is out of range
        if(position < 0 || position > 4){
            System.out.println("Invalid position!");
            return;
        }

        // If Word Code
        if(val >= 'a' && val <= 'z') {
            Guess[position] = val;
        }
        else{
            System.out.println("Must be a letter!");
        }

        // If Number Code
        if(val >= '0' && val <= '9'){
            Guess[position] = val;
        }
        else{
            System.out.println("Must be a number!");
        }

        return;
    }

    /*
    Validates input and passes guess off to be checked
    @return true if guess matches code
    */
    private boolean submitGuess(){
        if(validateInput() == true){
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

    void showSolution(){

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
