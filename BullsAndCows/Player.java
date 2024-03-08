package BullsAndCows;

public class Player {

    private String username;
    private int numberOfBulls;
    private int numberOfCows;
    private int codesAttempted;
    private int codesDeciphered;

    public Player(String uName){
        this.username = uName;

        this.numberOfBulls = 0;
        this.numberOfCows = 0;
        this.codesAttempted = 0;
        this.codesDeciphered = 0;
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

    void incrementCodesAttempted(){

    }

    void incrementCodesDeciphered(){

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

    int getCodesAttempted(){
        return this.codesAttempted;
    }

    int getCodesDeciphered(){
        return this.codesDeciphered;
    }
}
