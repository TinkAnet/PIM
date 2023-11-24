package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Text extends PIRInterface implements Serializable {
    protected static int nextId = 1;

    public Text() {
        TITLES.put("Title", 20);
        TITLES.put("Note", 50);
    }

    public Text(String title, String note) {
        this();
        data = new String[]{title, note};
    }

    public Integer getNexId() {return Text.nextId;}
    public void setNextId(int nextId) {
        Text.nextId = nextId;
    }
}
