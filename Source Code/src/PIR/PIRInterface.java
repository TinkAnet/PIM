package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class PIRInterface implements Serializable {
    protected int ID;
    protected String[] data = new String[]{};
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

    public void setNextId(int nextId) {}

    public Integer getNexId() {return null;}

    public void setID(int newID){ID = newID;}

    public int getID() {
        return ID;
    }
}
