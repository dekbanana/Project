/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5;


/**
 *
 * @author NOOM
 */
public class Key_Generator {
    
    public static String [] genKey(String key){
        
        String keygen[] = new String [16];
        int ParityDropTable[] =  {  
            57, 49, 41, 33, 25, 17, 9, 1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 27, 19, 11, 3,
            60, 52, 44, 36, 63, 55, 47, 39,
            31, 23 , 15, 7, 62, 54, 46, 38,
            30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 28, 20, 12, 4 
        };
        
        int KeyCompressionTable[] = {
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
        };
        
        int ShiftTable[] = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
        
        String dropKey = permute(64, 56, hextoBin(key), ParityDropTable); // เปลี่ยนค่าจาก 64 เหลือ 56 bit พร้อมสลับตำแหน่ง
        String left = dropKey.substring(0, 28);                 // เก็บค่าฝั่งซ้าย
        String right = dropKey.substring(28, dropKey.length());  // เก็บค่าฝั่งขวา
  
        for(int round=1; round<=16; round++){
            left = shiftLeft(left, ShiftTable[round-1]);
            right = shiftLeft(right, ShiftTable[round-1]);
            keygen[round-1] = bintoHex(permute(56, 48, combine(28, 56, left, right), KeyCompressionTable));
        }
        return keygen;
    }
  
    public static String permute(int numIn, int numOut, String plan, int [] swap){
        String out = "";
        if(numIn == plan.length()){
            for(int i=0; i<numOut; i++){
                out += plan.charAt(swap[i]-1);
            }
        }
        return out;
    }
    
    public static String hextoBin(String keys){  //เปลี่ยนเลขฐาน 16 มาเป็น String เลขฐานสอง 64 bit
        String bin = "";  // ประกาศเพื่อมาเก็บค่าเลขฐานสองของ ptext
        for(char key : keys.toCharArray()){
            String temp ="000"+Integer.toBinaryString(Integer.parseInt(key+"", 16));
            bin += temp.substring(temp.length()-4, temp.length());
        }
        return bin;
    }
    
    public static String bintoHex(String key){   // เปลี่ยน String binary 0110001010101000.... เป็น เลขฐาน 16
        String hex = "";
        for(int i=0; i<key.length(); i+=4){
            hex = hex+""+Integer.toHexString(Integer.parseInt(key.charAt(i)+""+key.charAt(i+1)+""+key.charAt(i+2)+""+key.charAt(i+3), 2));
        }
        return hex.toUpperCase();
    }
    
    public static String shiftLeft(String left,int num){
        char block[] = left.toCharArray();
        for(int i=0; i<num ;i++){
            char temp = block[0];
            for(int j=1; j<28; j++){
                block[j-1] = block[j];
            }
            block[27] = temp;
        }
        left = "";
        for(int i=0; i<28; i++){
            left = left+""+block[i];
        }
        return left;
    }
    
    public static String combine(int numIn, int numOut, String left, String right){
        String com = "";
        if(left.length()+right.length()==numOut){
            for(int i=0; i<numIn; i++){
                com=com+""+left.charAt(i);
            }
            for(int i=0; i<numIn; i++){
                com=com+""+right.charAt(i);
            }
        }
        return com;
    }
}
