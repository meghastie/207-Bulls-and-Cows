package BullsAndCows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Float.isNaN;

public class Game {

    //<editor-fold desc="Fields">

    private final int CODELENGTH;

    private enum STAGE{
        LOGIN,
        MENU,
        STATS,
        LEADERBOARD,
        GAME,
        GAMESELECT,
        EXIT,
        HOWTOPLAY
    }

    private Player currentPlayer;
    private Players allPlayers;
    private char[] Guess, preChangeGuess;
    private SecretCode codeGame;
    public int status = 0;
    private Scanner inputScanner;
    private boolean[] hintsUsed;

    //</editor-fold>


    //<editor-fold desc="Constructors">

    //Testing Constructor
    public Game(Player p, SecretCode setCode, int codeLength){
        CODELENGTH = codeLength;
        this.currentPlayer = p;
        this.codeGame = setCode;
        allPlayers = new Players();
        allPlayers.addPlayer(p);
        resetGuess();
        inputScanner = new Scanner(System.in);
    }

    // old Testing Constructor
    public Game(SecretCode setCode, int codeLength){
        CODELENGTH = codeLength;
        codeGame = setCode;
        allPlayers = new Players();
        resetGuess();
        inputScanner = new Scanner(System.in);
    }
    //old Testing Constructor
    public Game(Player p, int codeLength){
        CODELENGTH = codeLength;
        this.currentPlayer = p;
        allPlayers = new Players();
        resetGuess();
        inputScanner = new Scanner(System.in);
    }

    public Game(int codeLength){
        CODELENGTH = codeLength;
        allPlayers = new Players();
        resetGuess();
        inputScanner = new Scanner(System.in);
    }

    //</editor-folds>

    //<editor-fold desc="Test Methods">

    public SecretCode getCodeGame() {
        return codeGame;
    }

    public void testGuessOptions(){
        guessOptions();
    }

