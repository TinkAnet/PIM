import java.util.ArrayList;
import java.util.Date;

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
    private ArrayList<PIMInterface> pimItems;

    public PIMKernel() {
        pimItems = new ArrayList<>();
    }

    public PIMInterface create_PIR(String type, Object... data) {
        // TODO: Implement this method
        return null;
    }

    public void modify_PIR(PIMInterface pir) {
        // TODO: Implement this method
    }

    public ArrayList<PIMInterface> search_PIR(Object... criteria) {
        // TODO: Implement this method
        return null;
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
    private String type = "Text";
    private int ID;
    private String title;
    private String content;

    public Text(Tuple data) {
        Object[] dataArray = data.getData();
        this.ID = (int) dataArray[0];
        this.title = (String) dataArray[1];
        this.content = (String) dataArray[2];
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
    private String type = "Task";
    private int ID;
    private String title;
    private String description;
    private Date dueDate;

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

    public static void main(String[] args) {
        Tuple textData = new Tuple(1, "Sample Title", "This is a sample content.");
        PIMInterface text = kernel.create_PIR("Text", textData);

    }
}

