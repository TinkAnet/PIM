import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class PIMInterface {
    abstract int getID();

    abstract Map<String, Integer> getTitles();
    abstract String[] getData();
    abstract void setData(String[] data);
    public abstract boolean matchesKeyword(String keyword);

    public static Map<Integer, PIMInterface> filterByKeyword(Collection<PIMInterface> items, String keyword) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            if (item.matchesKeyword(keyword)) {
                filteredItems.put(item.getID(), item);
            }
        }
        return filteredItems;
    }


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
}

class Contact extends PIMInterface implements Serializable {
    private static int nextId = 1;
    private int ID;
    private String name;
    private String email;
    private String phoneNumber;
    public static final Map<String, Integer> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("Name", 10);
        TITLES.put("Email", 20);
        TITLES.put("Phone Number", 20);
    }

    public Contact() {
        Utils.cls();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        this.name = scanner.nextLine();
        System.out.print("Email: ");
        this.email = scanner.nextLine();
        System.out.print("Phone number: ");
        this.phoneNumber = scanner.nextLine();
        Utils.cls();
        System.out.println("The contact is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }

    @Override
    public boolean matchesKeyword(String keyword) {
        return false;
    }

    public static void search(Map<Integer, PIMInterface> items){
    if (items.isEmpty()){
        System.out.println("No data");
        return;
    }
    List<String> expressionList = new ArrayList<>();
    Map<Integer, Integer> displayNumberToId;
    Map<Integer, PIMInterface> copy = new HashMap<>(items); // 使用 items 的初始副本

    while(true){
        Utils.cls();
        if (!expressionList.isEmpty()){
            String expressionString = String.join(" ", expressionList);
            System.out.println("Applied Keywords: " + expressionString);
        }
        displayNumberToId = PIMKernel.print(copy.values());
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
                PIMInterface.printWrappedText(contact.getName(), 120);
                System.out.println("Email");
                PIMInterface.printWrappedText(contact.getEmail(), 120);
                System.out.println("Phone number");
                PIMInterface.printWrappedText(contact.getPhoneNumber(), 120);
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
                    if (expressionList.isEmpty()) {
                        expressionList.add(String.format("\"%s\"", keyword));
                        copy = updateByKeyword(copy, items, keyword, "and");
                    } else {
                        System.out.println("Extend the keyword to the last one by:");
                        System.out.println("1. AND\n2. OR\n3. NOT\n0. Go back");
                        int choice = scanner.nextInt();
                        switch (choice) {
                            case 1: expressionList.add("AND"); expressionList.add(String.format("\"%s\"", keyword)); copy = updateByKeyword(copy, items, keyword, "and"); break;
                            case 2: expressionList.add("OR"); expressionList.add(String.format("\"%s\"", keyword)); copy = updateByKeyword(copy, items, keyword, "or"); break;
                            case 3: expressionList.add("NOT"); expressionList.add(String.format("\"%s\"", keyword)); copy = updateByKeyword(copy, items, keyword, "not"); break;
                            case 0: break;
                            default: System.out.println("Invalid Option"); break;
                        }
                    }
                }
        }
        else if (cmd == 2){ // cmd == 2
            scanner.nextLine();
            Utils.cls();
            System.out.print("Enter Keyword: ");
            String keyword = scanner.nextLine();
            if(expressionList.isEmpty()){
                expressionList.add(String.format("\"%s\"", keyword));
                copy = updateByKeyword(items, items, keyword, "and");
            } else {
                System.out.println("Extend the keyword to the last one by:");
                System.out.println("1. AND\n2. OR\n3. NOT\n0. Go back");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 读取回车符
                switch (choice){
                    case 1: expressionList.add("AND"); expressionList.add(String.format("\"%s\"", keyword)); copy = updateByKeyword(copy, items, keyword, "and"); break;
                    case 2: expressionList.add("OR"); expressionList.add(String.format("\"%s\"", keyword)); copy = updateByKeyword(items, items, keyword, "or"); break;
                    case 3: expressionList.add("NOT"); expressionList.add(String.format("\"%s\"", keyword)); copy = updateByKeyword(copy, items, keyword, "not"); break;
                    case 0: break;
                    default: System.out.println("Invalid Option"); break;
                }
            }
        }
    }
}


        private static Map<Integer, PIMInterface> updateByKeyword(Map<Integer, PIMInterface> current, Map<Integer, PIMInterface> all, String keyword, String mode) {
    Map<Integer, PIMInterface> result = new HashMap<>();
    keyword = keyword.toLowerCase();

    if (mode.equals("and")) {
        for (PIMInterface item : current.values()) {
            if (item instanceof Contact && matchesContact((Contact) item, keyword)) {
                result.put(item.getID(), item);
            }
        }
    } else if (mode.equals("or")) {
        result.putAll(current);
        for (PIMInterface item : all.values()) {
            if (item instanceof Contact && !current.containsKey(item.getID()) && matchesContact((Contact) item, keyword)) {
                result.put(item.getID(), item);
            }
        }
    } else if (mode.equals("not")) {
        for (PIMInterface item : current.values()) {
            if (item instanceof Contact && !matchesContact((Contact) item, keyword)) {
                result.put(item.getID(), item);
            }
        }
    }

    return result;
}

