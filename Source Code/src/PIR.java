import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class PIMInterface {
    abstract int getID();

    abstract Map<String, Integer> getTitles();
    abstract String[] getData();
    abstract void setData(String[] data);
    public abstract boolean matchesKeyword(String keyword);

    public static Map<Integer, PIMInterface> filterByKeyword(Collection<PIMInterface> items, String keyword) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            if (item.matchesKeyword(keyword)) {
                filteredItems.put(item.getID(), item);
            }
        }
        return filteredItems;
    }


    static void printWrappedText(String text, int lineLength) {
        String[] words = text.split(" ");
        StringBuilder lineBuilder = new StringBuilder();
        boolean firstWord = true;

        for (String word : words) {
            if (lineBuilder.length() + word.length() + (firstWord ? 0 : 1) > lineLength) {
                // Print the current line
                System.out.println(lineBuilder.toString());
                lineBuilder.setLength(0);  // Reset the lineBuilder
                firstWord = true;
            }

            if (!firstWord) {
                lineBuilder.append(" ");
            }
            lineBuilder.append(word);
            firstWord = false;
        }
        System.out.println(lineBuilder.toString());
    }
}

class Contact extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private int ID;
    private String name;
    private String email;
    private String phoneNumber;
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
        this.name = scanner.nextLine();
        System.out.print("Email: ");
        this.email = scanner.nextLine();
        System.out.print("Phone number: ");
        this.phoneNumber = scanner.nextLine();
        Utils.cls();
        System.out.println("The contact is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    @Override
    public boolean matchesKeyword(String keyword) {
        return false;
    }


    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String[] getData() { return new String[]{this.name, this.email, this.phoneNumber};}
    public void setData(String[] data){}

    // 其他属性的 getters 和 setters
    @Override
    public Map<String, Integer> getTitles() {
        return TITLES;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public static void setNextId(int nextId) {
        Contact.nextId = nextId;
    }
}

class Task extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private int ID;
    private String[] data;

    // Adjust format as necessary
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
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
    public boolean matchesKeyword(String keyword) {
        return false;
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
    public void setData(String[] data){this.d}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public static void setNextId(int nextId) {
        Task.nextId = nextId;
    }

    public static Map<Integer, PIMInterface> filterByDueDate(Collection<PIMInterface> items, Date searchDate, int option) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            if (item instanceof Task) {
                Task task = (Task) item;
                Date dueDate = task.getDueDate();

                switch (option) {
                    case 1: // Equal
                        if (dueDate.equals(searchDate)) {
                            filteredItems.put(task.getID(), item);
                        }
                        break;
                    case 2: // After
                        if (dueDate.after(searchDate)) {
                            filteredItems.put(task.getID(), item);
                        }
                        break;
                    case 3: // Before
                        if (dueDate.before(searchDate)) {
                            filteredItems.put(task.getID(), item);
                        }
                        break;
                }
            }
        }
        return filteredItems;
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
    public boolean matchesKeyword(String keyword) {
        return false;
    }

    public static Map<Integer, PIMInterface> UpdateByKeyword(Map<Integer, PIMInterface> copy, Map<Integer, PIMInterface> items ,String keyword, String mode) {
        Map<Integer, PIMInterface> result = new HashMap<>();

        if(mode.equals("and")){
            for (PIMInterface item : copy.values()) {
                String[] data = item.getData();
                if (data[0].toLowerCase().contains(keyword.toLowerCase()) || data[1].toLowerCase().contains(keyword.toLowerCase())) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(item.getID(), item);
                }
            }
        }else if(mode.equals("or")){
            for (PIMInterface item : copy.values()) {
                Text text = (Text) item;
                result.put(text.getID(), item);
            }

            for(PIMInterface item : items.values()){
                String[] data = item.getData();
                if (data[0].toLowerCase().contains(keyword.toLowerCase()) || data[1].toLowerCase().contains(keyword.toLowerCase())) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(item.getID(), item);
                }
            }

        }else if(mode.equals("not")){
            for (PIMInterface item : copy.values()) {
                String[] data = item.getData();
                if (!(data[0].toLowerCase().contains(keyword.toLowerCase()) || data[1].toLowerCase().contains(keyword.toLowerCase()))) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(item.getID(), item);
                }
            }
        }

        return result;
    



    @Override
    public Map<String, Integer> getTitles() {return TITLES;}


    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String[] getData() {return data;}
    @Override
    public void setData(String[] data){this.data =data;}

    public static void setNextId(int nextId) {
        Text.nextId = nextId;
    }
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

    @Override
    public boolean matchesKeyword(String keyword) {
        return false;
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
