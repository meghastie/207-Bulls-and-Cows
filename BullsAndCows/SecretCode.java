package BullsAndCows;

public abstract class SecretCode {
    private char[] code;

    SecretCode(char[] savedCode){
        this.code = savedCode;
    }
    SecretCode(){
        //this.code = generateCode();
    }

    abstract char[] generateCode();

    /*
        Compares the passed guess to the secret code

        @param guess the 4 character guess being compared
        @return an array of two integers storing: the number of bulls [0]; the number of cows [1]
     */
    public int[] compareCode(char[] guess){
        int[] bullsCows = {0,0};

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
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
}
