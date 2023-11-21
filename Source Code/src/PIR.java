import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class PIRInterface implements Serializable{
    protected int ID;
    protected String[] data;
    protected Map<String, Integer> TITLES = new LinkedHashMap<>();

    public Map<String, Integer> getTitles() {return TITLES;}
    public String[] getData() {return data;}
    public void setData(String[] data) {this.data = data;}
    public static void setNextId(int nextId) {}
    public int getID() {return ID;}
}

class Contact extends PIRInterface implements Serializable {
    protected static int nextId = 1;
    public Contact() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);

        Utils.cls();
        Scanner scanner = new Scanner(System.in);
        data = new String[3];
        System.out.print("Name: ");
        data[0] = scanner.nextLine();
        System.out.print("Email: ");
        data[1] = scanner.nextLine();
        System.out.print("Phone number: ");
        data[2] = scanner.nextLine();
        Utils.cls();
        System.out.println("The contact is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    public Contact(String name, String email, String phoneNumber) {
        // 初始化 TITLES，与原构造函数类似
        TITLES = new LinkedHashMap<>();
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);

        // 直接设置数据
        data = new String[]{name, email, phoneNumber};

        // 设置 ID
        this.ID = nextId++;
    }

    public static void setNextId(int nextId) {Contact.nextId = nextId;}
}

class Task extends PIRInterface implements Serializable {
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
        System.out.printf("DueDate in format %s: ",dateFormat);

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

    public static void setNextId(int nextId) {Task.nextId = nextId;}
}

class Text extends PIRInterface implements Serializable {
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

    public static void setNextId(int nextId) {Text.nextId = nextId;}
}

class Event extends PIRInterface implements Serializable {
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
