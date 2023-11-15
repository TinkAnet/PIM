import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class PIMInterface {
    abstract int getID();

    abstract Map<String, Integer> getTitles();
    abstract String[] getData();
    abstract void setData(String[] data);
}

class Contact extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private int ID;
    private String[] data;
    public static final Map<String, Integer> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);
    }

    public Contact() {
        Utils.cls();
        Scanner scanner = new Scanner(System.in);
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
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String[] getData() {return data;}
    public void setData(String[] data){this.data = data;}

    // 其他属性的 getters 和 setters
    @Override
    public Map<String, Integer> getTitles() {
        return TITLES;
    }
    public static void setNextId(int nextId) {
        Contact.nextId = nextId;
    }
}

class Task extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private int ID;
    private String[] data;
    public static final Map<String, Integer> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("Title", 20);
        TITLES.put("Description", 50);
        TITLES.put("DueDate", 19);
    }

    public Task() {
        Scanner scanner = new Scanner(System.in);
        data = new String[3];
        System.out.print("Title: ");
        data[0] = scanner.nextLine();
        System.out.print("Description: ");
        data[1] = scanner.nextLine();
        System.out.println("DueDate in format dd-MM-yyyy");
        data[2] = scanner.nextLine();
        Utils.cls();
        System.out.println("The task is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }
    @Override
    public Map<String, Integer> getTitles() {
        return TITLES;
    }
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String[] getData() {return data;}
    public void setData(String[] data){this.data = data;}

    public static void setNextId(int nextId) {
        Task.nextId = nextId;
    }
}

class Text extends PIMInterface implements Serializable {
    // STATIC variable to keep track of the next available ID
    private static int nextId = 1;
    private int ID;
    private String[] data;

    public static final Map<String, Integer> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("Title", 20);
        TITLES.put("Note", 50);
    }

    public Text() {
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
    @Override
    public Map<String, Integer> getTitles() {return TITLES;}

    @Override
    public int getID() {return ID;}
    @Override
    public String[] getData() {return data;}
    @Override
    public void setData(String[] data){this.data =data;}

    public static void setNextId(int nextId) {Text.nextId = nextId;}
}

class Event extends PIMInterface implements Serializable {
    private static int nextId = 1;

    private final int ID;

    private String[] data;
    public static final Map<String, Integer> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("Title", 10);
        TITLES.put("Description", 20);
        TITLES.put("Starting Time", 19);
        TITLES.put("Alarm", 19);
    }
    public Event() {
        Scanner scanner = new Scanner(System.in);
        data = new String[4];
        System.out.print("Title: ");
        data[0] = scanner.nextLine();
        System.out.print("Description: ");
        data[1] = scanner.nextLine();
        System.out.print("Starting time in format HH:mm dd-MM-yyyy: ");
        data[2] = scanner.nextLine();
        System.out.print("Alarm in format HH:mm dd-MM-yyyy: ");
        data[3] = scanner.nextLine();
        
        /*try {
            data[2] = sdf.parse(scanner.nextLine());
            System.out.print("Alarm in format HH:mm dd-MM-yyyy: ");
            data[3] = sdf.parse(scanner.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Invalid date format. Please enter the date in the format HH:mm dd-MM-yyyy.");
        }*/
        Utils.cls();
        System.out.println("The event is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }
    public Map<String, Integer> getTitles(){return TITLES;}
    @Override
    public int getID() {
        return ID;
    }

    public void setData(String[] data){this.data = data;}
    @Override
    public String[] getData() {
        return data;
    }
    public static void setNextId(int nextId) {
        Event.nextId = nextId;
    }
}
