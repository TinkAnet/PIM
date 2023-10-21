import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class PIMInterface {
    abstract String getType();
    abstract int getID();
    abstract String getTitle();
    abstract Tuple getData();

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
    static void setNextID(int newID){}
}

class Contact extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private String type = "Contact";
    private int ID;
    private String name;
    private String email;
    private String phoneNumber;

    public Contact(Tuple data) {
        Object[] dataArray = data.getData();
        this.ID = (int) dataArray[0];
        this.name = (String) dataArray[1];
        this.email = (String) dataArray[2];
        this.phoneNumber = (String) dataArray[3];
    }

    @Override
    public String getTitle() {
        return name; // 对于联系人，标题是名称
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Tuple getData() {
        return new Tuple(ID, name, email, phoneNumber);
    }

    // 其他属性的 getters 和 setters
    public String getName() {
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
    private static String type = "Task";
    private int ID;
    private String title;
    private String description;
    private Date dueDate;
    public Task() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Title: ");
        this.title = scanner.nextLine();
        System.out.print("Description: ");
        this.description = scanner.nextLine();
        System.out.print("DueDate in format dd-MM-yyyy");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");  // Adjust format as necessary
        try {
            this.dueDate = sdf.parse(scanner.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Invalid date format. Please enter the date in the format dd-MM-yyyy.");
        }
        System.out.println("The task is successfully added to the system.\n");
        this.ID = nextId++;
    }

    public static void search(PIMKernel kernel){
        Map<Integer, PIMInterface> items = kernel.getItems().get(type);
        System.out.printf("%-2s | %-10s | %-30s | %-10s%n", "ID", "Title", "Description","DueDate");
        String partitionLine = new String(new char[46]).replace('\0', '_');
        for (PIMInterface tuple : items.values()) {
            Task task = (Task) tuple;
            int id = (int) task.getID();
            String title = (String) task.getTitle();
            String description = (String) task.getDescription();
            Date date = (Date) task.getDueDate();
            System.out.println(partitionLine);
            System.out.printf("%-2s | %-10s | %-30s | %-10s%n", id, title, description, date);
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Tuple getData() {
        return new Tuple(ID, title, description, dueDate);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public static void setNextId(int nextId) {
        Task.nextId = nextId;
    }
}

class Text extends PIMInterface implements Serializable {
    // STATIC variable to keep track of the next available ID
    private static int nextId = 1;
    private static String type = "Text";
    private int ID;
    private String title;
    private String content;

    public Text() {
        System.out.println("\n\n");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Title: ");
        this.title = scanner.nextLine();
        System.out.print("Note: ");
        this.content = scanner.nextLine();
        System.out.println("The text is successfully added to the system.\n");
        this.ID = nextId++;
    }

    public static Map<Integer, Integer> print(Collection<PIMInterface> items) {
        if (items.isEmpty()) {System.out.println("No data"); return null;}
        else {
            Map<Integer, Integer> displayNumberToId = new HashMap<>();
            int displayNumber = 1;
            String partitionLine = new String(new char[120]).replace('\0', '-');
            System.out.printf(partitionLine + "%n|%-2s | %-40s | %-70s|%n", "#", "Title", "Note");
            for (PIMInterface tuple : items) {
                Text text = (Text) tuple;
                displayNumberToId.put(displayNumber, text.getID());
                String title = (String) text.getTitle();
                String content = (String) text.getContent();
                title = title.length() > 40 - 3 ? title.substring(0, 40 - 3) + "..." : title;
                content = content.length() > 70 - 3 ? content.substring(0, 70 - 3) + "..." : content;
                System.out.printf(partitionLine + "%n|%-2d | %-40s | %-70s|%n", displayNumber, title, content);
                displayNumber++;
            }
            System.out.println(partitionLine);
            return displayNumberToId;
        }
    }

    public static Map<Integer, PIMInterface> filterByKeyword(Collection<PIMInterface> items, String keyword) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();

        for (PIMInterface item : items) {
            Text text = (Text) item;
            String title = text.getTitle();
            String content = text.getContent();

            if (title.toLowerCase().contains(keyword.toLowerCase()) || content.toLowerCase().contains(keyword.toLowerCase())) {
                // If the title or content contains the keyword, add it to the filtered items
                filteredItems.put(text.getID(), item);
            }
        }

        return filteredItems;
    }
    public static void search(PIMKernel kernel){
        Map<Integer, PIMInterface> items = kernel.getItems().get(type);
        if (items.isEmpty()){
            System.out.println("No data");
            return;
        }
        List<String> keywords = new ArrayList<>();
        Map<Integer, Integer> displayNumberToId;
        Map<Integer, PIMInterface> copy = items;
        while (true){
            System.out.println("\n\n");
            if (!keywords.isEmpty()){
                String keywordString = String.join(", ", keywords);
                System.out.println("Applied Keywords: " + keywordString);
            }
            displayNumberToId = print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            System.out.print("Choose an option: ");
            Scanner scanner = new Scanner(System.in);
            int cmd = scanner.nextInt();
            if (cmd == 0) break;

            if (cmd == 1){
                System.out.print("Enter #: ");
                int displayNumber = scanner.nextInt();
                int id = displayNumberToId.get(displayNumber);
                Text text  = (Text) copy.get(id);
                System.out.println("\n\n");
                System.out.println("<Title>");
                PIMInterface.printWrappedText(text.getTitle(), 120);
                System.out.println("<Content>");
                PIMInterface.printWrappedText(text.getContent(), 120);
                System.out.println("\n\n(1) Modify, (2) Delete, (3) Go Back");
                System.out.print("Choose an option: ");
                scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                if (option == 1){
                    System.out.println("\n\n");
                    scanner.nextLine();
                    System.out.print("Enter the modified title: ");
                    text.setTitle(scanner.nextLine());
                    System.out.print("Enter the modified content: ");
                    text.setContent(scanner.nextLine());
                    System.out.println("PIR content modified successfully.");
                    return;
                }
                else if (option == 2){
                    items.remove(id);
                    System.out.println("PIR content deleted successfully.");
                    return;
                }
            }
            else{ //cmd == 2
                scanner.nextLine();
                System.out.print("Enter Keyword: ");
                String keyword = scanner.nextLine();
                keywords.add(keyword);
                copy = filterByKeyword(copy.values(), keyword);
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Tuple getData() {
        return new Tuple(ID, title, content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static void setNextId(int nextId) {
        Text.nextId = nextId;
    }
}

class Event extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private String type = "Event";
    private int ID;
    private String title;
    private String description;
    private Date date;

    public Event(Tuple data) {
        // 使用 data 初始化对象属性
        Object[] dataArray = data.getData();
        this.ID = (int) dataArray[0];
        this.title = (String) dataArray[1];
        this.description = (String) dataArray[2];
        this.date = (Date) dataArray[3];
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Tuple getData() {
        return new Tuple(ID, title, description, date);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static void setNextId(int nextId) {
        Event.nextId = nextId;
    }
}
