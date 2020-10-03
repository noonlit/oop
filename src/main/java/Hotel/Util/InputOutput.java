package Hotel.Util;

import java.util.Scanner;

public class InputOutput {
    Scanner scanner = new Scanner(System.in);

    /**
     * Reads a string from the keyboard
     */
    public String readLine()
    {
        try {
            return scanner.next();
        } catch (Exception e) {
            System.out.println("A string is expected.");
            scanner.next();
            return readLine();
        }
    }

    /**
     * Reads an integer from the keyboard.
     */
    public int readInt()
    {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("An integer is expected.");
            scanner.next();
            return readInt();
        }
    }

}
