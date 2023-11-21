package PIR;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Event extends PIRInterface implements Serializable {
    public static final String dateFormat = "yyyyMMdd HHmm";
    protected static int nextId = 1;

    public Event() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 10);
        TITLES.put("Description", 20);
        TITLES.put("Starting Time", 19);
        TITLES.put("Alarm", 19);

        Utils.cls();
        Scanner scanner = new Scanner(System.in);
        data = new String[4];
        System.out.print("Title: ");
        data[0] = scanner.nextLine();
        System.out.print("Description: ");
        data[1] = scanner.nextLine();

        System.out.printf("Starting time in format %s: ", dateFormat);
        Date alarm;
        try {
            Date startingTime = new SimpleDateFormat(dateFormat).parse(scanner.nextLine());
            data[2] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startingTime);

            System.out.printf("Alarm in format %s: ", dateFormat);
            alarm = new SimpleDateFormat(dateFormat).parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Invalid Time format");
            return;
        }

        data[3] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(alarm);

        Utils.cls();
        System.out.println("The event is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    public Event(String title, String description, String startingTime, String alarm) {
        // Initialize TITLES
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 10);
        TITLES.put("Description", 20);
        TITLES.put("Starting Time", 19);
        TITLES.put("Alarm", 19);

        // Set data directly
        data = new String[]{title, description, startingTime, alarm};

        // Set ID
        this.ID = nextId++;
    }

    public static void setNextId(int nextId) {Text.nextId = nextId;}
}
