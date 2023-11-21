package PIR;

import java.io.*;
import java.util.*;

public class Utils {
    public static void cls() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                //  Unix/Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("An error occurred while trying to clear the screen");
        }
    }

    public static void ptc() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\n[Press any key to continue...]");
        scanner.nextLine();
    }
}
