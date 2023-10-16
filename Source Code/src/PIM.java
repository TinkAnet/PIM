import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

//MODIFICATIONS: try creating and searching Text Note
// this class Tuple is just a sample
class Tuple {
    private Object[] data;
    public Tuple(Object... data) {
        this.data = data;
    }
    public Object[] getData() {
        return data;
    }
}

// PIMInterface
interface PIMInterface {
    String getType();
    int getID();
    String getTitle();
    Tuple getData();
}


// PIMKernel


class PIMKernel {
    private Map<String, List<PIMInterface>> pimItems;

    public PIMKernel() {
        pimItems = new HashMap<>();
    }

    public void create_PIR(PIMInterface data) {
        String type = data.getType();
        pimItems.computeIfAbsent(type, k -> new ArrayList<>()).add(data);
    }

    public void modify_PIR(PIMInterface pir) {
        // TODO: Implement this method
    }

    public List<PIMInterface> search_PIR(Object ... criteria) {
        // assuming no criteria
        String type = (String) criteria[0];
        List<PIMInterface> list = pimItems.get(type);
        String partitionLine = null;
        if (type == "Text"){
            System.out.printf("%-2s | %-10s | %-30s%n", "ID", "Title", "Content");
            partitionLine = new String(new char[46]).replace('\0', '_');
        }
        else if (type == "Task"){
            System.out.printf("%-2s | %-10s | %-30s | %-10s%n", "ID", "Title", "Description","DueDate");
            partitionLine = new String(new char[46]).replace('\0', '_');
        }
        // TODO: Implement contact and event

        for (PIMInterface tuple : list) {
            if (type == "Text"){
                Text text = (Text) tuple;
                int id = (int) text.getID();
                String title = (String) text.getTitle();
                String content = (String) text.getContent();
                System.out.println(partitionLine);
                System.out.printf("%-2d | %-10s | %-30s%n", id, title, content);
            }
            else if(type == "Task"){
                Task task = (Task) tuple;
                int id = (int) task.getID();
                String title = (String) task.getTitle();
                String description = (String) task.getDescription();
                Date date = (Date) task.getDueDate();
                System.out.println(partitionLine);
                System.out.printf("%-2s | %-10s | %-30s | %-10s%n", id, title, description, date);
            }
            // TODO: Implement contact and event
        }
        return list;
    }

    public void delete_PIR(PIMInterface pir) {
        // TODO: Implement this method
    }

    public void export(String pathway) {
        // TODO: Implement this method
    }

    public void load(String pathway) {
        // TODO: Implement this method
    }
}


// Text
class Text implements PIMInterface {
    // STATIC variable to keep track of the next available ID
    private static int nextId = 1;
    private static String type = "Text";
    private int ID;
    private String title;
    private String content;

    public static Text create(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Note: ");
        String content = scanner.nextLine();
        Tuple textData = new Tuple(nextId, title, content);
        Text text = new Text(textData);
        System.out.println("The text is successfully added to the system.\n");
        return text;
    }
    public Text(Tuple data) {
        Object[] dataArray = data.getData();
        this.ID = (int) dataArray[0];
        this.title = (String) dataArray[1];
        this.content = (String) dataArray[2];
        // when new object is created, static id increments
        nextId++;
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
        return new Tuple(ID, title, content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


// Task
class Task implements PIMInterface {
    private static int nextId = 1;
    private static String type = "Task";
    private int ID;
    private String title;
    private String description;
    private static Date dueDate;

    public static Task create(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("DueDate in format dd-MM-yyyy");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");  // Adjust format as necessary
        try {
            dueDate = sdf.parse(scanner.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Invalid date format. Please enter the date in the format dd-MM-yyyy.");
        }

        Tuple textData = new Tuple(nextId, title, description, dueDate);
        Task task = new Task(textData);
        System.out.println("The task is successfully added to the system.\n");
        return task;
    }

    public Task(Tuple data) {
        Object[] dataArray = data.getData();
        this.ID = (int) dataArray[0];
        this.title = (String) dataArray[1];
        this.description = (String) dataArray[2];
        this.dueDate = (Date) dataArray[3];
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
}


// Event
class Event implements PIMInterface {
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
}


// Contact

class Contact implements PIMInterface {
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
}




// PIM
public class PIM {

    private static PIMKernel kernel = new PIMKernel();

    private static int moves(){
        System.out.println("1. Create");
        System.out.println("2. Search (Modify & Delete)");
        System.out.println("3. Exit the System");
        System.out.print("Choose an option:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        return choice;
    }

    private static String types(){
        System.out.println("1. Text Note");
        System.out.println("2. Task");
        System.out.println("3. Event");
        System.out.println("4. Contact");
        System.out.print("Choose an option:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                return "Text";
            case 2:
                return "Task";
            case 3:
                return "Event";
            case 4:
                return "Contact";
            default:
                System.out.println("Invalid choice. Please choose a valid option.");
        }
        return null;
    }
    private static int home(){
        System.out.println("Welcome to the Personal Information Management System!");
        int choice = moves();

        String type;
        switch (choice) {
            case 1:
                System.out.println("Which information do you want to create?");
                type = types();
                PIMInterface data = null;
                switch (type) {
                    case "Text":
                        data = Text.create();
                        break;
                    case "Task":
                        data = Task.create();
                        break;
                    default:
                        break;
                }
                kernel.create_PIR(data);
                break;
            case 2:
                System.out.println("Which information do you want to search?");
                type = types();
                kernel.search_PIR(type);
//                switch (type) {
//                    case "Text":
//
//                        System.out.println("Provide a keyword to narrow down the search.");
//                    default:
//                        break;
//                }
                break;
            case 3:
                System.out.println("System Ended.");
                return -1;
            default:
                System.out.println("Invalid choice. Please choose a valid option.");
        }
        return 1;
    }
    public static void main(String[] args) {
        int cmd = 0;
        while(cmd != -1){
            cmd = home();
        }
        //Tuple textData = new Tuple(1, "Sample Title", "This is a sample content.");
        //PIMInterface text = kernel.create_PIR("Text", textData);

    }
}
