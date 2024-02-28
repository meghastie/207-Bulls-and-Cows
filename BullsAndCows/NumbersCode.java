package BullsAndCows;

import java.util.Random;

public class NumbersCode extends SecretCode {
    int numCode;
    public NumbersCode() {
    }


    private String generateNumber() {
        Random rand = new Random();
        int min = 1000;
        int max = 9999;

        numCode = rand.nextInt(max - min + 1) + min; //generates random 4 digit number

        String code = "NULL";
        while (hasDuplicateCharacter(Integer.toString(numCode))) {
            numCode = rand.nextInt(max - min + 1) + min;
            code = Integer.toString(numCode);               //converts integer to string
        }
        return code;
    }

    /*
    Check if a given secret code has duplicate letter or number
     */
    public static boolean hasDuplicateCharacter(String code) {
        for (int i = 0; i < code.length(); i++) {
            for (int j = i + 1; j < code.length(); j++) {
                if (code.charAt(i) == code.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }
}