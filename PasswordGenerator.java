package MrEncrypter;

import java.util.Random;

public class PasswordGenerator {

    private char[] charTable;
    private Random rnd = new Random();
    public PasswordGenerator() {
        charTable = new char[26];
        char firstChar = 0x061;
        for (int i = 0; i < 26; i++) {
            charTable[i] = firstChar++;
        }
    }


    public String generate(int len, boolean isNum, boolean isUpperCase) {
        StringBuilder sb = new StringBuilder();
        int countNum = 0;
        int countUpperCase = 0;
        if (len > 4) {
            countNum = rnd.nextInt(len - 3);
            countUpperCase = rnd.nextInt(len - 3);
        }
        String temp = new String();
        for (int i = 0; i < len; i++) {
            if (countNum > 0 && isNum) {
                sb.append(Integer.toString(rnd.nextInt(10)).charAt(0));
                countNum--;
            } else if (countUpperCase > 0 && isUpperCase) {
                temp += charTable[rnd.nextInt(charTable.length)];
                sb.append(temp.toUpperCase());
                temp = "";
                countUpperCase--;
            } else {
                sb.append(charTable[rnd.nextInt(charTable.length)]);
            }
        }

        byte arr[] = sb.toString().getBytes();
        for (int i = 0; i < arr.length; i++) {
            int newInd = rnd.nextInt(arr.length);
            byte tempByte = arr[0];
            arr[0] = arr[newInd];
            arr[newInd] = tempByte;
        }
        return new String(arr);
    }

    public static void main(String[] args) {
        PasswordGenerator generator = new PasswordGenerator();
        String s = generator.generate(15, false, false);
        System.out.println("len: " + s.length());
        System.out.println(s);
    }
}
