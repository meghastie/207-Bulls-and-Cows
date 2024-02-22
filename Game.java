public class Game {
    private Player currentPlayer;
    private char[] Guess;

    public Game(Player p, String codeType){
        Guess = new char[4];
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

    void enterGuess(){

    }

    void saveGuess(){

    }

    void loadGame(){

    }

    void showSolution(){

    }

    private boolean validateInput(String guess){

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
