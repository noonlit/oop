package Cinema.Util;

import java.util.Scanner;

public class InputOutput {
    Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    /**
     * Reads a string from the keyboard.
     */
    public String readLine() {
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
    public int readInt() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("An integer is expected.");
            scanner.next();
            return readInt();
        }
    }

    /**
     * Prints the given string to the console.
     *
     * @param string
     */
    public void println(String string) {
        System.out.println(string);
    }

    /**
     * Prints the given error string to the console.
     *
     * @param string
     */
    public void printErrln(String string) {
        System.err.println(string);
    }
}
