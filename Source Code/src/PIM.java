import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



// PIMKernel
class PIMKernel {
    private Map<String, Map<Integer, PIMInterface>> pimItems;
    public PIMKernel() {pimItems = new HashMap<>();}
    private static String types(){
        System.out.println("1. Text Note");
        System.out.println("2. Task");
        System.out.println("3. Event");
        System.out.println("4. Contact");
        System.out.println("0. Back to home");
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice == -1) {
            try {
                System.out.print("Choose an option:");
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
        switch (choice) {
            case 1: return "Text";
            case 2: return "Task";
            case 3: return "Event";
            case 4: return "Contact";
            case 0: return "home";
            default: System.out.println("Invalid choice. Please choose a valid option.");
        }
        return null;
    }

    public void create_PIR() {
        Utils.cls();
        System.out.println("Which information do you want to create?");
        String type = types();
        PIMInterface data = null;
        switch (type) {
            case "Text": data = new Text(); break;
            case "Task": data = new Task(); break;
            case "Event": data = new Event(); break;
            case "Contact": data = new Contact(); break;
            case "Home": return;
            default: break;
        }
        pimItems.computeIfAbsent(type, k -> new HashMap<>()).put(data.getID(), data);
    }

    public void search_PIR() {
        //System.out.println("\n\n");
        Utils.cls();

        System.out.println("Which information do you want to search?");
        String type = types();
        if (pimItems.get(type) == null && !Objects.equals(type, "Home")) {System.out.println("No data in the system."); return;}
        switch (type) {
            case "Text": search(pimItems.get("Text")); break;
            case "Task": search(pimItems.get("Task")); break;
            case "Contact": search(pimItems.get("Contact")); break;
            case "Event": search(pimItems.get("Event")); break;
            default: break;
        }
    }

    public void export() {
        try{
            System.out.println("Name the file where you want to export: ");
            Scanner scanner = new Scanner(System.in);
            String filename = scanner.nextLine();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename+".pim"));
            out.flush();

            out.writeObject(pimItems);
            out.flush();
            out.close();
            Utils.cls();
            System.out.printf("PIRs exported to %s successfully",filename);
            Utils.ptc();
        }catch(IOException e){
            Utils.cls();
            System.out.printf("Failed to export: %s",e);
            Utils.ptc();
        }
    }

    public void load() {
        // TODO: Implement this method
        try{
            System.out.println("Enter the filename to be load from: ");
            Scanner scanner = new Scanner(System.in);
            String filename = scanner.nextLine();

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename+".pim"));
            pimItems = (Map<String, Map<Integer, PIMInterface>>) in.readObject();

            if(pimItems.containsKey("Text")){
                Text.setNextId(pimItems.get("Text").size()+1);
            }
            if(pimItems.containsKey("Task")){
                Task.setNextId(pimItems.get("Task").size()+1);
            }
            if(pimItems.containsKey("Contact")){
                Contact.setNextId(pimItems.get("Contact").size()+1);
            }
            if(pimItems.containsKey("Event")){
                Event.setNextId(pimItems.get("Event").size()+1);
            }

            in.close();
            Utils.cls();
            System.out.printf("PIRs loaded from %s successfully",filename);
            Utils.ptc();
        }catch(Exception e){
            Utils.cls();
            System.out.printf("Failed to load: %s",e);
            Utils.ptc();
        }
    }
    public static Map<Integer, Integer> print(Collection<PIMInterface> items) {
        if (items.isEmpty()) {
            System.out.println("No data");
            return null;
        }

        Map<Integer, Integer> displayNumberToId = new HashMap<>();
        PIMInterface firstItem = items.iterator().next();
        Map<String, Integer> titles = firstItem.getTitles();

        int totalWidth = 5;
        for (int width : titles.values()) {
            totalWidth += width + 3;
        }
        String partitionLine = new String(new char[totalWidth]).replace('\0', '-');
        System.out.println(partitionLine);
        System.out.printf("|%-2s |", "ID");
        for (String key : titles.keySet()) {
            System.out.printf(" %-" + titles.get(key) + "s |", key);
        }
        System.out.println();

        int displayNumber = 1;

        for (PIMInterface item : items) {
            System.out.println(partitionLine);
            String[] data = item.getData();

            if (data.length != titles.size()) {
                continue;
            }

            System.out.printf("|%-2s |", item.getID());
            int dataIndex = 0;
            for (String key : titles.keySet()) {
                String content = data[dataIndex];
                int space = titles.get(key);

                if (content.length() > space) {
                    content = content.substring(0, space - 3) + "..."; // Adjust length and add "..."
                }

                System.out.printf(" %-" + space + "s |", content);
                dataIndex++;
            }
            System.out.println();

            displayNumberToId.put(displayNumber, item.getID());
            displayNumber++;
        }
        System.out.println(partitionLine);
        return displayNumberToId;
    }

    public static void search(Map<Integer, PIMInterface> items){
        if (items.isEmpty()){
            System.out.println("No data");
            return;
        }
        List<String> expressionList = new ArrayList<>();
        Map<Integer, Integer> displayNumberToId;
        Map<Integer, PIMInterface> copy = items;
        PIMInterface firstItem = items.values().iterator().next();
        boolean flag = firstItem instanceof Task || firstItem instanceof Event;

        while (true){
            Utils.cls();
            if (!expressionList.isEmpty()){
                String expressionString = String.join(" ", expressionList);
                System.out.println("Applied Keywords: " + expressionString);
            }
            displayNumberToId = PIMKernel.print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            if (flag) {System.out.println("3. Search by Date");}
            System.out.println("0. Back to home");
            System.out.print("Choose an option: ");
            Scanner scanner = new Scanner(System.in);
            int cmd = scanner.nextInt();
            if (cmd == 0) break;

            if (cmd == 1) {
                System.out.print("Enter #: ");
                int displayNumber = scanner.nextInt();
                int id = displayNumberToId.get(displayNumber);
                PIMInterface selectedItem = copy.get(id);

                Utils.cls();
                Map<String, Integer> titles = selectedItem.getTitles();
                String[] data = selectedItem.getData();
                int i = 0;
                for (String key : titles.keySet()) {
                    System.out.printf("<%s>%n", key);
                    System.out.printf("%s%n\n", data[i++]);
                }
                i = 0;
                for (String key : titles.keySet()) {
                    System.out.printf("Do you want to modify %s? (Y/N): ", key);
                    String choice = scanner.next().trim();
                    if (choice.equalsIgnoreCase("Y")) {
                        System.out.printf("Enter the modified %s: ", key);
                        data[i] = scanner.next();
                        selectedItem.setData(data); // Update the modified data
                    }
                    i++;
                }

                Utils.cls();
                System.out.println("PIR content modified successfully.");
                Utils.ptc();
                return;
            }
            else if (cmd == 2){
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
            else if (cmd == 3){
                scanner.nextLine();
                System.out.print("Enter the date (HH:mm dd-MM-yyyy): ");
                String inputDate = scanner.nextLine();
                System.out.println("1. Equal\n2. After\n3. Before");
                System.out.print("Choose an option: ");
                int dateOption = scanner.nextInt();

                try {
                    Date searchDate = sdf.parse(inputDate);
                    copy = filterByDate(items.values(), searchDate, dateOption);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format HH:mm dd-MM-yyyy.");
                }
            }
        }
    }
    private static boolean match(PIMInterface item, String keyword) {
        String[] data = item.getData();
        for (String str : data) {
            if (str.toLowerCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    private static Map<Integer, PIMInterface> UpdateByKeyword(Map<Integer, PIMInterface> current, Map<Integer, PIMInterface> all, String keyword, String mode) {
        Map<Integer, PIMInterface> result = new HashMap<>();
        keyword = keyword.toLowerCase();

        if (mode.equals("and")) {
            for (PIMInterface item : current.values()) {
                if (match(item, keyword)) {
                    result.put(item.getID(), item);
                }
            }
        } else if (mode.equals("or")) {
            result.putAll(current);
            for (PIMInterface item : all.values()) {
                if (!current.containsKey(item.getID()) && match(item, keyword)) {
                    result.put(item.getID(), item);
                }
            }
        } else if (mode.equals("not")) {
            for (PIMInterface item : current.values()) {
                if (!match(item, keyword)) {
                    result.put(item.getID(), item);
                }
            }
        }

        return result;
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
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
    public static Map<Integer, PIMInterface> filterByDate(Collection<PIMInterface> items, Date searchDate, int option) {
        Map<Integer, PIMInterface> filteredItems = new HashMap<>();
        for (PIMInterface item : items) {
            String[] data = item.getData();
            if (item instanceof Event) {
                Date startingTime = null;
                Date alarmTime = null;
                try {
                    startingTime = sdf.parse(data[2]);
                    alarmTime = sdf.parse(data[3]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                boolean matchesStartingTime = compareDates(startingTime, searchDate, option);
                boolean matchesAlarmTime = compareDates(alarmTime, searchDate, option);

                if (matchesStartingTime || matchesAlarmTime) {
                    filteredItems.put(item.getID(), item);
                }
            }
            else{ //Task
                Date dueDate = null;
                try {
                    dueDate = sdf.parse(data[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (compareDates(dueDate, searchDate, option)) filteredItems.put(item.getID(), item);
            }
        }
        return filteredItems;
    }
}


public class PIM {

    private static PIMKernel kernel = new PIMKernel();

    private static int moves(){
        //System.out.println("\n\n");
        Utils.cls();
        System.out.println("[ Home Page ]");
        System.out.println("1. Create");
        System.out.println("2. Search (Modify & Delete)");
        System.out.println("3. Export");
        System.out.println("4. Load");
        System.out.println("5. Exit the System");
        System.out.print("Choose an option:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        //System.out.print("\n\n");
        Utils.cls();
        return choice;
    }
    private static int home(){
        int choice = moves();
        String type;
        switch (choice) {
            case 1: kernel.create_PIR(); break;
            case 2: kernel.search_PIR(); break;
            case 3: kernel.export(); break;
            case 4: kernel.load(); break;
            case 5:
                System.out.println("System Ended.");
                return -1;
            /*case 4:
                System.out.println("Which information do you want to delete?");
                type = types();
                System.out.println("Please provide the ID of the item you want to delete:");
                int idToDelete = new Scanner(System.in).nextInt();

                List<PIMInterface> itemsOfType = kernel.search_PIR(type);
                for (PIMInterface item : itemsOfType) {
                    if (item.getID() == idToDelete) {
                        kernel.delete_PIR(item);
                        break;
                    }
                }
                break;*/
            default:
                System.out.println("Invalid choice. Please choose a valid option.");
        }

        return 1;
    }
    public static void main(String[] args) {
        System.out.println("Welcome to the Personal Information Management System!");
        int cmd = 0;
        while(cmd != -1){
            cmd = home();
        }
        //Tuple textData = new Tuple(1, "Sample Title", "This is a sample content.");
        //PIMInterface text = kernel.create_PIR("Text", textData);

    }
}
