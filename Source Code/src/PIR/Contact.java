package PIR;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Contact extends PIRInterface implements Serializable {
    protected static int nextId = 1;

    public Contact() {
        TITLES = new LinkedHashMap<>();
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);

        Utils.cls();
        Scanner scanner = new Scanner(System.in);
        data = new String[3];
        System.out.print("Name: ");
        data[0] = scanner.nextLine();
        System.out.print("Email: ");
        data[1] = scanner.nextLine();
        System.out.print("Phone number: ");
        data[2] = scanner.nextLine();
        Utils.cls();
        System.out.println("The contact is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    public Contact(String name, String email, String phoneNumber) {
        // 初始化 TITLES，与原构造函数类似
        TITLES = new LinkedHashMap<>();
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);

        // 直接设置数据
        data = new String[]{name, email, phoneNumber};

        // 设置 ID
        this.ID = nextId++;
    }

    public static void setNextId(int nextId) {
        Contact.nextId = nextId;
    }
}
