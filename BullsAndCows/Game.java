package BullsAndCows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Float.isNaN;

public class Game {

    private Player currentPlayer;
    private Players allPlayers;
    private char[] Guess, preChangeGuess;
    private SecretCode codeGame;
    public int status = 0;
    private Scanner inputScanner;

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
                            if (loginUser()) {
                                System.out.println("\nLogged in as: " + this.currentPlayer.getUsername());
                            } else {
                                System.out.println("\nLogin cancelled.");
                            }
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
                                System.out.println("\nUndo Confirmed.");
                            } else {
                                System.out.println("\nUndo Cancelled.");
                            }
                            break;

                        case "/save":
                            if (this.currentPlayer != null) {
                                if (this.saveGame()) {
                                    givenUp = true;
                                    finishInputGuess = true;
                                }
                            } else {
                                System.out.println("\nNo user logged in. You must be logged in to save and load games. Try /login.");
                            }

                            break;

                        case "/load":
                            if (this.currentPlayer != null) {
                                if (this.loadGame()) {
                                    this.currentPlayer.incrementCodesAttempted();
                                }
                            } else {
                                System.out.println("\nNo user logged in. You must be logged in to save and load games. Try /login.");
                            }

                            break;

                        case "/stats":
                            print_player_details(this.currentPlayer);
                            break;

                        case "/quit":
                            System.out.println("\nQuitting current game.");
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

            if (!gameOver) {
                if (submitGuess()) {
                    System.out.println("\nCongratulations, your guess was correct!!!");
                    currentPlayer.incrementCodesDeciphered();
                    gameOver = true;
                } else {
                    System.out.println("\nYour guess was incorrect, try again.");
                }
            }
        }

        this.currentPlayer.incrementCodesAttempted();
        this.allPlayers.saveUpdatedPlayers();
        System.out.println("\n\nGame Over :)");
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
    Create account for / log in the user
    @return true if login not cancelled by user
     */
    private boolean loginUser() {
        if (this.currentPlayer != null) {
            System.out.println("\nWarning: This will log out user (" + this.currentPlayer.getUsername() + ")");
        }
        System.out.println("\nEnter the user you wish to login >>>");

        String userLogin = inputScanner.nextLine();

        if (userLogin.compareTo("") == 0) {
            return false;
        } else {
            this.currentPlayer = this.allPlayers.findPlayer(userLogin);

            if (this.currentPlayer == null) {
                this.currentPlayer = new Player(userLogin);
                System.out.println("\nCreating account with username: " + userLogin);
            }

            return true;
        }
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

    /*
    Saves the current secret code game for user to try again later, then end game
     */
    private boolean saveGame(){
        final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        final String playerSavePath = "./BullsAndCows/playerSaves/" + this.currentPlayer.getUsername() + ".txt";

        try {
            FileOutputStream fileOut = new FileOutputStream(playerSavePath, true);
            fileOut.write(LocalDateTime.now().format(CUSTOM_FORMATTER).getBytes());
            fileOut.write(this.showSolution(this.codeGame).getBytes());
            fileOut.close();

            return true;
        }
        catch (IOException e) {
            System.err.println("\nFatal IO error; this shouldn't happen");
            System.exit(0);
        }

        return false;
    }

    /*
    Load a game from users saved games, player validated by now, so currentPlayer never null
    @returns boolean based on completion or not
     */
    private boolean loadGame(){
        final String playerSavePath = "./BullsAndCows/playerSaves/" + this.currentPlayer.getUsername() + ".txt";
        String datetime, savedCode;
        ArrayList<String> readLines = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(playerSavePath));
            while (scanner.hasNextLine()) {
                readLines.add(scanner.nextLine());
            }
            scanner.close();

            if (readLines.size() <= 1) {
                System.out.println("\nNo saved codes for this user. Try saving the current game.");
                return false;
            } else {
                return this.confirmLoadGame(readLines);
            }
        }
        catch (IOException e) {
            System.err.println("\nFatal IO error; this shouldn't happen");
            System.exit(0);
        }

        return false;
    }

    private boolean confirmLoadGame(ArrayList<String> lines) {
        StringBuilder loadOptions = getLoadOptions(lines);

        while (true) {
            System.out.println("\nGames saved for user: " + this.currentPlayer.getUsername());
            System.out.println("\n" + loadOptions + "\n");

            Scanner loadScan = inputScanner;
            System.out.println("\nWhat game would you like to load? >>>");

            String loadIndex = loadScan.nextLine();
            if (loadIndex.compareTo("") == 0) {
                System.out.println("\nLoad game cancelled.");
                return false;
            }

            if (Integer.parseInt(loadIndex) > 0 && Integer.parseInt(loadIndex) <= (lines.size()/2)) {
                String loadDate = lines.get( (Integer.parseInt(loadIndex) * 2) - 2 );
                String loadCode = lines.get( (Integer.parseInt(loadIndex) * 2) - 1 );
                System.out.println("\nLoading game from " + loadDate);

                if (isNumeric(loadCode)) {
                    this.codeGame = new NumbersCode(loadCode.toCharArray());
                } else {
                    this.codeGame = new LettersCode(loadCode.toCharArray());
                }

                return true;
            } else {
                System.out.println("\nOption out of range. Please select a valid option.\n");
            }
        }
    }

    private StringBuilder getLoadOptions(ArrayList<String> lines) {
        StringBuilder loadOptions = new StringBuilder();

        for (int i = 0; i < lines.size(); i += 2) {
            int loadI = (i / 2) + 1;
            String codeType;

            if (isNumeric(lines.get(i+1))) {
                codeType = "Number Code";
            } else {
                codeType = "Letter Code";
            }

            String gameSavedLine = "(" + loadI + ") Game Saved - " + lines.get(i) + " (" + codeType + ")\n";
            loadOptions.append(gameSavedLine);
        }

        return loadOptions;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /*
    Gets solution from secret code class and returns

    @param secretCode instance
    @return solution as String
    */
    private String showSolution(SecretCode secretCode){
        StringBuilder solution = new StringBuilder();

        for (char c : secretCode.getCode()) {
            solution.append(String.valueOf(c));
        }

        return solution.toString();
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

    /*
    Prints the total number of cows achieved by all players
    */
    void print_total_cows(){
        System.out.println("TOTAL COWS ACHIEVED: " + allPlayers.getAllPlayerCows());
    }

    /*
    Prints the total number of bulls achieved by all players
    */
    void print_total_bulls(){
        System.out.println("TOTAL COWS ACHIEVED: " + allPlayers.getAllPlayerBulls());
    }

    /*
    Prints the total number of codes attempted by all players
    */
    void print_total_attempts(){
        System.out.println("TOTAL COWS ACHIEVED: " + allPlayers.getAllPlayersSecretCodesAttempted());
    }

    /*
    Prints the total number of codes deciphered by all players
    */
    void print_total_deciphered(){
        System.out.println("TOTAL COWS ACHIEVED: " + allPlayers.getAllPlayersSecretCodesDeciphered());
    }

    /*
    Prints the username and number of codes deciphered of the top ten players
    */
    void print_top_ten(){

        Player[] topTen = allPlayers.getTopTen();
        System.out.println("THE TOP 10 PLAYERS BASED ON CODES COMPLETED:");
        System.out.println();

        int i = 0;
        while(topTen[i] != null && i<10) {
            System.out.println("USER: " + topTen[i].getUsername() + "\tSCORED: " + topTen[i].getCodesDeciphered());
            i++;
        }
    }
}