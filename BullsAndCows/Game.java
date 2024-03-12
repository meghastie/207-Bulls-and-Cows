package BullsAndCows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Float.isNaN;

public class Game {

    private Player currentPlayer;
    private Players allPlayers;
    private char[] Guess, preChangeGuess;
    private SecretCode codeGame;
    public int status = 0;
    private Scanner inputScanner;
    private int addPlayersFrom;

    public Game(Player p, String codeType){
        this.currentPlayer = p;
        resetGuess();
    }

    public Game(SecretCode setCode){
        codeGame = setCode;
        resetGuess();
        inputScanner = new Scanner(System.in);
        playGame();
    }

    public Game(Player p){
        this.currentPlayer = p;
    }

    public Game(){
        resetGuess();
        inputScanner = new Scanner(System.in);
        playGame();
    }

    /*
   Get a hint from the secret code and return

   @return a hint to help the user guess the code
   */
    String getHint(){
        return "";
    }

    public SecretCode getCodeGame() {
        return codeGame;
    }

    /*
    Load all players from a file, and store in instance of players class
    players.txt should be in form  <playerName>,<bulls>,<cows>,<guesses>,<attempts>,<completed>\n
     */
    void loadAllPlayers() {
        final String playersFilePath = "./BullsAndCows/players.txt";
        String line;
        String[] parsedLine;
        Player newPlayer;

        // Create new instance of players
        this.allPlayers = new Players();

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

                allPlayers.addPlayer(newPlayer);
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("\nFile not found, exiting program");
            System.exit(0);
        }
    }

    /*
    Adds all new players created to file, should run when game is quit
    each line should be in form  <playerName>,<bulls>,<cows>,<guesses>,<attempts>,<completed>\n
     */
    void updatePlayers() {
        final String playerFilePath = "./BullsAndCows/players.txt";

        try {
            FileOutputStream fileOut = new FileOutputStream(playerFilePath, true);

            for (int i = this.addPlayersFrom; i < allPlayers.getPlayerCount(); i++) {       // Append all players to file, apart from ones already in
                fileOut.write(formatPlayer(this.allPlayers.findPlayer(i)).getBytes());
            }

            fileOut.close();
        }
        catch (IOException e) {
            System.err.println("\nFatal IO error; this shouldn't happen");
            System.exit(0);
        }                                    // file will already have been found by loadPlayers at beginning, so no need to catch again
    }

    /*
    Formats a players data to be written to file
    should be in form  <playerName>,<bulls>,<cows>,<guesses>,<attempts>,<completed>\n
     */
    private String formatPlayer(Player p) {
        return p.getUsername() + "," + p.getBulls() + "," + p.getCows() + "," + p.getGuesses() + "," + p.getCodesAttempted() + "," + p.getCodesDeciphered() + "\n";
    }

    /*
    Create account for / log in the user (load player), if not already logged in
    @param the username the user wants to log in as
     */
    void loadPlayer(String loginUser) {
        if (this.currentPlayer != null) {                                       // if logged in already
            System.out.println("\nAlready logged in as user: " + this.currentPlayer.getUsername() + ". Try exiting current game?");
        } else {
            this.currentPlayer = this.allPlayers.findPlayer(loginUser);         // null if user doesnt exist

            if (this.currentPlayer == null) {                                   // if player does not exist
                this.currentPlayer = new Player(loginUser);
                System.out.println("\nCreating account with username: " + loginUser);
            } else {                                                            // player exists
                System.out.println("\nLogging in as user: " + loginUser);
            }
        }
    }

    /*
    Resets the current guess for making a new guess
     */
    void resetGuess() {
        this.Guess = new char[]{'\0', '\0', '\0', '\0'};
        this.preChangeGuess = new char[]{'\0', '\0', '\0', '\0'};
    }

    /*
    Formats the array as a string to output
    @return formatted string of Guess array
     */
    String showGuess() {
        return "[ " + this.Guess[0] + " ] [ " + this.Guess[1] + " ] [ " + this.Guess[2] + " ] [ " + this.Guess[3] + " ]";
    }

    /*
    Main game loop for one "round" each time function is called
    @param is the game type a number game? (True -> number game, False -> letter game)
    */
    void playGame() {                       // Main game loop (Noa)
        // introduce game
        System.out.println("\n\nWelcome to Bulls and Cows. Please alter and submit your guess, or type /help for instructions of how to play.");

        // local variables
        boolean gameOver, finishInputGuess, givenUp;
        String userInput;

        // create an instance of chosen secret code
        //if (isNumberGame) {codeGame = new NumbersCode();} else {codeGame = new LettersCode();}
        if(codeGame == null) {
            gameSelection();
        }

        gameOver = false;
        while (!gameOver) {                                     // begin game loop
            finishInputGuess = false;
            givenUp = false;

            while (!finishInputGuess) {                          // begin loop for making guess
                //inputScanner = new Scanner(System.in);
                System.out.println("\n\nCurrent Guess: " + showGuess() + "\n>>> ");
                userInput = inputScanner.nextLine();            // receive input

                if (userInput.charAt(0) != '/') {               // input is not a user command
                    ArrayList<String> completedChanges = inputGuessChange(userInput);
                    if (completedChanges.isEmpty()) {
                        System.out.println("\nNo changes completed, maybe input had incorrect format?");
                    } else {
                        System.out.println("\nChanges completed: " + completedChanges);
                    }

                } else {                                        // input is a user command, or invalid

                    switch (userInput.toLowerCase()) {          // cases for all user commands

                        case "/login":
                            System.out.println("\nLOGIN USER");
                            break;

                        case "/help":
                            printInstructions();
                            break;

                        case "/guess":
                            finishInputGuess = true;
                            break;

                        case "/hint":
                            System.out.println(getHint());
                            break;

                        case "/giveup":
                            if (giveUpConfirmation()) {
                                System.out.println("\nSolution: " + showSolution(codeGame));
                                givenUp = true;
                                finishInputGuess = true;
                            }
                            break;

                        case "/undo":
                            if (undoConfirmation()) {
                                System.out.println("\nUNDO CONFIRMED");
                            } else {
                                System.out.println("\nUNDO CANCELLED");
                            }
                            break;

                        case "/save":
                            System.out.println("\nSAVE CURRENT CODE FOR LATER");
                            saveGuess();
                            break;

                        case "/load":
                            System.out.println("\nLOAD CODE FROM PREVIOUS");
                            loadGame();
                            break;

                        case "/stats":
                            System.out.println("\nMY CURRENT STATS");
                            break;

                        case "/quit":
                            System.out.println("\nQUITING GAME");
                            givenUp = true;
                            finishInputGuess = true;
                            break;

                        default:                                // case when input is not recognised
                            System.out.println("\nInput not recognised as a command or guess, try /help to see instructions and try again.");
                            break;
                    }
                }
            }

            // valid input has been given
            gameOver = givenUp;

            if (!givenUp) {
                if (submitGuess()) {
                    System.out.println("\nYOUR GUESS WAS CORRECT!!!");
                    currentPlayer.incrementCodesDeciphered();
                    gameOver = true;
                } else {
                    System.out.println("\nYOUR GUESS WAS INCORRECT, TRY AGAIN");
                }
            }
        }
        this.currentPlayer.incrementCodesAttempted();
        updatePlayers();
        System.out.println("GAME FINISHED");
    }


    /*
    Prints the instructions
     */
    private void printInstructions(){
        final String instructions =   "\nHOW TO PLAY\n\nYou are tasked with deciphering a secret code, consisting of either 4 different numbers or " +
                "letters.\nEach guess you make, you will be shown how many 'Bulls' and 'Cows' you managed to get, meaning how " +
                "many numbers / letters you got correct & in the right position, and how many you just got correct, " +
                "respectively.\nCOMMANDS\n\nYou begin with an empty guess. To set one of the characters in your guess, type the " +
                "letter / number you wish to guess, followed by the position to put that guess (between 1 and 4), e.g. a4. To " +
                "change more than one position at a time, simply enter a comma followed by your next guess, e.g. a4,g1,h3. Note " +
                "there are no spaces\nOther commands are as follows:\n\t/login -\tLogin or create an account to save your " +
                "score and statistics, and maybe you can top the leaderboard!\n\t/hint -\tIf you are stuck, receive a hint for your " +
                "guess\n\t/giveup -\tIf you are really stuck, you can give up and see the solution\n\t/guess -\tSubmit your " +
                "completed guess, all positions of your guess must be filled.\n\t/undo -\t If you want to undo a " +
                "change you've made to your guess (only one undo can be made to any given position).\n\t/save -\tSave the secret " +
                "code you are currently guessing to try again later.\n\t/load -\tLoad a previous secret code to resume guessing.\n\t" +
                "/stats -\tView game play statistics, such as accuracy over all games.\n\t/quit -\tQuit current game without saving " +
                "or completing guess.";

        System.out.println(instructions);
    }

    /*
    Check if the user input is valid for changing the guess, any invalid inputs are skipped
    @return array list of completed changes
     */
    private ArrayList<String> inputGuessChange(String userInput) {
        if (userInput.contains(" ") || userInput.contains("-") || userInput.length() < 2 || (userInput.length() > 2 && (userInput.length()+1) % 3 != 0)) {
            return new ArrayList<>();
        }

        ArrayList<String> completed = new ArrayList<>();

        for (int i = 0; i < userInput.length(); i += 3) {
            char inputChangeChar = userInput.charAt(i);
            int inputChangePos = Integer.parseInt(String.valueOf(userInput.charAt(i + 1))) -1;

            try {
                enterGuess(inputChangePos, inputChangeChar);
                completed.add(userInput.substring(i, i+2));
            } catch (ArrayIndexOutOfBoundsException err) {
                System.out.println("\nError in changing guess: Position '" + inputChangePos + "' was not a valid position.");
            } catch (IllegalArgumentException err) {
                System.out.println("\nError in changing guess: " + err);
            } catch (RuntimeException err) {
                System.out.println("\nError in classes: " + err);
            }
        }

        return completed;
    }

    public void gameSelection(){
        boolean check = true;
        while(check){
            //inputScanner = new Scanner(System.in);
            System.out.println("What type of game do you want ot play? number or letter?");
            String input = inputScanner.nextLine().toLowerCase();
            switch(input){
                case "help":
                    printHelp();
                    break;
                case "number":
                    codeGame = new NumbersCode();
                    check = false;
                    break;
                case "letter":
                    codeGame = new LettersCode();
                    check = false;
                    break;
                //Case purely for testing
                case "letterinvalidfile":
                    codeGame = new LettersCode();
                default:
                    System.out.println("Enter a valid command");
                    break;
            }
        }
    }

    /*
    Prints how to choose a game type
     */
    private void printHelp(){
        System.out.println("Possible commands\n" +
                "/number select number game\n" +
                "/letter select letter game\n" +
                "/help shows list of commands");
    }

    /*
    Helper function for confirming the user wishes to give up
     */
    private boolean giveUpConfirmation() {
        while (true) {
            System.out.println("\nAre you sure you want to give up? (y / n) >>>");

            String userConfirm = inputScanner.nextLine();

            if (userConfirm.compareTo("y") == 0) {
                return true;
            } else if (userConfirm.compareTo("n") == 0) {
                return false;
            } else {
                System.out.println("\nError: Please choose y / n\n");
            }
        }
    }

    /*
    Helper function for confirming the user wishes to give up
     */
    private boolean undoConfirmation() {
        while (true) {
            Scanner undoScan = inputScanner;
            System.out.println("\nWhat position of your guess do you wish to undo? >>>");

            String undoPos = undoScan.nextLine();
            if (undoPos.compareTo("") == 0) {
                return false;
            }

            if (Integer.parseInt(undoPos) >= 1 && Integer.parseInt(undoPos) <= 4) {
                undoGuess(Integer.parseInt(undoPos) - 1);
                return true;
            } else {
                System.out.println("\nError: Please choose a position between 1 and 4, or press ENTER to exit\n");
            }
        }
    }

    void requestCode(){
        
    }

    /*
    Checks if given input position and value are valid for current code-type. If so, inserts it into the
    guess array at corresponding position
    @param position The position in the Guess array the value is being entered
    @param val      The value being entered into the Guess array

    @throws ArrayIndexOutOfBoundsException If the given position does not exist in the Guess array
    @throws IllegalArgumentException       If the given value is not acceptable for the given code type
    @throws RuntimeException               If codeGame exists as neither types of subclass
    */
    private void enterGuess(int position, char val) {
        // Checks if given position is out of range
        if (position < 0 || position > 4) {
            throw new ArrayIndexOutOfBoundsException();
        }

        // Checks the current game type, then checks if the appropriate characters have been used
        if (codeGame.getClass() == LettersCode.class) {
            val = Character.toLowerCase(val);
            if (val >= 'a' && val <= 'z') {
                preChangeGuess[position] = Guess[position];
                Guess[position] = val;
            } else {
                throw new IllegalArgumentException("Value must be a letter");
            }
        } else if (codeGame.getClass() == NumbersCode.class) {
            if (val >= '0' && val <= '9') {
                preChangeGuess[position] = Guess[position];
                Guess[position] = val;
            } else {
                throw new IllegalArgumentException("Value must be a Number");
            }
        } else{
            throw new RuntimeException("codeGame exists as neither Subclass");
        }
    }

    /*
    Validates input and passes guess off to be checked
    @return true if guess matches code
    */
    private boolean submitGuess(){
        if(validateInput()){
            int[] bullsCows = codeGame.compareCode(Guess);
            System.out.println("Bulls: " + bullsCows[0]);
            System.out.println("Cows: " + bullsCows[1]);

            currentPlayer.updateBulls(bullsCows[0]);
            currentPlayer.updateCows(bullsCows[1]);
            currentPlayer.incrementGuesses();

            return bullsCows[0] == 4;
        }

        return false;
    }

    /*
    Sets the value at one of the given places to null
    @param position number of place to null
    */
    private void undoGuess(int position){
        if(Guess[position] == '\0'){
            System.out.println("A complete guess has not been made yet");
        } else {
            Guess[position] = preChangeGuess[position];
        }
    }

    void saveGuess(){

    }

    void loadGame(){

    }

    /*
    Gets solution from secret code class and returns

    @param secretCode instance
    @return solution as String
    */
    private String showSolution(SecretCode secretCode){
        return "";
    }

    /*
    Checks the Guess for empty positions and duplicate values

    @return true if Guess contains none of the above
    */
    private boolean validateInput(){

        for(int i = 0; i<4; i++){

            // Checks for missing value
            if(Guess[i] == '\0'){
                System.out.println("Missing value at position " + i);
                return false;
            }

            // Checks for duplicate values
            for (int j = i + 1; j<4; j++) {
                if (Guess[i] == Guess[j]) {
                    System.out.println("Duplicate value present at positions " + i + " & " + j);
                    return false;
                }
            }
        }

        return true;
    }

    /*
    Prints all of a user's details

    @param p    The player to be printed
     */
    private void print_player_details(Player p){
        System.out.println("USERNAME:        " + p.getUsername());
        System.out.println("CODES ATTEMPTED: " + p.getCodesAttempted());
        System.out.println("CODES COMPLETED: " + p.getCodesDeciphered());
        System.out.println("BULLS:           " + p.getBulls());

        System.out.println("ACCURACY:        ");
        if(isNaN(p.bullsAccuracy())){
            System.out.print("N/A");
        }
        else{
            System.out.print(new DecimalFormat("#.##").format(p.bullsAccuracy()) + "%");
        }

        System.out.println("COWS:            " + p.getCows());

        System.out.println("ACCURACY:        ");
        if(isNaN(p.cowsAccuracy())){
            System.out.print("N/A");
        }
        else{
            System.out.print(new DecimalFormat("#.##").format(p.cowsAccuracy()) + "%");
        }

        System.out.println();
    }
}