private static boolean matchesContact(Contact contact, String keyword) {
    return contact.getName().toLowerCase().contains(keyword) ||
           contact.getEmail().toLowerCase().contains(keyword) ||
           contact.getPhoneNumber().contains(keyword);
}

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String[] getData() { return new String[]{this.name, this.email, this.phoneNumber};}
    public void setData(String[] data){}

    // 其他属性的 getters 和 setters
    @Override
    public Map<String, Integer> getTitles() {
        return TITLES;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public static final Map<String, Integer> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("Title", 20);
        TITLES.put("Description", 50);
        TITLES.put("DueDate", 19);
    }

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


    @Override
    public boolean matchesKeyword(String keyword) {
        return false;
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
            displayNumberToId = PIMKernel.print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            System.out.println("3. Search by Date");
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
            else if(cmd == 2){ //cmd == 2
                scanner.nextLine();
                Utils.cls();
                System.out.print("Enter Keyword: ");
                String keyword = scanner.nextLine();
                keywords.add(keyword);
                copy = filterByKeyword(copy.values(), keyword);
            }else if(cmd == 3){
                scanner.nextLine(); 
                System.out.print("Enter the date (dd-MM-yyyy): ");
                String inputDate = scanner.nextLine();
                System.out.println("1. Equal\n2. After\n3. Before");
                System.out.print("Choose an option: ");
                int dateOption = scanner.nextInt();

                try {
                    Date searchDate = sdf.parse(inputDate);
                    copy = filterByDueDate(copy.values(), searchDate, dateOption);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format dd-MM-yyyy.");
                }
            }
        }
    }

    @Override
    public Map<String, Integer> getTitles() {
        return TITLES;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String[] getData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(this.dueDate);
        return new String[]{this.title, this.description, formattedDate};
    }
    public void setData(String[] data){}

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

    public static Map<Integer, PIMInterface> filterByDueDate(Collection<PIMInterface> items, Date searchDate, int option) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            if (item instanceof Task) {
                Task task = (Task) item;
                Date dueDate = task.getDueDate();

                switch (option) {
                    case 1: // Equal
                        if (dueDate.equals(searchDate)) {
                            filteredItems.put(task.getID(), item);
                        }
                        break;
                    case 2: // After
                        if (dueDate.after(searchDate)) {
                            filteredItems.put(task.getID(), item);
                        }
                        break;
                    case 3: // Before
                        if (dueDate.before(searchDate)) {
                            filteredItems.put(task.getID(), item);
                        }
                        break;
                }
            }
        }
        return filteredItems;
    }
}

class Text extends PIMInterface implements Serializable {
    // STATIC variable to keep track of the next available ID
    private static int nextId = 1;
    private int ID;
    private String[] data;

