package BullsAndCows;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Players {

    private ArrayList<Player> allPlayers;
    private int addPlayersFrom;


    public Players(ArrayList<Player> p){
        allPlayers = p;
    }

    public Players(){
        allPlayers = new ArrayList<>();

        this.addPlayersFrom = 0;
        loadAllPlayers();
    }

    //<editor-fold desc="Manage List">

    /*
    Deletes all Saved coded and the text files storing them from the playerSaves folder
    and empties the players.txt file.
    Deletes all saved user information
     */
    public void clearSaves(){
        File playerSaves = new File("./BullsAndCows/PlayerSaves");
        File[] savesContents = playerSaves.listFiles();
        if(savesContents != null)
            for(File file : savesContents)
                file.delete();
        try {
            FileWriter writer = new FileWriter("./BullsAndCows/players.txt", false);
            writer.write("");
            writer.close();
        }
        catch(IOException e){
            System.out.println("Couldn't delete contents of player.txt!");
        }

        allPlayers = new ArrayList<Player>();
    }

    public void addPlayer(Player p){
        this.allPlayers.add(p);
    }

    /*
    Load all players from a file, and store in instance of players class
    players.txt should be in form  <playerName>,<bulls>,<cows>,<guesses>,<attempts>,<completed>\n
     */
    private void loadAllPlayers() {
        final String playersFilePath = "./BullsAndCows/players.txt";
        String line;
        String[] parsedLine;
        Player newPlayer;

        try {                                                           // Try opening the file and reading data
            Scanner scanner = new Scanner(new File(playersFilePath));
            while (scanner.hasNext()) {
                this.addPlayersFrom++;                                  // Keep track of what players are already in file, for updating at end

                line = scanner.next();
                parsedLine = line.split(",");                    // Split by commas to get individual data

                newPlayer = new Player(
                        parsedLine[0],
                        Integer.parseInt(parsedLine[1]),
                        Integer.parseInt(parsedLine[2]),
                        Integer.parseInt(parsedLine[3]),
                        Integer.parseInt(parsedLine[4]),
                        Integer.parseInt(parsedLine[5])
                );

                this.addPlayer(newPlayer);
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("\nFile not found, exiting program");
            System.exit(0);
        }
    }

    /*
    Formats a players data to be written to file
    should be in form  <playerName>,<bulls>,<cows>,<guesses>,<attempts>,<completed>\n

    @return     Formatted string of player details
    @param p    Player object to be formatted into a string
     */
    private String formatPlayer(Player p) {
        return p.getUsername() + "," + p.getBulls() + "," + p.getCows() + "," + p.getGuesses() + "," + p.getCodesAttempted() + "," + p.getCodesDeciphered() + "\n";
    }

    /*
    Adds all new players created to file, should run when game is quit
    each line should be in form  <playerName>,<bulls>,<cows>,<guesses>,<attempts>,<completed>\n
     */
    public void saveUpdatedPlayers() {
        try {
            String playerFilePath = "./BullsAndCows/players.txt";
            FileWriter writer = new FileWriter(playerFilePath, false);

            for (Player p : this.allPlayers) {
                writer.write(formatPlayer(p));
            }
            writer.close();
        }
        catch (IOException e){
            System.err.println("File not found, exiting program");
            System.exit(0);
        }
    }

    //</editor-fold>

    /*
    Searches the list of all users for the player with the given username

    @param targetUser   The username of the desired player
    @return             The player with the given username. Null if none found.
     */
    public Player findPlayer(String targetUser){
        for(Player p : this.allPlayers){
            if(p.getUsername().equalsIgnoreCase(targetUser)){
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
    public Player findPlayer(int targetIndex){
        if (targetIndex >= this.allPlayers.size() || targetIndex < 0) {
            return null;
        }

        return this.allPlayers.get(targetIndex);
    }

    //<editor-fold desc="Get Cumulative"

    /*
    Sums the total number of Bulls achieved by all stored players

    @returns The total number of Bulls
     */
    public int getAllPlayerBulls(){
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
    public int getAllPlayerCows(){
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
    public int getAllPlayersSecretCodesAttempted(){
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
    public int getAllPlayersSecretCodesDeciphered(){
        int totalCodes = 0;
        for(Player p : allPlayers){
            totalCodes = totalCodes + p.getCodesDeciphered();
        }
        return totalCodes;
    }

    //</editor-fold>

    public int getPlayerCount() {
        return this.allPlayers.size();
    }

    /*
    Finds the 10 players with the most codes deciphered

    @returns An array holding the players doing the best
    */
    public Player[] getTopTen(){
        Player[] topTen = new Player[10];

        if(allPlayers.isEmpty()){
            System.out.println("Error: No players are stored");
            return topTen;
        }

        for(Player p : allPlayers){
            if(topTen[9] == null){
                int i = 0;
                while(topTen[i] != null){
                    i++;
                }
                topTen[i] = p;
                topTen = sortTopTenArray(topTen, i);
            }
            else{
                if(p.getCodesDeciphered() > topTen[9].getCodesDeciphered()) {
                    topTen[9] = p;
                    topTen = sortTopTenArray(topTen, 9);
                }
            }
        }

        return topTen;
    }

    /*
    Helper method to re-sort the list of top ten from best to worst

    @return         The sorted array of the top 10 players

    @param topTen   The unsorted array of the top 10 players
    @param last     The last position in the array
    */
    private Player[] sortTopTenArray(Player[] topTen, int last){
        int i = last;
        Player temp = topTen[last];
        while(i > 0 && temp.getCodesDeciphered() > topTen[i-1].getCodesDeciphered()){
            topTen[i] = topTen[i-1];
            i--;
        }
        topTen[i] = temp;
        return topTen;
    }
}