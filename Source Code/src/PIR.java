import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class PIMInterface {
    abstract String getType();
    abstract int getID();
    abstract String getTitle();
    abstract Tuple getData();

    static void printWrappedText(String text, int lineLength) {
        String[] words = text.split(" ");
        StringBuilder lineBuilder = new StringBuilder();
        boolean firstWord = true;

        for (String word : words) {
            if (lineBuilder.length() + word.length() + (firstWord ? 0 : 1) > lineLength) {
                // Print the current line
                System.out.println(lineBuilder.toString());
                lineBuilder.setLength(0);  // Reset the lineBuilder
                firstWord = true;
            }

            if (!firstWord) {
                lineBuilder.append(" ");
            }
            lineBuilder.append(word);
            firstWord = false;
        }
        System.out.println(lineBuilder.toString());
    }
    static void setNextID(int newID){}
}

class Contact extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private String type = "Contact";
    private int ID;
    private String title;
    private String email;
    private String phoneNumber;

    public Contact() {
        Utils.cls();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        this.title = scanner.nextLine();
        System.out.print("Email: ");
        this.email = scanner.nextLine();
        System.out.print("Phone number: ");
        this.phoneNumber = scanner.nextLine();
        Utils.cls();
        System.out.println("The contact is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    public static Map<Integer, Integer> print(Collection<PIMInterface> items) {
        if (items.isEmpty()) {System.out.println("No data"); return null;}
        else {
            Map<Integer, Integer> displayNumberToId = new HashMap<>();
            int displayNumber = 1;
            String partitionLine = new String(new char[120]).replace('\0', '-');
            System.out.printf(partitionLine + "%n|%-2s | %-30s| %-45s| %-34s|%n", "ID", "Name", "Email","Phone number");
            for (PIMInterface tuple : items) {
                Contact contact = (Contact) tuple;
                displayNumberToId.put(displayNumber, contact.getID());
                String name = contact.getTitle();
                String email = contact.getEmail();
                String phone_num = contact.getPhoneNumber();
                name = name.length() > 30 - 3 ? name.substring(0, 30 - 3) + "..." : name;
                email = email.length() > 45 - 3 ? email.substring(0, 45 - 3) + "..." : email;
                phone_num = phone_num.length() > 35 - 3 ? phone_num.substring(0, 35 - 3) + "..." : phone_num;
                System.out.printf(partitionLine + "%n|%-2s | %-30s| %-45s| %-34s|%n", displayNumber, name, email, phone_num);
                displayNumber++;
            }
            System.out.println(partitionLine);
            return displayNumberToId;
        }
    }

    public static Map<Integer, PIMInterface> filterByKeyword(Collection<PIMInterface> items, String keyword) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            Contact contact = (Contact) item;
            String name = contact.getTitle();
            String email = contact.getEmail();
            String phone_num = contact.getPhoneNumber();

            if (name.toLowerCase().contains(keyword.toLowerCase()) || email.toLowerCase().contains(keyword.toLowerCase()) || phone_num.contains(keyword.toLowerCase())) {
                // If the title or content contains the keyword, add it to the filtered items
                filteredItems.put(contact.getID(), item);
            }
        }
        return filteredItems;
    }

    public static void search(Map<Integer, PIMInterface> items){
        if (items.isEmpty()){
            System.out.println("No data");
            return;
        }
        List<String> keywords = new ArrayList<>();
        Map<Integer, Integer> displayNumberToId;
        Map<Integer, PIMInterface> copy = items;

        while(true){
            Utils.cls();
            if (!keywords.isEmpty()){
                String keywordString = String.join(", ", keywords);
                System.out.println("Applied Keywords: " + keywordString);
            }
            displayNumberToId = print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            System.out.println("0. Back to home");
            System.out.print("Choose an option: ");
            Scanner scanner = new Scanner(System.in);
            int cmd = scanner.nextInt();
            if (cmd == 0) break;

            if (cmd == 1){
                System.out.print("Enter #: ");
                int displayNumber = scanner.nextInt();
                assert displayNumberToId != null;
                Integer id = displayNumberToId.get(displayNumber);

                Contact contact = (Contact) copy.get(id);
                Utils.cls();

                System.out.println("<Name>");
                PIMInterface.printWrappedText(contact.getTitle(), 120);
                System.out.println("Email");
                PIMInterface.printWrappedText(contact.getTitle(), 120);
                System.out.println("Phone number");
                PIMInterface.printWrappedText(contact.getTitle(), 120);
                Utils.cls();

                System.out.println("(1) Modify, (2) Delete, (3) Go Back");
                System.out.print("Choose an option: ");
                scanner = new Scanner(System.in);

                int option = scanner.nextInt();
                if (option == 1){
                    //System.out.println("\n\n");
                    Utils.cls();
                    scanner.nextLine();  // Consume the newline left from previous input
                    System.out.print("Do you want to modify the name? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified name: ");
                        contact.setName(scanner.nextLine());
                    }

                    System.out.print("Do you want to modify the email? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified email: ");
                        contact.setEmail(scanner.nextLine());
                    }

                    System.out.print("Do you want to modify the phone number? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified phone number: ");
                        contact.setPhoneNumber(scanner.nextLine());
                    }

                    Utils.cls();
                    System.out.println("PIR content modified successfully.");
                    Utils.ptc();
                    return;

                }
                else if (option == 2){
                    items.remove(id);
                    Utils.cls();
                    System.out.println("PIR content deleted successfully.");
                    Utils.ptc();
                    return;
                }
                else{ //cmd == 2
                    scanner.nextLine();
                    Utils.cls();
                    System.out.print("Enter Keyword: ");
                    String keyword = scanner.nextLine();
                    keywords.add(keyword);
                    copy = filterByKeyword(copy.values(), keyword);
                }
            }
        }


    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Tuple getData() {
        return new Tuple(ID, title, email, phoneNumber);
    }

    // 其他属性的 getters 和 setters
    @Override
    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public static void setNextId(int nextId) {
        Contact.nextId = nextId;
    }
}

