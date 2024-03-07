package BullsAndCows;

public class Players {

    private Player[] allPlayers;


    public Players(){

    }

    void addPlayer(Player p){

    }

    void savePlayers(){

    }

    void findPlayer(Player p){

    }

    /*
    Sums the total number of Bulls achieved by all stored players

    @returns The total number of Bulls
     */
    int getAllPlayerBulls(){
        int totalBulls = 0;
        for(Player p : allPlayers){
            totalBulls = totalBulls + p.getBulls();
        }
        return totalBulls;
    }

    /*
    Sums the total number of Cows achieved by all stored players

    @returns The total number of Cows
     */
    int getAllPlayerCows(){
        int totalCows = 0;
        for(Player p : allPlayers){
            totalCows = totalCows + p.getCows();
        }
        return totalCows;
    }

    /*
    Sums the total number of Secret Codes attempted by all stored players

    @returns The total number of Secret Codes attempted
     */
    int getAllPlayersSecretCodesAttempted(){
        int totalCodes = 0;
        for(Player p : allPlayers){
            totalCodes = totalCodes + p.getCodesAttempted();
        }
        return totalCodes;
    }

    /*
    Sums the total number of Secret Codes deciphered by all stored players

    @returns The total number of Secret Codes deciphered
     */
    int getAllPlayersSecretCodesDeciphered(){
        int totalCodes = 0;
        for(Player p : allPlayers){
            totalCodes = totalCodes + p.getCodesDeciphered();
        }
        return totalCodes;
    }
}
