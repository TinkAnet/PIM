package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Text extends PIRInterface implements Serializable {
    protected static int nextId = 1;

    public Text() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 20);
        TITLES.put("Note", 50);

        Utils.cls();
        Scanner scanner = new Scanner(System.in);
        data = new String[2];
        System.out.print("Title: ");
        data[0] = scanner.nextLine();
        System.out.print("Note: ");
        data[1] = scanner.nextLine();
        Utils.cls();
        System.out.println("The text is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();

    }

    public Text(String title, String note) {
        // Initialize TITLES
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 20);
        TITLES.put("Note", 50);

        // Set data directly
        data = new String[]{title, note};

        // Set ID
        this.ID = nextId++;
    }

    public static void setNextId(int nextId) {
        Text.nextId = nextId;
    }
}
