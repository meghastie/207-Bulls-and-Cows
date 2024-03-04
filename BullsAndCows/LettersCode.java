package BullsAndCows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LettersCode extends SecretCode{
    final String filePath = "./BullsAndCows/words.txt";
    private List<String> word; //holds the 15 options of words from file
    //String gameWord the random word chosen for the game
    //code storage is already part of secret code


    //words from the file are parsed into the arrayList 'word'
    public LettersCode(){
        super();
    }

    public LettersCode(char[] code){
        super(code);
    }

    //chooses a random index 0-14. the random index will correspond to one of the 15 words in the file
    @Override
    public char[] generateCode() {
        if(word == null){
            word = new ArrayList<>();
        }
        if (word.isEmpty()) {
            //System.out.println("Word arraylist is empty - please fill");
            populateWordList();
            //return new char[] {'N','U','L','L'};
        }

        Random rand = new Random();
        int randIndex = rand.nextInt(word.size());

        code = word.get(randIndex).toCharArray();
        return code;
    }

    public List<String> getWord(){
        return word;
    }

    private void populateWordList(){
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNext()) {
                String line = scanner.next();
                word.add(line.trim());
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found, exiting program");
            System.exit(0);
        }
    }

    public static void populateWordList(String path) throws FileNotFoundException {
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNext()) {
                String line = scanner.next();
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found, exiting program");
            throw new FileNotFoundException();
        }
    }
}
