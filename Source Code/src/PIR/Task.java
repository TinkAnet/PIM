package PIR;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Task extends PIRInterface implements Serializable {
    protected static int nextId = 1;
    private static final String dateFormat = "yyyyMMdd";

    public Task() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 20);
        TITLES.put("Description", 50);
        TITLES.put("DueDate", 19);
    }

    public Task(String title, String description, String dueDate) {
        // Initialize TITLES
        new Task();

        // Set data directly
        data = new String[]{title, description, dueDate};

        // Set ID
        this.ID = nextId++;
    }

    public Integer getNexId() {return Task.nextId;}
    public void setNextId(int nextId) {Task.nextId = nextId;}
    public static String getDateFormat() {return dateFormat;}
}
