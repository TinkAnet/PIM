package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Task extends PIRInterface implements Serializable {
    protected static int nextId = 1;
    private static final String dateFormat = "yyyyMMdd";

    public Task() {
        TITLES.put("Title", 20);
        TITLES.put("Description", 50);
        TITLES.put("DueDate", 19);
    }

    public Task(String title, String description, String dueDate) {
        this();
        data = new String[]{title, description, dueDate};
        this.ID = nextId++;
    }

    public Integer getNexId() {return Task.nextId;}
    public void setNextId(int nextId) {Task.nextId = nextId;}
    public static String getDateFormat() {return dateFormat;}
}
