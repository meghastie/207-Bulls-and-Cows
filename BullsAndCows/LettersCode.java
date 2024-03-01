package BullsAndCows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LettersCode extends SecretCode{

    private List<String> word = new ArrayList<String>(); //holds the 15 options of words from file
    String gameWord; //the random word chosen for the game


    //words from the file are parsed into the arrayList 'word'
    public LettersCode(String file){
        gameWord = "NULL";
        try {
        Scanner scanner = new Scanner(new File(file));
        while (scanner.hasNext()) {
            String line = scanner.next();
            word.add(line.trim());
        }
        scanner.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
    }

    public LettersCode(char[] code){
        gameWord = new String (code);
    }

    public LettersCode(){
        gameWord = "NULL";
    }

    //chooses a random index 0-14. the random index will correspond to one of the 15 words in the file
    @Override
    public char[] generateCode() {

        if (word.isEmpty()) {
            System.out.println("Word arraylist is empty - please fill");
            return new char[] {'N','U','L','L'};
        }

        Random rand = new Random();
        int randIndex = rand.nextInt(word.size());
        gameWord = word.get(randIndex);

        return gameWord.toCharArray();
    }

    public List<String> getWord(){
        return word;
    }
}
