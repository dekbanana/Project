/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5;

/**
 *
 * @author NOOM
 */
public class DES {
    
    public static void main(String args[]){
        
        String keyInput = "AABB09182736CCDD";
        String keygen[] = Key_Generator.genKey(keyInput);
        String plaintext = "123456ABCD132536";
        System.out.println("Plaintext : "+plaintext+"\tKey : "+keyInput);
        System.out.println("key bin : "+Key_Generator.hextoBin(keygen[0]));
        plaintext = Key_Generator.hextoBin(plaintext);
        System.out.println("bin plain : "+plaintext);
        
        int iInitial[] = {58,50,42,34,26,18,10,2,
                          60,52,44,36,28,20,12,4,
                          62,54,46,38,30,22,14,6,
                          64,56,48,40,32,24,16,8,
                          57,49,41,33,25,17,9,1,
                          59,51,43,35,27,19,11,3,
                          61,53,45,37,29,21,13,5,
                          63,55,47,39,31,23,15,7
        };
        int iFinal[] = {40,8,48,16,56,24,64,32,
                        39,7,47,15,55,23,63,31,
                        38,6,46,14,54,22,62,30,
                        37,5,45,13,53,21,61,29,
                        36,4,44,12,52,20,60,28,
                        35,3,43,11,51,19,59,27,
                        34,2,42,10,50,18,58,26,
                        33,1,41,9,49,17,57,25 
        };
        
        String plainBlock = Key_Generator.permute(64, 64, plaintext, iInitial);
        System.out.println("plan per  : "+plainBlock);
        
        
        String left = plainBlock.substring(0,32);
        String right = plainBlock.substring(32, 64);
        System.out.println("left : "+left);
        System.out.println("right : "+right);
        
        
        for(int round=1; round<=16; round++){
            left = mixer(left, right, keygen[round-1]);
            System.out.println("Mixer : "+left);
            if(round!=16){
                String t = left;
                left = right;
                right = t;
            }
            System.out.printf("round %d \t\t %s \t %s \t %s",round,Key_Generator.bintoHex(left),Key_Generator.bintoHex(right),keygen[round-1]);
            
            System.out.println("");
        }
        String out = Key_Generator.combine(32, 64, left, right);
        System.out.println(out);
        System.out.println(Key_Generator.bintoHex(out));
        out = Key_Generator.permute(64, 64, out, iFinal);
        System.out.println(out);
        System.out.println(Key_Generator.bintoHex(out));
        
    }
    
    public static String mixer(String left, String right, String key){
        key = Key_Generator.hextoBin(key);  // เปลี่ยน key จากฐาน 16 เป็น ฐาน 2
        String temp = function(right, key);
        System.out.println("temp : "+temp);
        String output = exclusiveOr(temp, left);
        System.out.println("outex : "+output);
        return output;
    }
    
    public static String function(String right, String key){
        int expansionPermutation[] = {
            32,1,2,3,4,5,
            4,5,6,7,8,9,
            8,9,10,11,12,13,
            12,13,14,15,16,17,
            16,17,18,19,20,21,
            20,21,22,23,24,25,
            24,25,26,27,28,29,
            28,29,30,31,32,1
        };
        int straightPermutationtable[] = {
            16,7,20,21,29,12,28,17,
            1,15,23,26,5,18,31,10,
            2,8,24,14,32,27,3,9,
            19,13,30,6,22,11,4,25
        };
        
        String Pbox = Key_Generator.permute(32, 48, right, expansionPermutation);
        System.out.println("expa: "+Pbox);
        System.out.println("key : "+key);
        String XOR = exclusiveOr(Pbox, key);
        System.out.println("XOR : "+XOR);
        String Sbox = substitute(XOR);
        System.out.println("Sbox : "+Sbox);
        String output = Key_Generator.permute(32, 32, Sbox, straightPermutationtable);
        System.out.println("out : "+output);
        return output;
    }
    
    public static String exclusiveOr(String plan, String key){  // 
        String sol = "";
        for(int i=0; i<plan.length(); i++){
            sol+=plan.charAt(i)^key.charAt(i);
                
          
        }
        return sol; 
    }
    
