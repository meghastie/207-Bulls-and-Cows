package BullsAndCows;

import java.util.Random;

public abstract class SecretCode {
    final private char[] code;
    private boolean[] hintsUsed;
    private int lastHintPos;

    SecretCode(char[] savedCode){
        this.code = savedCode;
        this.hintsUsed = new boolean[savedCode.length];
        for(int i =0; i< savedCode.length; i++){
            hintsUsed[i] = false;
        }
    }
    SecretCode(int codeLength){
        this.code = generateCode(codeLength);
        this.hintsUsed = new boolean[codeLength];
        for(int i =0; i< codeLength; i++){
            hintsUsed[i] = false;
        }
    }

    public char[] getCode() {
        return code;
    }

    abstract char[] generateCode(int codeLength);

    /*
        Compares the passed guess to the secret code

        @param guess the 4 character guess being compared
        @return an array of two integers storing: the number of bulls [0]; the number of cows [1]
     */
    public int[] compareCode(char[] guess){
        int[] bullsCows = {0,0};

        for(int i=0; i< guess.length; i++){
            for(int j=0; j< guess.length; j++){
                if(guess[i] == code[j]){
                    if(i==j)
                        bullsCows[0]++;
                    else
                        bullsCows[1]++;
                }
            }
        }

        return bullsCows;
    }

    /*
    Show the user a random letter / number from the secret code, unless they have used all their hints
    (they are allowed half the code length of hints)

    @return String with hint, or letting the user know they have no hints left
     */
    public String getHint() {
        Random rand = new Random();
        int randPos, counter = 0, codeLength = this.code.length;

        do {
            randPos = rand.nextInt(codeLength);
            counter++;
        } while (this.hintsUsed[randPos] && counter <= (codeLength/2));

        if (counter > (codeLength/2)) {
            return "\nYou have ran out of hints!";
        }

        this.hintsUsed[randPos] = true;
        lastHintPos = randPos;
        return "\nHINT: Position " + (randPos + 1) + " is [ " + this.code[randPos] + " ]";
    }

    public int getLastHintPos() {
        return lastHintPos;
    }

    public boolean[] getHintsUsed() {
        return hintsUsed;
    }
}
