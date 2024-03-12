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
        this.username = name;
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
        this.codesAttempted++;
    }

    void incrementCodesDeciphered(){
        this.codesDeciphered++;
    }

    String getUsername() {
        return this.username;
    }

    int getBulls(){
        return this.numberOfBulls;
    }

    int getCows(){
        return this.numberOfCows;
    }

    int getGuesses() {
        return this.numberOfGuesses;
    }

    int getCodesAttempted(){
        return this.codesAttempted;
    }

    int getCodesDeciphered(){
        return this.codesDeciphered;
    }

    /*
    Calculates the accuracy of bulls as a percentage of the total number of characters guessed

    @return The accuracy
     */
    float bullsAccuracy(){
        return ((float)(numberOfBulls * 100) / numberOfGuesses);
    }

    /*
    Calculates the accuracy of cows as a percentage of the total number of characters guessed

    @return The accuracy
     */
    float cowsAccuracy(){
        return ((float)(numberOfCows * 100) / numberOfGuesses);
    }
}
