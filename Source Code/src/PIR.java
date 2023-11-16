import java.io.Serializable;
import java.util.*;

abstract class PIMInterface {
    protected static int nextId = 1;
    protected int ID;
    protected String[] data;
    protected Map<String, Integer> TITLES;

    public Map<String, Integer> getTitles() {return TITLES;}
    public String[] getData() {return data;}
    public void setData(String[] data) {this.data = data;}
    public static void setNextId(int nextId) {PIMInterface.nextId = nextId;}
    public int getID() {return ID;}
}

class Contact extends PIMInterface implements Serializable {
    public Contact() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);

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
}

class Task extends PIMInterface implements Serializable {
    public Task() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 20);
        TITLES.put("Description", 50);
        TITLES.put("DueDate", 19);

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
}

class Text extends PIMInterface implements Serializable {
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
}

class Event extends PIMInterface implements Serializable {
    public Event() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 10);
        TITLES.put("Description", 20);
        TITLES.put("Starting Time", 19);
        TITLES.put("Alarm", 19);

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

        Utils.cls();
        System.out.println("The event is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }
}