    private static String substitute(String in){   //S-Box
        int substitutionTables[][][] = {
            {
                {14 ,4 ,13 ,1 ,2 ,15 ,11 ,8 ,3 ,10 ,6 ,12 ,5 ,9 ,0 ,7},
                {0 ,15 ,7 ,4 ,14 ,2 ,13 ,1 ,10 ,6 ,12 ,11 ,9 ,5 ,3 ,8},
                {4 ,1 ,14 ,8 ,13 ,6 ,2 ,11 ,15 ,12 ,9 ,7 ,3 ,10 ,5 ,0},
                {15, 12, 8, 2, 4, 9, 1, 7 ,5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12 ,0, 5, 1},
                {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9 ,11, 5},
                {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                {13 ,8 ,10 ,1 ,3 ,15 ,4 ,2 ,11 ,6 ,7 ,12 ,0 ,5 ,14 ,9}
            },
            {
                {10 ,0 ,9 ,14 ,6 ,3 ,15 ,5 ,1 ,13 ,12 ,7 ,11 ,4 ,2 ,8},
                {13, 7, 0, 9, 3, 4, 6, 10, 2 ,8 ,5 ,14, 12, 11, 15, 1},
                {13, 6, 4, 9, 8 ,15 ,3 ,0, 11, 1, 2, 12, 5, 10, 14, 7},
                {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            {
                {7 ,13 ,14 ,3 ,0 ,6 ,9 ,10 ,1 ,2 ,8 ,5 ,11 ,12 ,4 ,15},
                {13 ,8 ,11 ,5, 6 ,15, 0 ,3 ,4 ,7 ,2 ,12 ,1 ,10 ,14 ,9},
                {10, 6, 9, 0, 12, 11, 7 ,13, 15, 1, 3, 14, 5, 2, 8, 4},
                {3, 15, 0, 6, 10, 1, 13, 8, 9, 4 ,5 ,11 ,12, 7, 2, 14}
            },
            {
                {2 ,12 ,4 ,1 ,7 ,10 ,11 ,6 ,8 ,5 ,3 ,15 ,13 ,0 ,14 ,9},
                {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3 ,9 ,8 ,6},
                {4 ,2, 1, 11, 10, 13, 7 ,8 ,15, 9, 12, 5, 6, 3, 0, 14},
                {11, 8, 12 ,7, 1, 14 ,2 ,13 ,6 ,15, 0 ,9, 10, 4, 5, 3}
            },
            {
                {12 ,1 ,10 ,15, 9 ,2, 6 ,8 ,0 ,13 ,3 ,4 ,14 ,7 ,5 ,11},
                {10, 15, 4 ,2 ,7 ,12 ,9, 5, 6, 1, 13 ,14 ,0, 11, 3 ,8},
                {9, 14, 15, 5, 2 ,8 ,12, 3, 7 ,0 ,4 ,10, 1, 13 ,11 ,6},
                {4, 3, 2, 12, 9, 5, 15 ,10 ,11, 14, 1, 7, 6, 0, 8, 13}
            },
            {
                {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7 ,5 ,10 ,6 ,1},
                {13, 0, 11, 7, 4, 9, 1, 10, 14 ,3 ,5, 12, 2, 15, 8, 6},
                {1, 4, 11, 13, 12, 3, 7 ,14, 10, 15, 6, 8, 0, 5, 9, 2},
                {6, 11, 13, 8 ,1 ,4, 10, 7, 9, 5 ,0 ,15 ,14, 2, 3, 12}
            },
            {
                {13, 2, 8, 4, 6, 15, 11, 1 ,10, 9, 3, 14, 5, 0, 12, 7},
                {1, 15, 13, 8 ,10, 3 ,7 ,4 ,12, 5, 6 ,11, 0, 14, 9, 2},
                {7, 11, 4, 1 ,9 ,12 ,14 ,2 ,0 ,6 ,10, 13 ,15 ,3 ,5 ,8},
                {2, 1, 14, 7 ,4 ,10, 8 ,13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
        };

        char out[] = new char[32];
        for(int i=0;i<=7;i++){
            int row = 2*Integer.parseInt(in.charAt(i*6)+"")+Integer.parseInt(in.charAt(i*6+5)+"");
            int col = 8*Integer.parseInt(in.charAt(i*6+1)+"")+4*Integer.parseInt(in.charAt(i*6+2)+"")+2*Integer.parseInt(in.charAt(i*6+3)+"")+Integer.parseInt(in.charAt(i*6+4)+"");
            int value = substitutionTables[i][row][col];
            out[i*4] = Character.forDigit(value/8,10); value%=8;
            out[i*4+1] = Character.forDigit(value/4,10); value%=4;
            out[i*4+2] = Character.forDigit(value/2,10); value%=2;
            out[i*4+3] = Character.forDigit(value,10); 
        }
        in = "";
        for(int i=0;i<out.length;i++)
            in+=out[i];
        return in;
    }    
    
}
