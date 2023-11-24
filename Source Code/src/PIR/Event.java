package PIR;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Event extends PIRInterface implements Serializable {
    private static final String dateFormat = "yyyyMMdd HHmm";
    protected static int nextId = 1;

    public Event() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 10);
        TITLES.put("Description", 20);
        TITLES.put("Starting Time", 19);
        TITLES.put("Alarm", 19);
    }

    public Event(String title, String description, String startingTime, String alarm) {
        // Initialize TITLES
        new Event();

        // Set data directly
        data = new String[]{title, description, startingTime, alarm};

        // Set ID
        this.ID = nextId++;
    }

    public Integer getNexId() {return Event.nextId;}
    public void setNextId(int nextId) {Event.nextId = nextId;}
    public static String getDateFormat() { return dateFormat; }
}
