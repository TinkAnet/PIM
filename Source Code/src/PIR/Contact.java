package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Contact extends PIRInterface implements Serializable {
    protected static int nextId = 1;

    public Contact() {
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);
    }

    public Contact(String name, String email, String phoneNumber) {
        this();
        data = new String[]{name, email, phoneNumber};
    }
    public Integer getNexId() {return Contact.nextId;}
    public void setNextId(int nextId) {Contact.nextId = nextId;}
}