    public Players getPlayerList(){
        return allPlayers;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    //</editor-fold>

    /*
    Main game loop for one "round" each time function is called
    @param is the game type a number game? (True -> number game, False -> letter game)
    */
    public void playGame() {
        // Main game loop (Noa)
        // introduce game

        System.out.println("\n\nWelcome to Bulls and Cows. Please alter and submit your guess, or type /help for instructions of how to play. To start, enter 'game'");

        // local variables
        boolean gameOver = false, finishInputGuess, givenUp;

        String userInput;

        /*
        Switch statement inputted twice: allows user to log in/help/ any other commands before they start the game. if the user wants to just play the game straight away, they just input 'game'
         */
        do {
            userInput = inputScanner.nextLine();

            if (codeGame == null) {
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

                    case "/stats":
                        print_player_details(this.currentPlayer);
                        break;

                    case "/quit":
                        System.out.println("\nQuitting current game.");
                        gameOver = true;
                        break;

                    case "game":
                        gameSelection();
                        break;

                    default:                                // case when input is not recognised
                        System.out.println("\nInput not recognised as a command or guess, try /help to see instructions and try again, or type 'game' to begin the game.");
                        break;

                }
            }
        } while (!userInput.equalsIgnoreCase("game"));

        while (!gameOver) {                                     // begin game loop
            finishInputGuess = false;
            givenUp = false;

            while (!finishInputGuess) {                          // begin loop for making guess
                //inputScanner = new Scanner(System.in);
                System.out.println("\n\nCurrent Guess: " + showGuess() + "\n>>> ");
                userInput = inputScanner.nextLine();            // receive input
                if (userInput == null || userInput.isEmpty()) {
                    System.out.println("\nInput not recognised as a command or guess, try /help to see instructions and try again.");
                } else if (userInput.charAt(0) != '/') {               // input is not a user command
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
                            System.out.println(this.getHint());
                            break;

                        case "/giveup":
                            if (giveUpConfirmation()) {
                                System.out.println("\nSolution: " + showSolution());
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
                            gameOver = true;
                            finishInputGuess = true;
                            break;

                        default:                                // case when input is not recognised
                            System.out.println("\nInput not recognised as a command or guess, try /help to see instructions and try again.");
                            break;
                    }
                }
            }

            // valid input has been given
            //gameOver = givenUp;

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
        if(currentPlayer != null){
            this.currentPlayer.incrementCodesAttempted();
            this.allPlayers.saveUpdatedPlayers();
        }
        System.out.println("\n\nGame Over :)");
    }

    //<editor-fold desc="Play Game 2 Methods"

    /*
    A stage-by-stage game play loop. Uses an enum to keep track of the stage
    */
    public void playGame2(){
        STAGE stage = STAGE.LOGIN;
        System.out.println("\n\nWelcome to Bulls and Cows!");

        for(;;){
            switch(stage){
                case LOGIN:
                    stage = requestLogin();
                    break;

                case MENU:
                    stage = menu();
                    break;

                case STATS:
                    displayStats();
                    stage = STAGE.MENU;
                    break;

                case LEADERBOARD:
                    displayLeaderBoard();
                    stage = STAGE.MENU;
                    break;

                case GAMESELECT:
                    if(newGame()) {
                        stage = STAGE.GAME;
                    } else {
                        stage = STAGE.MENU;
                    }
                    break;

                case GAME:
                    this.hintsUsed = new boolean[]{false, false, false, false};
                    guessOptions();
                    resetGuess();
                    stage = STAGE.MENU;
                    break;

                case HOWTOPLAY:
                    PrintHowToPlay();
                    stage = STAGE.MENU;
                    break;

                case EXIT:
                    // exit functions
                    allPlayers.saveUpdatedPlayers();
                    return;
            }
        }
    }

    /*
    Prints the instructions
     */
    private void printInstructions(){
        final String instructions = """

                HOW TO PLAY

                You are tasked with deciphering a secret code, consisting of either 4 different numbers or letters.
                Each guess you make, you will be shown how many 'Bulls' and 'Cows' you managed to get, meaning how many numbers / letters you got correct & in the right position, and how many you just got correct, respectively.
                COMMANDS
                
                Before the game begins, you are able to:
                \t/login -\tLogin or create an account to save your score and statistics, and maybe you can top the leaderboard!
                \t/stats -\tView current user's game play statistics, such as accuracy over all games.
                \t/quit -\tQuit game completely.
                \tgame - \tStart the game.

                You begin with an empty guess. To set one of the characters in your guess, type the letter / number you wish to guess, followed by the position to put that guess (between 1 and 4), e.g. a4. To change more than one position at a time, simply enter a comma followed by your next guess, e.g. a4,g1,h3. Note there are no spaces
                Other commands are as follows (as well as commands above):
                \t/hint -\tIf you are stuck, receive a hint for your guess
                \t/giveup -\tIf you are really stuck, you can give up and see the solution
                \t/guess -\tSubmit your completed guess, all positions of your guess must be filled.
                \t/undo -\t If you want to undo a change you've made to your guess (only one undo can be made to any given position).
                \t/save -\tSave the secret code you are currently guessing to try again later.
                \t/load -\tLoad a previous secret code to resume guessing.""";

        System.out.println(instructions);
    }

    /*
    Displays the list of commands required to play the game and how to change a guess
     */
    private void printCommands(){
        final String commands = """
                
                HOW TO ALTER GUESS:
                
                You begin with an empty guess
                To set one of the characters in your guess, type the letter / number you wish to guess,
                followed by the position to put that guess, between 1 and 4 (e.g. a4).
                To change more than one position at a time, simply enter a comma followed by your next guess (e.g. a4,g1,h3). Note there are no spaces
                
                
                COMMANDS:
                
                    /help   - Prints the list of valid commands (this prompt)
                    /save   - The current code is saved and can be reattempted later
                    /undo   - Reverts one of the guess positions back to its previous state
                    /submit - Submits the guess for comparison
                    /giveup - Forfeit game and return to menu
                
                """;
        System.out.println(commands);

    }

    /*
    Prints a description of how the game is played
     */
    private void PrintHowToPlay(){
        final String instructions = """

                HOW TO PLAY

                You are tasked with deciphering a secret code, consisting of either 4 different numbers or letters.
                Each guess you make, you will be shown how many 'Bulls' and 'Cows' you managed to achieve.
                A Cow represents a correct character, but is in the wrong position
                A Bull represents a correct character, at the correct location
                
                You win when you achieve 4 bulls: a complete correct guess!
            
                
                COMMANDS

                You begin with an empty guess. To set one of the characters in your guess, type the letter / number you wish to guess, followed by the position to put that guess (between 1 and 4), e.g. a4. To change more than one position at a time, simply enter a comma followed by your next guess, e.g. a4,g1,h3. Note there are no spaces
                Other commands are as follows:""";

        System.out.println(instructions);
    }

    /*
    Requests the user to select the type of game, or load an existing save

    @return If the user has created game. Will be false if they wish to back out of selection.
     */
    private boolean newGame() {
        for(;;) {

            System.out.println("\nWhat kind of code would you like? type \"back\" to return");
            System.out.println("1. Number code");
            System.out.println("2. Letter code");
            if (currentPlayer != null) {
                System.out.println("3. Load saved code");
            }

            System.out.print("\n>");

            String userInput = inputScanner.nextLine();

            switch(userInput){
                case "back":
                    return false;

                case "1":
                    codeGame = new NumbersCode(CODELENGTH);
                    System.out.println("\nNew number based code game created!");
                    if (currentPlayer != null)
                        currentPlayer.incrementCodesAttempted();
                    return true;

                case "2":
                    codeGame = new LettersCode(CODELENGTH);
                    System.out.println("\nNew letter based code game created!");
                    if (currentPlayer != null)
                        currentPlayer.incrementCodesAttempted();
                    return true;

                case "3":
                    if(currentPlayer != null){
                        if(loadGame()) {
                            try {
                                var temp = Files.deleteIfExists(Path.of("./BullsAndCows/playerSaves/" + this.currentPlayer.getUsername() + ".txt"));
                            }catch (IOException ignored){}
                            return true;
                        }
                        break;
                    }

                default:
                    System.out.println("\nInvalid Option!");
            }
        }
    }

    public char[] getGuess() {
        return Guess;
    }

    /*
    Gets the menu option selected and converts it into the corresponding stage in the gameplay loop

    @return The stage corresponding to the chosen option
     */
    private STAGE menu(){
        int option = menuOptions();
        switch(option){
            case 1:
                return STAGE.GAMESELECT;

            case 2:
                return STAGE.STATS;

            case 3:
                return STAGE.LEADERBOARD;

            case 4:
                currentPlayer = null;
                return STAGE.LOGIN;

            case 5:
                return STAGE.HOWTOPLAY;

            case 6:
                return STAGE.EXIT;

            default:
                System.out.println("\nSomething went wrong!");
                System.out.println("Exiting Game!");
                return STAGE.EXIT;
        }
    }

    /*
    Upon Signing in, prompts the user to select what they'd like to do

    @return The option chosen
    */
    private int menuOptions() {
        for (; ; ) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Play Game");
            System.out.println("2. View Stats");
            System.out.println("3. View Leaderboard");
            if(currentPlayer == null){
                System.out.println("4. Log In");
            }
            else {
                System.out.println("4. Log Out");
            }
            System.out.println("5. How To Play");
            System.out.println("6. Quit Game");
            System.out.print("\n>");

            String userInput = inputScanner.nextLine();

            switch(userInput){
                case "1":
                    return 1;

                case "2":
                    return 2;

                case "3":
                    return 3;

                case "4":
                    return 4;

                case "5":
                    return 5;

                case "6":
                    return 6;

                default:
                    System.out.println("\nInvalid Input!");
            }
        }
    }

