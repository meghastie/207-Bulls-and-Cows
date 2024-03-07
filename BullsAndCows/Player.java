package BullsAndCows;

public class Player {

    private String username;
    private int numberOfBulls;
    private int numberOfCows;
    private int numberOfGuesses;
    private int codesAttempted;
    private int codesDeciphered;

    public Player(){

    }

    public Player(String name){

    }

    public Player(String name, int bulls, int cows, int guesses, int attempted, int deciphered){

    }

    /*
    Increases the number of Bulls by the given amount

    @param Bulls The number of bulls added to the total
     */
    void updateBulls(int Bulls){
        numberOfBulls = numberOfBulls + Bulls;
    }

    /*
    Increases the number of Cows by the given amount

    @param Cows The number of cows added to the total
     */
    void updateCows(int Cows){
        numberOfCows = numberOfCows + Cows;
    }

    /*
    Increases the number of total guesses made by 1.
     */
    void incrementGuesses(){
        this.numberOfGuesses++;
    }

    void incrementCodesAttempted(){

    }

    void incrementCodesDeciphered(){

    }

    void getBulls(){

    }

    void getCows(){

    }

    void getCodesAttempted(){

    }

    void getCodesDeciphered(){

    }

    void getUsername(){

    }
}
