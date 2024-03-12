package BullsAndCows;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Players {

    private ArrayList<Player> allPlayers;


    public Players(){
        allPlayers = new ArrayList<>();
    }

    public void addPlayer(Player p){
        this.allPlayers.add(p);
    }

    public void savePlayers(){                 // loadPlayers does what I assume this is meant to, but in game class, could be moved?
        try {
            String filePath = "Players.txt";
            FileWriter writer = new FileWriter(filePath);
            for (Player p : this.allPlayers) {
                writer.write(p.getUsername() + "," + p.getBulls() + "," + p.getCows() + "," + p.getGuesses() + "," + p.getCodesAttempted() + "," + p.getCodesDeciphered() + "\n");
            }
            writer.close();
        }
        catch (IOException e) {
            System.err.println("File not found, exiting program");
            System.exit(0);
        }
    }

    /*
    Searches the list of all users for the player with the given username

    @param targetUser   The username of the desired player
    @return             The player with the given username. Null if none found.
     */
    Player findPlayer(String targetUser){
        for(Player p : this.allPlayers){
            if(p.getUsername().equals(targetUser)){
                return p;
            }
        }
        return null;
    }

    /*
    Searches the list of all users for the player with the given username

    @param targetIndex  The index of the desired player in allPlayers list
    @return             The player with the given index, or null if index out of bounds
     */
    Player findPlayer(int targetIndex){
        if (targetIndex >= this.allPlayers.size() || targetIndex < 0) {
            return null;
        }

        return this.allPlayers.get(targetIndex);
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

    int getPlayerCount() {
        return this.allPlayers.size();
    }
}
