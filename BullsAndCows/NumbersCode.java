package BullsAndCows;

import java.util.Random;

public class NumbersCode extends SecretCode {
    private int numCode;
    public NumbersCode(int codeLength) {
        super(codeLength);
    }
    public NumbersCode(char[] code){
        super(code);
        numCode = Integer.parseInt(String.valueOf(code));
    }
    public char[] generateCode(int codeLength) {
        Random rand = new Random();
        int min = 10^(codeLength-1);
        int max = 10^(codeLength) - 1;

        numCode = rand.nextInt(max - min + 1) + min; //generates random 4 digit number
        char[] code = Integer.toString(numCode).toCharArray();
        //String code = "NULL";
        while (hasDuplicateCharacter(Integer.toString(numCode).toCharArray())) {
            numCode = rand.nextInt(max - min + 1) + min;
            code = Integer.toString(numCode).toCharArray();               //converts integer to string
        }
        return code;
    }

    /*
    Check if a given secret code has duplicate letter or number
     */
    public static boolean hasDuplicateCharacter(char[] code) {
        for (int i = 0; i < code.length; i++) {
            for (int j = i + 1; j < code.length; j++) {
                if (code[i] == code[j]) {
                    return true;
                }
            }
        }
        return false;
    }
}