    //</editor-fold>

    //<editor-fold desc="Guess Options">

    /*
    Gets the input when guessing a code, and calls appropriate actions based on input
    */
    private void guessOptions() {
        for (; ; ) {
            System.out.println("\nFor a list of valid commands, type \"/help\"");
            System.out.print("\n>");

            String userInput = inputScanner.nextLine();

            switch (userInput) {
                case "/submit":
                    if (submit()) {return;}
                    break;

                case "/help":
                    printCommands();
                    break;

                case "/giveup":
                    if (giveUp()){
                        return;
                    }
                    break;

                case "/save":
                    if(save()){return;}
                    break;
                case "/hint":
                    System.out.println(this.getHint());
                    break;

                case "/undo":
                    undoConfirmation();
                    break;

                default:
                    changeGuess(userInput);
            }
        }
    }

    boolean hint(){
        return true;
    }


    /*
    Attempts to change one of the guess positions

    @param userInput    The User's input string
     */
    private void changeGuess(String userInput){
        ArrayList<String> completedChanges = inputGuessChange(userInput);
        if (completedChanges.isEmpty()) {
            System.out.println("\nNo changes completed, maybe input had incorrect format?");
        } else {
            System.out.println("\nChanges completed: " + completedChanges);
        }
    }

    /*
    Checks if a user is logged in before trying to save the current code game

    @return If the save was successful or not
     */
    private boolean save(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("./BullsAndCows/playerSaves/" + this.currentPlayer.getUsername() + ".txt"));
            if (br.readLine() != null) {
                System.out.println("overwrite save?(y/n)");
                while(true){
                    String input = inputScanner.nextLine();
                    if(Objects.equals(input, "y")){
                        break;
                    }
                    else if(Objects.equals(input, "n")){
                        return true;
                    }
                    else{
                        System.out.println("Invalid input. Overwrite save? (y/n)");
                    }
                }
            }
        } catch (IOException ignored) {
        }

        if(currentPlayer == null){
            System.out.println("\nTo save a game, you must first create an account, or login to an existing one");
            return false;
        }
        else if(saveGame()){
            System.out.println("\nGame saved for later!");
            return true;
        }
        else{
            System.out.println("\nSomething went wrong saving your game!");
            return false;
        }
    }

    /*
    A short method that checks if the user actually wants to give up, and prints a message if so

    @return If the user wants to give up
     */
    private boolean giveUp(){
        if(giveUpConfirmation()){
            System.out.println("\nOh well. Better luck next time!");
            System.out.println("The code was: " + showSolution());
            return true;
        }
        return false;
    }

    /*
    Just a short method that checks if a guess was correct, then preforms appropriate actions.

    @return If the guess was correct
     */
    private boolean submit(){
        if (submitGuess()) {
            System.out.println("\nCongratulations! Your code was correct!");
            if (currentPlayer != null)
                currentPlayer.incrementCodesDeciphered();
            return true;
        }
        return false;
    }

    //</editor-fold>

    //<editor-fold desc="Account Actions">

    /*
    Gives the user the option to log-in.

    @return The next stage in the program (either exit or menu)
    */
    private STAGE requestLogin() {
        for (;;) {
            System.out.println("\nPlease choose one of the following:");
            System.out.println("1. Log in");
            System.out.println("2. Create account");
            System.out.println("3. Continue as guest");
            System.out.println("4. Quit");
            System.out.print("\n>");
            String option = inputScanner.nextLine();

            switch (option) {
                case "1":
                    if (logIn()) {
                        System.out.println("\nWelcome " + currentPlayer.getUsername() + "!");
                        return STAGE.MENU;
                    }
                    break;

                case "2":
                    if (createAccount()) {
                        System.out.println("\nWelcome " + currentPlayer.getUsername() + "!");
                        return STAGE.MENU;
                    }
                    break;

                case "3":
                    return STAGE.MENU;

                case "4":
                    return STAGE.EXIT;

                default:
                    System.out.println("Invalid option!");

            }
        }
    }

    /*
    Gives the user the option to either back out to 'requestLogin' or try and login to an existing account.

    @return If the user has logged into an existing account
    */
    private boolean logIn(){
        for(;;){
            System.out.println("\nEnter your username or type \"back\" to return:");
            System.out.print("\n>");
            String userInput = inputScanner.nextLine();

            if(userInput.equals("back")){
                return false;
            } else {
                Player logInAttempt = allPlayers.findPlayer(userInput);

                if (logInAttempt != null){
                    currentPlayer = logInAttempt;
                    System.out.println("\nLog in successful!");
                    return true;
                }
                else{
                    System.out.println("\nInvalid Username!");
                }
            }
        }
    }

    /*
    Prompts the user to create an account and checks if the given username is valid

    @return If the account was successfully created
    */
    private boolean createAccount(){
        for(;;) {
            System.out.println("\nPlease enter the username you would like, or \"back\" to return:");
            System.out.print("\n>");
            String newUsername = inputScanner.nextLine();

            if(newUsername.equals("back")){
                return false;
            }
            else {
                if (allPlayers.findPlayer(newUsername) == null) {
                    newAccount(newUsername);
                    System.out.println("\nAccount created!");
                    return true;
                } else {
                    System.out.println("\nThis username is already taken!");
                }
            }
        }
    }

    /*
    Creates a new instance of the player account and adds it to the list of players

    @param newUsername  The username of the account to be created
    */
    private void newAccount(String newUsername){
        currentPlayer = new Player(newUsername);
        allPlayers.addPlayer(currentPlayer);
    }

    /*
    Logs player out by setting the user to null
     */
    private void logOut(){
        currentPlayer = null;
    }

    //</editor-fold>

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
   Requests the user to select one of the two game-types
   */
    public void gameSelection(){
        boolean check = true;
        while(check){
            //inputScanner = new Scanner(System.in);
            System.out.println("What type of game do you want to play? number or letter?");
            String input = inputScanner.nextLine().toLowerCase();
            switch(input){
                case "help":
                    printHelp();
                    break;
                case "number":
                    codeGame = new NumbersCode(CODELENGTH);
                    check = false;
                    break;
                case "letter":
                    codeGame = new LettersCode(CODELENGTH);
                    check = false;
                    break;
                //Case purely for testing
                case "letterinvalidfile":
                    codeGame = new LettersCode(CODELENGTH);
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
        System.out.println("""
                Possible commands
                /number select number game
                /letter select letter game
                /help shows list of commands""");
    }

    /*
    Helper function for confirming the user wishes to give up

    @return If the user wants to give up
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

    void requestCode(){

    }

    //<editor-fold desc="Guess Actions">

    /*
    Formats the array as a string to output
    @return formatted string of Guess array
     */
    String showGuess() {

        String formattedGuess = "";

        for(int i = 0; i < CODELENGTH; i++){
            formattedGuess = formattedGuess + "[ " + this.Guess[i] + " ] ";
        }
        return formattedGuess;
    }

    /*
    Resets the current guess for making a new guess
     */
    void resetGuess() {
        this.Guess = new char[CODELENGTH];
        this.preChangeGuess = new char[CODELENGTH];

        for(int i = 0; i < CODELENGTH; i++){
            this.Guess[i] = '\0';
            this.preChangeGuess[i] = '\0';
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
            int inputChangePos;
            try {
                inputChangePos = Integer.parseInt(String.valueOf(userInput.charAt(i + 1))) - 1;
            }
            catch(NumberFormatException e){
                System.out.println("\nInput not recognised as a command or guess, try /help to see instructions and try again.");
                return new ArrayList<>();
            }

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
        if (position < 0 || position >= CODELENGTH) {
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
        if(validateGuessInput()){
            int[] bullsCows = codeGame.compareCode(Guess);
            System.out.println("Bulls: " + bullsCows[0]);
            System.out.println("Cows: " + bullsCows[1]);

            if(currentPlayer != null) {
                currentPlayer.updateBulls(bullsCows[0]);
                currentPlayer.updateCows(bullsCows[1]);
                currentPlayer.incrementGuesses();
            }
            return bullsCows[0] == CODELENGTH;
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
       Checks the Guess for empty positions and duplicate values

       @return true if Guess contains none of the above
       */
    private boolean validateGuessInput(){

        for(int i = 0; i<CODELENGTH; i++){

            // Checks for missing value
            if(Guess[i] == '\0'){
                System.out.println("Missing value at position " + i);
                return false;
            }

            // Checks for duplicate values
            for (int j = i + 1; j<CODELENGTH; j++) {
                if (Guess[i] == Guess[j]) {
                    System.out.println("Duplicate value present at positions " + i + " & " + j);
                    return false;
                }
            }
        }

        return true;
    }

    /*
   Helper function for confirming the user wishes to undo guess

   @return If user wants to undo guess
    */
    private boolean undoConfirmation() {
        while (true) {
            Scanner undoScan = inputScanner;
            System.out.println("\nWhat position of your guess do you wish to undo? >>>");

            String undoPos = undoScan.nextLine();
            if (undoPos.compareTo("") == 0) {
                return false;
            }

            if (Integer.parseInt(undoPos) >= 1 && Integer.parseInt(undoPos) <= CODELENGTH) {
                undoGuess(Integer.parseInt(undoPos) - 1);
                return true;
            } else {
                System.out.println("\nError: Please choose a position between 1 and" + CODELENGTH + ", or press ENTER to exit\n");
            }
        }
    }

    /*
    Show the user a random letter / number from the secret code, unless they have used all their hints
    (they are allowed half the code length of hints)
    @return String with hint, or letting the user know they have no hints left
     */
    private String getHint() {
        Random rand = new Random();
        int randPos, counter = 0;

        do {
            randPos = rand.nextInt(CODELENGTH);
            counter++;
        } while (this.hintsUsed[randPos] && counter <= (CODELENGTH/2));

        if (counter > (CODELENGTH/2)) {
            return "\nYou have ran out of hints!";
        }

        this.hintsUsed[randPos] = true;
        return "\nHINT: Position " + (randPos + 1) + " is [ " + this.codeGame.getCode()[randPos] + " ]";
    }

    //</editor-fold>

    //<editor-fold desc="Saving and Loading codes">

    /*
    Saves the current secret code game for user to try again later, then end game
     */
    private boolean saveGame(){
        final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd;HH:mm:ss");
        final String playerSavePath = "./BullsAndCows/playerSaves/" + this.currentPlayer.getUsername() + ".txt";

        try {
            FileOutputStream fileOut = new FileOutputStream(playerSavePath, false);
            fileOut.write(LocalDateTime.now().format(CUSTOM_FORMATTER).getBytes());
            fileOut.write((";" + showSolution() + "\n").getBytes());
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
        ArrayList<String> readLines = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(playerSavePath));        // this file will always exist when a player is created
            while (scanner.hasNextLine()) {
                readLines.add(scanner.nextLine());
            }
            scanner.close();

            if (readLines.isEmpty()) {
                System.out.println("\nNo saved codes for this user!");
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

    /*
    Attempts to load a stored game

    @returns If the load was successful
    */
    private boolean confirmLoadGame(ArrayList<String> lines) {

        ArrayList<String[]> splitCodes = splitSaveInfo(lines);

        StringBuilder loadOptions = getLoadOptions(splitCodes);

        while (true) {
            System.out.println("\nGames saved for user: " + this.currentPlayer.getUsername());
            System.out.println("\n" + loadOptions + "\n");

            Scanner loadScan = inputScanner;
            System.out.println("\nWhat game would you like to load? type \"back\" to return >>>");

            String loadIndex = loadScan.nextLine();
            if (loadIndex.compareTo("back") == 0) {
                System.out.println("\nLoad game cancelled.");
                return false;
            }

            try {
                int i = (Integer.parseInt(loadIndex)-1);
                if (i >= 0 && i <= (splitCodes.size()-1)) {
                    System.out.println("\nLoading game from " + splitCodes.get(i)[0]);
                    char[] code = convertToCode(splitCodes.get(i)[2]);

                    if (isNumeric(code)) {
                        this.codeGame = new NumbersCode(code);
                        System.out.println("\nNumber code loaded");
                    } else {
                        this.codeGame = new LettersCode(code);
                        System.out.println("\nLetter code loaded");
                    }

                    return true;
                } else {
                    System.out.println("\nOption out of range. Please select a valid option.\n");
                }
            }
            catch(NumberFormatException e){
                System.out.println("\nInput contains non-numeric values!");
            }
        }
    }

    /*
    Converts the saved string format of the code into the character array

    @returns    The code in the usable format

    @param oldCode  The code string to be converted
     */
    private char[] convertToCode(String oldCode){
        String code = oldCode.replace("[","");
        code = code.replace("]","");
        code = code.replace(",","");
        code = code.replace(" ","");
        return code.toCharArray();
    }

    /*
    Splits the saved code info into parts based on a common dellimiter

    @returns    All parts of all the saved codes

    @param codeList The list of single String code info to be split
     */
    private ArrayList<String[]> splitSaveInfo(ArrayList<String> codeList){
        ArrayList<String[]> splitCodeList = new ArrayList<String[]>();
        for(String code : codeList){
            String[] items = code.split(";");
            splitCodeList.add(items);
        }
        return splitCodeList;
    }

    /*

    */
    private StringBuilder getLoadOptions(ArrayList<String[]> lines) {
        StringBuilder loadOptions = new StringBuilder();
        String codeType;
        int loadCount = 0;

        for (int i = 0; i < lines.size(); i += 2) {
            loadCount += 1;

            if (isNumeric(convertToCode(lines.get(i)[2]))) {
                codeType = "Number Code";
            } else {
                codeType = "Letter Code";
            }

            String gameSavedLine = "(" + loadCount + ") Game Saved - " + lines.get(i)[0] + " | " + lines.get(i)[1] + " (" + codeType + ")\n";
            loadOptions.append(gameSavedLine);
        }

        return loadOptions;
    }

    private boolean isNumeric(char[] str) {
        return str[0] > '0' && str[0] < '9';
    }

    //</editor-fold>

    /*
    Gets solution from secret code class and returns

    @return solution as String
    */
    private String showSolution(){
        return Arrays.toString(this.codeGame.getCode());
    }


    //<editor-fold desc="Display Statistics">

    /*
    Prints the current player's details and waits for the user to hit enter before returning to the menu
    */
    private void displayStats(){
        if(currentPlayer == null){
            System.out.println("\nLogin to view statistics");
            return;
        }

        boolean status = print_player_details(currentPlayer);
        /*
        try {
            System.in.read();
        }
        catch (IOException e){
            System.out.println("\nProgram has encountered a problem!");
            System.out.println("Returning to menu...");
        }
        */
    }

    /*
    Prints the leaderboard (the top 10 players) and waits for the user to hit enter before returning to the menu
     */
    private void displayLeaderBoard(){
        print_top_ten();
        try {
            System.in.read();
        }
        catch (IOException e){
            System.out.println("\nProgram has encountered a problem!");
            System.out.println("Returning to menu...");
        }
    }

    public boolean print_player_details_testing(Player p){
        return print_player_details(p);
    }

    /*
    Prints all of a user's details

    @param p    The player to be printed
     */
    private boolean print_player_details(Player p){
        if(p.getGuesses() == 0){
            System.out.println("Try playing a game first");
            return false;
        }
        System.out.println("USERNAME:        " + p.getUsername());
        System.out.println("CODES ATTEMPTED: " + p.getCodesAttempted());
        System.out.println("CODES COMPLETED: " + p.getCodesDeciphered());
        System.out.println("BULLS:           " + p.getBulls());
        if(isNaN(p.bullsAccuracy())){
            System.out.println("ACCURACY:        N/A");
        }
        else{
            System.out.print("ACCURACY:        "+new DecimalFormat("#.##").format(p.bullsAccuracy()) + "%\n");
        }

        System.out.println("COWS:            " + p.getCows());

        if(isNaN(p.cowsAccuracy())){
            System.out.println("ACCURACY:        N/A");
        }
        else{
            System.out.print("ACCURACY:        "+ new DecimalFormat("#.##").format(p.cowsAccuracy()) + "%\n");
        }

        System.out.println();
        return true;
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

    //</editor-fold>
}