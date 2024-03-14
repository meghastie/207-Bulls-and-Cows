package Testing;

import org.junit.jupiter.api.*;
import BullsAndCows.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.Arrays;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayersTest {

    private Players playerList;

    @BeforeEach
    void populatePlayers(){
        ArrayList<Player> tempPlayerList = new ArrayList<>();
        tempPlayerList.add(new Player("User1", 5, 8, 20, 3, 2));
        tempPlayerList.add(new Player("User2", 9, 5, 17, 3, 3));
        tempPlayerList.add(new Player("User3", 16, 25, 50, 6, 6));
        tempPlayerList.add(new Player("User4", 2, 1, 5, 1, 0));
        tempPlayerList.add(new Player("User5", 98, 156, 300, 30, 30));
        tempPlayerList.add(new Player("User6", 15, 22, 42, 4, 3));
        tempPlayerList.add(new Player("User7", 32, 56, 109, 8, 8));
        tempPlayerList.add(new Player("User8", 0, 0, 0, 0, 0));
        tempPlayerList.add(new Player("User9", 32, 31, 70, 4, 4));
        tempPlayerList.add(new Player("User10", 14, 20, 48, 3, 2));
        tempPlayerList.add(new Player("User11", 8, 8, 20, 2, 2));
        tempPlayerList.add(new Player("User12", 17, 28, 49, 4, 3));
        tempPlayerList.add(new Player("User13", 0, 5, 16, 1, 0));
        tempPlayerList.add(new Player("User14", 38, 40, 98, 4, 4));
        tempPlayerList.add(new Player("User15", 156, 201, 425, 45, 40));

        playerList = new Players(tempPlayerList);
    }

    //<editor-fold desc="Method Tests">

    @Test
    void findPlayerTest(){
        Player p = playerList.findPlayer("User12");
        assertNotNull(p);
        assertEquals("User12", p.getUsername());
        assertEquals(17, p.getBulls());

        assertNull(playerList.findPlayer("User"));
    }

    @Test
    void addPlayerTest(){
        Player p = new Player("NewUser");
        playerList.addPlayer(p);
        assertEquals(16, playerList.getPlayerCount());

        Player a = playerList.findPlayer("NewUser");
        assertNotNull(a);
        assertEquals(a,p);
    }

    @Test
    void getTopTenStandardTest(){
        Player[] topTen = new Player[]{
            playerList.findPlayer("User15"),
            playerList.findPlayer("User5"),
            playerList.findPlayer("User7"),
            playerList.findPlayer("User3"),
            playerList.findPlayer("User9"),
            playerList.findPlayer("User14"),
            playerList.findPlayer("User2"),
            playerList.findPlayer("User6"),
            playerList.findPlayer("User12"),
            playerList.findPlayer("User1")
        };

        assertArrayEquals(topTen, playerList.getTopTen());
    }

    @Test
    void getTopTenPartialFullTest(){
        ArrayList<Player> tempPlayerList = new ArrayList<Player>();
        tempPlayerList.add(new Player("User1", 5, 8, 20, 3, 2));
        tempPlayerList.add(new Player("User2", 9, 5, 17, 3, 3));
        tempPlayerList.add(new Player("User3", 16, 25, 50, 6, 6));
        tempPlayerList.add(new Player("User4", 2, 1, 5, 1, 0));
        tempPlayerList.add(new Player("User5", 98, 156, 300, 30, 30));

        Players TopTenPlayerList = new Players(tempPlayerList);

        Player[] topTen = new Player[]{
                tempPlayerList.get(4),
                tempPlayerList.get(2),
                tempPlayerList.get(1),
                tempPlayerList.get(0),
                tempPlayerList.get(3),
                null,null,null,null,null
        };

        assertArrayEquals(topTen, TopTenPlayerList.getTopTen());
    }

    @Test
    void getTopTenEmptyTest(){
        Players emptyList = new Players(new ArrayList<Player>());
        Player[] emptyArray = new Player[10];

        assertArrayEquals(emptyArray, emptyList.getTopTen());
    }

    @Test
    void getAllTest(){
        assertEquals(442, playerList.getAllPlayerBulls());
        assertEquals(606, playerList.getAllPlayerCows());
        assertEquals(118, playerList.getAllPlayersSecretCodesAttempted());
        assertEquals(107, playerList.getAllPlayersSecretCodesDeciphered());
    }

    @Test
    void EmptyGetAllTest(){
        Players emptyList = new Players(new ArrayList<Player>());
        assertEquals(0, emptyList.getAllPlayerBulls());
        assertEquals(0, emptyList.getAllPlayerCows());
        assertEquals(0, emptyList.getAllPlayersSecretCodesAttempted());
        assertEquals(0, emptyList.getAllPlayersSecretCodesDeciphered());
    }
    //</editor-fold>

}
