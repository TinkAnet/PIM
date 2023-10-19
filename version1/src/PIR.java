import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


interface PIMInterface {
    String getType();
    int getID();
    String getTitle();
    Tuple getData();
}

class Task implements PIMInterface {
    private static int nextId = 1;
    private static String type = "PIR.Task";
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

// PIR.Text
class Text implements PIMInterface {
    // STATIC variable to keep track of the next available ID
    private static int nextId = 1;
    private static String type = "PIR.Text";
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

// PIR.Event
class Event implements PIMInterface {
    private String type = "PIR.Event";
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

class Contact implements PIMInterface {
    private String type = "PIR.Contact";
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