    public static final Map<String, Integer> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("Title", 20);
        TITLES.put("Note", 50);
    }

    public Text() {
        Utils.cls();
        Scanner scanner = new Scanner(System.in);
        data = new String[2];
        System.out.print("Title: ");
        data[0] = scanner.nextLine();
        System.out.print("Note: ");
        data[1] = scanner.nextLine();
        Utils.cls();
        System.out.println("The text is successfully added to the system.\n");
        this.ID = nextId++;
        Utils.ptc();
    }
    @Override
    public boolean matchesKeyword(String keyword) {
        return false;
    }

    public static Map<Integer, PIMInterface> UpdateByKeyword(Map<Integer, PIMInterface> copy, Map<Integer, PIMInterface> items ,String keyword, String mode) {
        Map<Integer, PIMInterface> result = new HashMap<>();

        if(mode.equals("and")){
            for (PIMInterface item : copy.values()) {
                String[] data = item.getData();
                if (data[0].toLowerCase().contains(keyword.toLowerCase()) || data[1].toLowerCase().contains(keyword.toLowerCase())) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(item.getID(), item);
                }
            }
        }else if(mode.equals("or")){
            for (PIMInterface item : copy.values()) {
                Text text = (Text) item;
                result.put(text.getID(), item);
            }

            for(PIMInterface item : items.values()){
                String[] data = item.getData();
                if (data[0].toLowerCase().contains(keyword.toLowerCase()) || data[1].toLowerCase().contains(keyword.toLowerCase())) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(item.getID(), item);
                }
            }

        }else if(mode.equals("not")){
            for (PIMInterface item : copy.values()) {
                String[] data = item.getData();
                if (!(data[0].toLowerCase().contains(keyword.toLowerCase()) || data[1].toLowerCase().contains(keyword.toLowerCase()))) {
                    // If the title or content contains the keyword, add it to the filtered items
                    result.put(item.getID(), item);
                }
            }
        }

        return result;
    }



    @Override
    public Map<String, Integer> getTitles() {return TITLES;}


    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String[] getData() {return data;}
    @Override
    public void setData(String[] data){this.data =data;}

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
    public static final Map<String, Integer> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("Title", 10);
        TITLES.put("Description", 20);
        TITLES.put("Starting Time", 19);
        TITLES.put("Alarm", 19);
    }
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

    @Override
    public boolean matchesKeyword(String keyword) {
        return false;
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");

    public static Map<Integer, PIMInterface> filterByDate(Collection<PIMInterface> items, Date searchDate, int option) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            if (item instanceof Event) {
                Event event = (Event) item;
                Date startingTime = event.getStartingTime();
                Date alarmTime = event.getAlarm();

                boolean matchesStartingTime = compareDates(startingTime, searchDate, option);
                boolean matchesAlarmTime = compareDates(alarmTime, searchDate, option);

                if (matchesStartingTime || matchesAlarmTime) {
                    filteredItems.put(event.getID(), item);
                }
            }
        }
        return filteredItems;
    }

    private static boolean compareDates(Date eventDate, Date searchDate, int option) {
        switch (option) {
            case 1: // Equal
                return eventDate.equals(searchDate);
            case 2: // After
                return eventDate.after(searchDate);
            case 3: // Before
                return eventDate.before(searchDate);
            default:
                return false;
        }
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
            displayNumberToId = PIMKernel.print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            System.out.println("3. Search by date");
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
            else if(cmd == 2){ //cmd == 2
                scanner.nextLine();
                Utils.cls();
                System.out.print("Enter Keyword: ");
                String keyword = scanner.nextLine();
                keywords.add(keyword);
                copy = filterByKeyword(copy.values(), keyword);
            }else if(cmd == 3) {
            scanner.nextLine();
            System.out.print("Enter the date (HH:mm dd-MM-yyyy): ");
            String inputDate = scanner.nextLine();
            System.out.println("1. Equal\n2. After\n3. Before");
            System.out.print("Choose an option: ");
            int dateOption = scanner.nextInt();

            try {
                Date searchDate = sdf.parse(inputDate);
                Map<Integer, PIMInterface> filtered = filterByDate(items.values(), searchDate, dateOption);
                // 显示过滤后的事件
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in the format HH:mm dd-MM-yyyy.");
            }
        }

        }
    }

    public Map<String, Integer> getTitles(){return TITLES;}
    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    @Override
    public int getID() {
        return ID;
    }

    public void setData(String[] data){}
    @Override
    public String[] getData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define your date format
        String formattedStartingTime = dateFormat.format(this.startingTime);
        String formattedAlarm = dateFormat.format(this.alarm);
        return new String[]{this.title, this.description, formattedStartingTime, formattedAlarm};
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

    public static Map<Integer, PIMInterface> filterByDueDate(Collection<PIMInterface> items, Date searchDate, int option) {
    Map<Integer, PIMInterface> filteredItems = new HashMap<>();
    for (PIMInterface item : items) {
        Task task = (Task) item;
        Date dueDate = task.getDueDate();

        switch (option) {
            case 1: // Equal
                if (dueDate.equals(searchDate)) {
                    filteredItems.put(task.getID(), item);
                }
                break;
            case 2: // After
                if (dueDate.after(searchDate)) {
                    filteredItems.put(task.getID(), item);
                }
                break;
            case 3: // Before
                if (dueDate.before(searchDate)) {
                    filteredItems.put(task.getID(), item);
                }
                break;
        }
    }
    return filteredItems;
}
}
