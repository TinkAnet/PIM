import java.io.*;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Utils {
    public static void cls() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // 如果是 Windows 系统
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // 如果是 Unix/Linux/Mac 系统
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("An error occurred while trying to clear the screen");
        }
    }
}
