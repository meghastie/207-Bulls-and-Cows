package BullsAndCows;
import java.util.ArrayList;

public class Players {

    private ArrayList<Player> allPlayers;


    public Players(){
        allPlayers = new ArrayList<>();
    }

    void addPlayer(Player p){
        this.allPlayers.add(p);
    }

    void savePlayers(){

    }

    void findPlayer(Player p) {      // IS THIS NEEDED? IF SO WHAT FOR, I CREATED A getPlayer() METHOD TO GET A PLAYER BASED ON USERNAME, DO WE NEED BOTH

    }

    Player getPlayer(String userName){
        for (Player p : this.allPlayers) {
            if (p.getUsername().equals(userName)) {
                return p;
            }
        }

        return null;
    }

    void getAllPlayerBulls(){

    }

    void getAllPlayerCows(){

    }

    void getAllPlayersSecretCodesAttempted(){

    }

    void getAllPlayersSecretCodesDeciphered(){

    }
}