class Task extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private static String type = "Task";
    private int ID;
    private String title;
    private String description;
    private Date dueDate;
    // Adjust format as necessary
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public Task() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Title: ");
        this.title = scanner.nextLine();
        System.out.print("Description: ");
        this.description = scanner.nextLine();
        System.out.println("DueDate in format dd-MM-yyyy");

        try {
            this.dueDate = sdf.parse(scanner.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Invalid date format. Please enter the date in the format dd-MM-yyyy.");
        }
        Utils.cls();
        System.out.println("The task is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

//    public static void search(PIMKernel kernel){
//        Map<Integer, PIMInterface> items = kernel.getItems().get(type);
//        if(items.isEmpty()){
//            System.out.println("No data");
//            return;
//        }
//        System.out.printf("%-2s | %-10s | %-30s | %-10s%n", "ID", "Title", "Description","DueDate");
//        String partitionLine = new String(new char[46]).replace('\0', '_');
//        for (PIMInterface tuple : items.values()) {
//            Task task = (Task) tuple;
//            int id = (int) task.getID();
//            String title = (String) task.getTitle();
//            String description = (String) task.getDescription();
//            Date date = (Date) task.getDueDate();
//            System.out.println(partitionLine);
//            System.out.printf("%-2s | %-10s | %-30s | %-10s%n", id, title, description, date);
//        }
//        Utils.ptc();
//    }

    public static Map<Integer, Integer> print(Collection<PIMInterface> items) {
        if (items.isEmpty()) {System.out.println("No data"); return null;}
        else {
            Map<Integer, Integer> displayNumberToId = new HashMap<>();
            int displayNumber = 1;
            System.out.printf("%-2s | %-20s | %-70s | %-20s|%n", "ID", "Title", "Description","DueDate");
            String partitionLine = new String(new char[120]).replace('\0', '-');
            for (PIMInterface tuple : items) {
                Task task = (Task) tuple;
                displayNumberToId.put(displayNumber, task.getID());
                String title = (String) task.getTitle();
                String description = (String) task.getDescription();
                String date = sdf.format(task.getDueDate()).toString();
                title = title.length() > 20 - 3 ? title.substring(0, 20 - 3) + "..." : title;
                description = description.length() > 70 - 3 ? title.substring(0, 70 - 3) + "..." : title;
                date = date.length() > 20 - 3 ? date.substring(0, 30 - 3) + "..." : date;
                System.out.printf(partitionLine + "%n|%-2d | %-20s | %-70s | %-20s|%n", displayNumber, title, description, date);
                displayNumber++;
            }
            System.out.println(partitionLine);
            return displayNumberToId;
        }
    }

    public static Map<Integer, PIMInterface> filterByKeyword(Collection<PIMInterface> items, String keyword) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            Task task = (Task) item;
            String title = task.getTitle();
            String description = task.getDescription();
            Date date = task.getDueDate();// Adjust format as necessary
            String DateToString = sdf.format(date);  // one special case for date type
            if (title.toLowerCase().contains(keyword.toLowerCase()) || description.toLowerCase().contains(keyword.toLowerCase()) || DateToString.contains(keyword)) {
                // If the title or content contains the keyword, add it to the filtered items
                filteredItems.put(task.getID(), item);
            }
        }
        return filteredItems;
    }

    public static void search(Map<Integer, PIMInterface> items){
        if (items.isEmpty()){
            System.out.println("No data");
            return;
        }
        List<String> keywords = new ArrayList<>();
        Map<Integer, Integer> displayNumberToId;
        Map<Integer, PIMInterface> copy = items;
        while (true){
            //System.out.println("\n\n");
            Utils.cls();
            if (!keywords.isEmpty()){
                String keywordString = String.join(", ", keywords);
                System.out.println("Applied Keywords: " + keywordString);
            }
            displayNumberToId = print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            System.out.println("0. Back to home");
            System.out.print("Choose an option: ");
            Scanner scanner = new Scanner(System.in);
            int cmd = scanner.nextInt();
            if (cmd == 0) break;

            if (cmd == 1){
                System.out.print("Enter #: ");
                int displayNumber = scanner.nextInt();
                int id = displayNumberToId.get(displayNumber);
                Task task  = (Task) copy.get(id);
                //System.out.println("\n\n");
                Utils.cls();
                System.out.println("<Title>");
                PIMInterface.printWrappedText(task.getTitle(), 120);
                System.out.println("<Description>");
                PIMInterface.printWrappedText(task.getDescription(), 120);
                System.out.println("<DueDate>");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");  // Adjust format as necessary
                PIMInterface.printWrappedText(sdf.format(task.getDueDate()), 120);
                Utils.cls();
                System.out.println("(1) Modify, (2) Delete, (3) Go Back");
                System.out.print("Choose an option: ");
                scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                if (option == 1){
                    //System.out.println("\n\n");
                    Utils.cls();
                    scanner.nextLine();  // Consume the newline left from previous input
                    System.out.print("Do you want to modify the title? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified title: ");
                        task.setTitle(scanner.nextLine());
                    }

                    System.out.print("Do you want to modify the description? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified description: ");
                        task.setDescription(scanner.nextLine());
                    }

                    System.out.print("Do you want to modify the due date? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified date (dd-MM-yyyy): ");
                        try {
                            task.setDueDate(sdf.parse(scanner.nextLine()));
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please enter the date in the format dd-MM-yyyy.");
                        }
                    }

                    Utils.cls();
                    System.out.println("PIR content modified successfully.");
                    Utils.ptc();
                    return;

                }
                else if (option == 2){
                    items.remove(id);
                    Utils.cls();
                    System.out.println("PIR content deleted successfully.");
                    Utils.ptc();
                    return;
                }
            }
            else{ //cmd == 2
                scanner.nextLine();
                Utils.cls();
                System.out.print("Enter Keyword: ");
                String keyword = scanner.nextLine();
                keywords.add(keyword);
                copy = filterByKeyword(copy.values(), keyword);
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Tuple getData() {
        return new Tuple(ID, title, description, dueDate);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public static void setNextId(int nextId) {
        Task.nextId = nextId;
    }
}

class Text extends PIMInterface implements Serializable {
    // STATIC variable to keep track of the next available ID
    private static int nextId = 1;
    private static String type = "Text";
    private int ID;
    private String title;
    private String content;

    public Text() {
        //System.out.println("\n\n");
        Utils.cls();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Title: ");
        this.title = scanner.nextLine();
        System.out.print("Note: ");
        this.content = scanner.nextLine();
        Utils.cls();
        System.out.println("The text is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    public static Map<Integer, Integer> print(Collection<PIMInterface> items) {
        if (items.isEmpty()) {System.out.println("No data"); return null;}
        else {
            Map<Integer, Integer> displayNumberToId = new HashMap<>();
            int displayNumber = 1;
            String partitionLine = new String(new char[120]).replace('\0', '-');
            System.out.printf(partitionLine + "%n|%-2s | %-40s | %-70s|%n", "#", "Title", "Note");
            for (PIMInterface tuple : items) {
                Text text = (Text) tuple;
                displayNumberToId.put(displayNumber, text.getID());
                String title = (String) text.getTitle();
                String content = (String) text.getContent();
                title = title.length() > 40 - 3 ? title.substring(0, 40 - 3) + "..." : title;
                content = content.length() > 70 - 3 ? content.substring(0, 70 - 3) + "..." : content;
                System.out.printf(partitionLine + "%n|%-2d | %-40s | %-70s|%n", displayNumber, title, content);
                displayNumber++;
            }
            System.out.println(partitionLine);
            return displayNumberToId;
        }
    }

    public static Map<Integer, PIMInterface> UpdateByKeyword(Map<Integer, PIMInterface> copy, Map<Integer, PIMInterface> items ,String keyword, String mode) {
        Map<Integer, PIMInterface> result = new HashMap<>();

        if(mode.equals("and")){
            for (PIMInterface item : copy.values()) {
                Text text = (Text) item;
                String title = text.getTitle();
                String content = text.getContent();

                if (title.toLowerCase().contains(keyword.toLowerCase()) || content.toLowerCase().contains(keyword.toLowerCase())) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(text.getID(), item);
                }
            }
        }else if(mode.equals("or")){
            for (PIMInterface item : copy.values()) {
                Text text = (Text) item;
                result.put(text.getID(), item);
            }

            for(PIMInterface item : items.values()){
                Text text = (Text) item;
                String title = text.getTitle();
                String content = text.getContent();

                if (title.toLowerCase().contains(keyword.toLowerCase()) || content.toLowerCase().contains(keyword.toLowerCase())) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(text.getID(), item);
                }
            }

        }else if(mode.equals("not")){
            for (PIMInterface item : copy.values()) {
                Text text = (Text) item;
                String title = text.getTitle();
                String content = text.getContent();

                if (!(title.toLowerCase().contains(keyword.toLowerCase()) || content.toLowerCase().contains(keyword.toLowerCase()))) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(text.getID(), item);
                }
            }
        }

        return result;
    }
    public static void search(Map<Integer, PIMInterface> items){
        if (items.isEmpty()){
            System.out.println("No data");
            return;
        }
        List<String> expressionList = new ArrayList<>();
        Map<Integer, Integer> displayNumberToId;
        Map<Integer, PIMInterface> copy = items;
        while (true){
            //System.out.println("\n\n");
            Utils.cls();
            if (!expressionList.isEmpty()){
                String expressionString = String.join(" ", expressionList);
                System.out.println("Applied Keywords: " + expressionString);
            }
            displayNumberToId = print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            System.out.println("0. Back to home");
            System.out.print("Choose an option: ");
            Scanner scanner = new Scanner(System.in);
            int cmd = scanner.nextInt();
            if (cmd == 0) break;

            if (cmd == 1){
                System.out.print("Enter #: ");
                int displayNumber = scanner.nextInt();
                int id = displayNumberToId.get(displayNumber);
                Text text  = (Text) copy.get(id);
                //System.out.println("\n\n");
                Utils.cls();
                System.out.println("<Title>");
                PIMInterface.printWrappedText(text.getTitle(), 120);
                System.out.println("<Content>");
                PIMInterface.printWrappedText(text.getContent(), 120);
                Utils.cls();
                System.out.println("(1) Modify, (2) Delete, (3) Go Back");
                System.out.print("Choose an option: ");
                scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                if (option == 1){
                    //System.out.println("\n\n");
                    Utils.cls();

                    scanner.nextLine();  // Consume the newline left from previous input

                    System.out.print("Do you want to modify the title? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified title: ");
                        text.setTitle(scanner.nextLine());
                    }

                    System.out.print("Do you want to modify the content? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified content: ");
                        text.setContent(scanner.nextLine());
                    }

                    Utils.cls();
                    System.out.println("PIR content modified successfully.");
                    Utils.ptc();
                    return;
                }
                else if (option == 2){
                    items.remove(id);
                    Utils.cls();
                    System.out.println("PIR content deleted successfully.");
                    Utils.ptc();
                    return;
                }
            }
            else{ //cmd == 2
                scanner.nextLine();
                Utils.cls();

                System.out.print("Enter Keyword: ");
                String keyword = scanner.nextLine();

                if(expressionList.isEmpty()){
                    expressionList.add(String.format("\"%s\"",keyword));
                    copy = UpdateByKeyword(copy, items, keyword,"and");

                } else {
                    System.out.println("Extend the keyword to the last one by:");
                    System.out.println("1. AND\n2. OR\n3. NOT\n0. Go back");
                    switch (scanner.nextInt()){
                        case 1: {expressionList.add("AND"); expressionList.add(String.format("\"%s\"",keyword)); copy = UpdateByKeyword(copy, items, keyword, "and"); break;}
                        case 2: {expressionList.add("OR"); expressionList.add(String.format("\"%s\"",keyword)); copy = UpdateByKeyword(copy, items, keyword, "or"); break;}
                        case 3: {expressionList.add("NOT"); expressionList.add(String.format("\"%s\"",keyword)); copy = UpdateByKeyword(copy, items, keyword, "not"); break;}
                        case 0: {break;}
                        default: {System.out.println("Invalid Option");break;}
                    }
                }


            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Tuple getData() {
        return new Tuple(ID, title, content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static void setNextId(int nextId) {
        Text.nextId = nextId;
    }
}

class Event extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private String type = "Event";
    private int ID;
    private String title;
    private String description;
    private Date startingTime;
    private Date alarm;
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");

    public Event() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Title: ");
        this.title = scanner.nextLine();
        System.out.print("Description: ");
        this.description = scanner.nextLine();
        System.out.print("Starting time in format HH:mm dd-MM-yyyy: ");

        try {
            this.startingTime = sdf.parse(scanner.nextLine());
            System.out.print("Alarm in format HH:mm dd-MM-yyyy: ");
            this.alarm = sdf.parse(scanner.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Invalid date format. Please enter the date in the format HH:mm dd-MM-yyyy.");
        }
        Utils.cls();
        System.out.println("The event is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    public static Map<Integer, Integer> print(Collection<PIMInterface> items) {
        if (items.isEmpty()) {System.out.println("No data"); return null;}
        else {
            Map<Integer, Integer> displayNumberToId = new HashMap<>();
            int displayNumber = 1;
            System.out.printf("%-2s | %-10s | %-60s | %-20s | %-20s|%n", "ID", "Title", "Starting time","Alarm","");
            String partitionLine = new String(new char[120]).replace('\0', '-');
            for (PIMInterface tuple : items) {
                Event event = (Event) tuple;
                displayNumberToId.put(displayNumber, event.getID());
                String title = (String) event.getTitle();
                String description = (String) event.getDescription();
                String startTime = sdf.format(event.getStartingTime()).toString();
                String alarm = sdf.format(event.getAlarm()).toString();

                title = title.length() > 20 - 3 ? title.substring(0, 20 - 3) + "..." : title;
                description = description.length() > 70 - 3 ? title.substring(0, 70 - 3) + "..." : title;
                startTime = startTime.length() > 20 - 3 ? startTime.substring(0, 30 - 3) + "..." : startTime;
                alarm = alarm.length() > 20 - 3 ? alarm.substring(0, 30 - 3) + "..." : alarm;

                System.out.printf(partitionLine + "%n|%-2d | %-10s | %-60s | %-20s | %-20s|%n", displayNumber, title, description, startTime, alarm,"");
                displayNumber++;
            }
            System.out.println(partitionLine);
            return displayNumberToId;
        }
    }

    public static Map<Integer, PIMInterface> filterByKeyword(Collection<PIMInterface> items, String keyword) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            Event event = (Event) item;
            String title = event.getTitle();
            String description = event.getDescription();
            String startTime = sdf.format(event.getStartingTime());// Adjust format as necessary
            String alarm =  sdf.format(event.getAlarm());

            if (title.toLowerCase().contains(keyword.toLowerCase()) || description.toLowerCase().contains(keyword.toLowerCase()) || startTime.toLowerCase().contains(keyword.toLowerCase()) || alarm.toLowerCase().contains(keyword.toLowerCase())) {
                // If the title or content contains the keyword, add it to the filtered items
                filteredItems.put(event.getID(), item);
            }
        }
        return filteredItems;
    }

    public static void search(Map<Integer, PIMInterface> items){
        if (items.isEmpty()){
            System.out.println("No data");
            return;
        }
        List<String> keywords = new ArrayList<>();
        Map<Integer, Integer> displayNumberToId;
        Map<Integer, PIMInterface> copy = items;
        while (true){
            //System.out.println("\n\n");
            Utils.cls();
            if (!keywords.isEmpty()){
                String keywordString = String.join(", ", keywords);
                System.out.println("Applied Keywords: " + keywordString);
            }
            displayNumberToId = print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            System.out.println("0. Back to home");
            System.out.print("Choose an option: ");
            Scanner scanner = new Scanner(System.in);
            int cmd = scanner.nextInt();
            if (cmd == 0) break;

            if (cmd == 1){
                System.out.print("Enter #: ");
                int displayNumber = scanner.nextInt();
                int id = displayNumberToId.get(displayNumber);
                Event event  = (Event) copy.get(id);
                //System.out.println("\n\n");
                Utils.cls();
                System.out.println("<Title>");
                PIMInterface.printWrappedText(event.getTitle(), 120);
                System.out.println("<Description>");
                PIMInterface.printWrappedText(event.getDescription(), 120);
                System.out.println("<Starting time>");
                PIMInterface.printWrappedText(sdf.format(event.getStartingTime()), 120);
                System.out.println("<Alarm>");
                PIMInterface.printWrappedText(sdf.format(event.getAlarm()), 120);

                Utils.cls();
                System.out.println("(1) Modify, (2) Delete, (3) Go Back");
                System.out.print("Choose an option: ");
                scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                if (option == 1){
                    //System.out.println("\n\n");
                    Utils.cls();
                    scanner.nextLine();  // Consume the newline left from previous input
                    System.out.print("Do you want to modify the title? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified title: ");
                        event.setTitle(scanner.nextLine());
                    }

                    System.out.print("Do you want to modify the description? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified description: ");
                        event.setDescription(scanner.nextLine());
                    }

                    System.out.print("Do you want to modify the starting time? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified date (HH:mm dd-MM-yyyy): ");
                        try {
                            event.setStartingTime(sdf.parse(scanner.nextLine()));
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please enter the date in the format dd-MM-yyyy.");
                        }
                    }

                    System.out.print("Do you want to modify the alarm? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        System.out.print("Enter the modified date (HH:mm dd-MM-yyyy): ");
                        try {
                            event.setAlarm(sdf.parse(scanner.nextLine()));
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please enter the date in the format dd-MM-yyyy.");
                        }
                    }


                    Utils.cls();
                    System.out.println("PIR content modified successfully.");
                    Utils.ptc();
                    return;

                }
                else if (option == 2){
                    items.remove(id);
                    Utils.cls();
                    System.out.println("PIR content deleted successfully.");
                    Utils.ptc();
                    return;
                }
            }
            else{ //cmd == 2
                scanner.nextLine();
                Utils.cls();
                System.out.print("Enter Keyword: ");
                String keyword = scanner.nextLine();
                keywords.add(keyword);
                copy = filterByKeyword(copy.values(), keyword);
            }
        }
    }

    private void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Tuple getData() {
        return new Tuple(ID, title, description, startingTime, alarm);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date time) {this.startingTime = time;}
    public Date getAlarm() {return alarm;}

    public void setAlarm(Date alarm){this.alarm = alarm;}

    public static void setNextId(int nextId) {
        Event.nextId = nextId;
    }
}
