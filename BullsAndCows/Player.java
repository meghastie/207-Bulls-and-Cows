package BullsAndCows;

import java.io.File;

public class Player {

    //<editor-fold desc="Fields">
    private String username;
    private int numberOfBulls;
    private int numberOfCows;
    private int numberOfGuesses;
    private int codesAttempted;
    private int codesDeciphered;
    //</editor-fold>

    //<editor-fold desc="constructors">
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

    //</editor-fold>

    /*
    Create a save file for this player's saved games
     */
    private void createSavesLocation() {
        final String playerSavePath = "./BullsAndCows/playerSaves/";
        new File(playerSavePath + this.username + ".txt");
    }

    //<editor-fold desc = "Setters">

    /*
    Increases the number of Bulls by the given amount

    @param Bulls The number of bulls added to the total
     */
    public void updateBulls(int Bulls){
        numberOfBulls = numberOfBulls + Bulls;
    }

    /*
    Increases the number of Cows by the given amount

    @param Cows The number of cows added to the total
     */
    public void updateCows(int Cows){
        numberOfCows = numberOfCows + Cows;
    }

    /*
    Increases the number of total guesses made by 1.
     */
    public void incrementGuesses(){
        this.numberOfGuesses = this.numberOfGuesses + 4;
    }

    /*
    Increases the number of total code attempts made by 1.
     */
    public void incrementCodesAttempted(){
        this.codesAttempted++;
    }

    /*
    Increases the number of total code deciphered made by 1.
     */
    public void incrementCodesDeciphered(){
        this.codesDeciphered++;
    }

    //</editor-fold>


    //<editor-fold desc="Getters">
    /*
    returns players username.
     */
    public String getUsername() {
        return this.username;
    }

    /*
    Returns number of bulls for specific player.
     */
    public int getBulls(){
        return this.numberOfBulls;
    }

    /*
    Returns number of cows for specific player.
     */
    public int getCows(){
        return this.numberOfCows;
    }

    /*
    Returns number of guesses made by specific player.
     */
    public int getGuesses() {
        return this.numberOfGuesses;
    }

    /*
    Returns number of codes attempted for specific player.
     */
    public int getCodesAttempted(){
        return this.codesAttempted;
    }

    /*
    Returns number of codes deciphered for specific player.
     */
    public int getCodesDeciphered(){
        return this.codesDeciphered;
    }

    //</editor-fold>


    /*
    Calculates the accuracy of bulls as a percentage of the total number of characters guessed

    @return The accuracy
     */
    public float bullsAccuracy(){
        return ((float)(numberOfBulls * 100) / numberOfGuesses);
    }

    /*
    Calculates the accuracy of cows as a percentage of the total number of characters guessed

    @return The accuracy
     */
    public float cowsAccuracy(){
        return ((float)(numberOfCows * 100) / numberOfGuesses);
    }
}
