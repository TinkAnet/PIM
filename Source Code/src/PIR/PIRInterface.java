package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class PIRInterface implements Serializable {
    protected int ID;
    protected String[] data;
    protected Map<String, Integer> TITLES = new LinkedHashMap<>();

    public Map<String, Integer> getTitles() {
        return TITLES;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public static void setNextId(int nextId) {
    }

    public int getID() {
        return ID;
    }
}
