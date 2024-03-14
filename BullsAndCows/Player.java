package BullsAndCows;

import java.io.File;

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
        username = name;
        numberOfBulls = bulls;
        numberOfCows = cows;
        numberOfGuesses = guesses;
        codesAttempted = attempted;
        codesDeciphered = deciphered;

        this.createSavesLocation();
    }

    /*
    Create a save file for this player's saved games
     */
    private void createSavesLocation() {
        final String playerSavePath = "./BullsAndCows/playerSaves/";
        new File(playerSavePath + this.username + ".txt");
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

    /*
    Increases the number of total code attempts made by 1.
     */
    void incrementCodesAttempted(){
        this.codesAttempted++;
    }

    /*
    Increases the number of total code deciphered made by 1.
     */
    void incrementCodesDeciphered(){
        this.codesDeciphered++;
    }

    /*
    returns players username.
     */
    String getUsername() {
        return this.username;
    }

    /*
    Returns number of bulls for specific player.
     */
    int getBulls(){
        return this.numberOfBulls;
    }

    /*
    Returns number of cows for specific player.
     */
    int getCows(){
        return this.numberOfCows;
    }

    /*
    Returns number of guesses made by specific player.
     */
    int getGuesses() {
        return this.numberOfGuesses;
    }

    /*
    Returns number of codes attempted for specific player.
     */
    int getCodesAttempted(){
        return this.codesAttempted;
    }

    /*
    Returns number of codes deciphered for specific player.
     */
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
