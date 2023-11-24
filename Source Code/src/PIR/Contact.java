package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Contact extends PIRInterface implements Serializable {
    protected static int nextId = 1;

    public Contact() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);
    }

    public Contact(String name, String email, String phoneNumber) {
        // 初始化 TITLES，与原构造函数类似
        new Contact();
        // 直接设置数据
        data = new String[]{name, email, phoneNumber};

        // 设置 ID
        this.ID = nextId++;
    }
    public Integer getNexId() {return Contact.nextId;}
    public void setNextId(int nextId) {Contact.nextId = nextId;}
}
