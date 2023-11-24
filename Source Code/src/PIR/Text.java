package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Text extends PIRInterface implements Serializable {
    protected static int nextId = 1;

    public Text() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Title", 20);
        TITLES.put("Note", 50);
    }

    public Text(String title, String note) {
        new Text();

        // Set data directly
        data = new String[]{title, note};

        // Set ID
        this.ID = nextId++;
    }

    public Integer getNexId() {return Text.nextId;}
    public void setNextId(int nextId) {
        Text.nextId = nextId;
    }
}
