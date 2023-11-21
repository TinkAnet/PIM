package PIR;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Task extends PIRInterface implements Serializable {
    public static final String dateFormat = "yyyyMMdd";
    protected static int nextId = 1;

    public Task() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 20);
        TITLES.put("Description", 50);
        TITLES.put("DueDate", 19);

        Scanner scanner = new Scanner(System.in);
        Utils.cls();
        data = new String[3];
        System.out.print("Title: ");
        data[0] = scanner.nextLine();
        System.out.print("Description: ");
        data[1] = scanner.nextLine();
        System.out.printf("DueDate in format %s: ", dateFormat);

        Date dueDate;
        try {
            dueDate = new SimpleDateFormat(dateFormat).parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Invalid Time format");
            return;
        }
        data[2] = new SimpleDateFormat("yyyy-MM-dd").format(dueDate);

        Utils.cls();
        System.out.println("The task is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    public Task(String title, String description, String dueDate) {
        // Initialize TITLES
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 20);
        TITLES.put("Description", 50);
        TITLES.put("DueDate", 19);

        // Set data directly
        data = new String[]{title, description, dueDate};

        // Set ID
        this.ID = nextId++;
    }

    public static void setNextId(int nextId) {
        Task.nextId = nextId;
    }
}
