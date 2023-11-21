import PIR.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


// PIMKernel
class PIMKernel {
    Map<String, Map<Integer, PIRInterface>> pimItems;
    public PIMKernel() {pimItems = new LinkedHashMap<>();}

    public Map<String, Map<Integer, PIRInterface>> getPimItems() {
        return pimItems;
    }

    public void create_PIR(String type) {
        Utils.cls();
        PIRInterface data = null;
        if (type != null) {
            switch (type) {
                case "Text" -> data = new Text();
                case "Task" -> data = new Task();
                case "Event" -> data = new Event();
                case "Contact" -> data = new Contact();
                case "Home" -> {
                    return;
                }
            }
        }
        if (data != null) {
            pimItems.computeIfAbsent(type, k -> new HashMap<>()).put(data.getID(), data);
        }
    }

    void export() {
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

    void load() {
        try{
            System.out.println("Enter the filename to be load from: ");
            Scanner scanner = new Scanner(System.in);
            String filename = scanner.nextLine();

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename+".pim"));
            pimItems = (Map<String, Map<Integer, PIRInterface>>) in.readObject();

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
        }catch (FileNotFoundException e) {
        System.out.println("File not found: " + e.getMessage());
        Utils.ptc();
    } catch (IOException e) {
        System.out.println("I/O error: " + e.getMessage());
        Utils.ptc();
    } catch (ClassNotFoundException e) {
        System.out.println("Class not found: " + e.getMessage());
        Utils.ptc();
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
        Utils.ptc();
    }
        
    }

    Map<Integer, Integer> print(Collection<PIRInterface> items) {
        if (items.isEmpty()) {
            System.out.println("No data");
            return null;
        }

        Map<Integer, Integer> displayNumberToId = new HashMap<>();
        PIRInterface firstItem = items.iterator().next();
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

        for (PIRInterface item : items) {
            System.out.println(partitionLine);
            String[] data = item.getData();

            if (data.length != titles.size()) {
                continue;
            }

            System.out.printf("|%-2s |", displayNumber);
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

    Map<Integer, PIRInterface> updateByDate(Map<Integer, PIRInterface> current, String DatePattern, Date searchDate, int dateOption, int searchOption) {
        if (searchDate == null) {
            System.out.println("Invalid time: search date is null");
            return current;
        }
        Map<Integer, PIRInterface> filteredItems = new HashMap<>();

        for (PIRInterface item : current.values()) {
            String[] data = item.getData();
            Date dueDate = null;
            try {
                dueDate = new SimpleDateFormat(DatePattern).parse(data[searchOption == 3 ? 2 : 3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (compareDates(dueDate, searchDate, dateOption)) filteredItems.put(item.getID(), item);

        }
        return filteredItems;
    }

    private boolean match(PIRInterface item, String keyword) {
        String[] data = item.getData();
        for (String str : data) {
            if (str.toLowerCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    Map<Integer, PIRInterface> updateByKeyword(Map<Integer, PIRInterface> current, Map<Integer, PIRInterface> all, String keyword, String mode) {
        Map<Integer, PIRInterface> result = new HashMap<>();
        keyword = keyword.toLowerCase();

        switch (mode) {
            case "and" -> {
                for (PIRInterface item : current.values()) {
                    if (match(item, keyword)) {
                        result.put(item.getID(), item);
                    }
                }
            }
            case "or" -> {
                result.putAll(current);
                for (PIRInterface item : all.values()) {
                    if (!current.containsKey(item.getID()) && match(item, keyword)) {
                        result.put(item.getID(), item);
                    }
                }
            }
            case "not" -> {
                for (PIRInterface item : current.values()) {
                    if (!match(item, keyword)) {
                        result.put(item.getID(), item);
                    }
                }
            }
        }

        return result;
    }
    private boolean compareDates(Date eventDate, Date searchDate, int option) {
        return switch (option) {
            case 1 -> // Equal
                    eventDate.equals(searchDate);
            case 2 -> // After
                    eventDate.after(searchDate);
            case 3 -> // Before
                    eventDate.before(searchDate);
            default -> false;
        };
    }

    void modifyPIR(PIRInterface selectedItem, String[] data) {
        selectedItem.setData(data);
    }

    void deletePIR(PIRInterface selectedItem, String type) {
        pimItems.get(type).remove(selectedItem.getID());
    }
}


public class PIM {

    private static final PIMKernel kernel = new PIMKernel();

    private String types(){
        System.out.println("1. Text Note");
        System.out.println("2. Task");
        System.out.println("3. Event");
        System.out.println("4. Contact");
        System.out.println("0. Back to home");
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice == -1) {
            try {
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
        switch (choice) {
            case 1 -> {return "Text";}
            case 2 -> {return "Task";}
            case 3 -> {return "Event";}
            case 4 -> {return "Contact";}
            case 0 -> {return "Home";}
            default -> {System.out.println("Invalid choice. Please choose a valid option.");Utils.ptc();}

        }
        return null;
    }

    int moves() {
        Utils.cls();
        System.out.println("[ Home Page ]");
        System.out.println("1. Create");
        System.out.println("2. Search (Modify & Delete)");
        System.out.println("3. Export");
        System.out.println("4. Load");
        System.out.println("5. Exit the System");
    
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
    
        while (choice < 1 || choice > 5) {
            System.out.print("Choose an option: ");
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 5) {
                    System.out.println("Invalid choice. Please choose a valid option between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear the buffer
            }
        }
    
        Utils.cls();
        return choice;
    }
    
    int home(){
        switch (moves()) {
            case 1 -> {
                System.out.println("Which information do you want to create?");
                kernel.create_PIR(types());
            }
            case 2 -> {
                System.out.println("Which information do you want to search?");
                searchPIR(types());
            }
            case 3 -> kernel.export();
            case 4 -> kernel.load();
            case 5 -> {
                System.out.println("System Ended.");
                return -1;
            }
            default -> {System.out.println("Invalid choice. Please choose a valid option.");Utils.ptc();}
        }

        return 1;
    }

    void searchPIR(String type) {
        if (type == null || type.equalsIgnoreCase("home"))
            return;


        Map<Integer, PIRInterface> items = kernel.getPimItems().get(type);
        if (items == null || items.isEmpty()) {
            Utils.cls();
            System.out.println("No items of type '" + type + "' available to search. Please create an item of this type first.");
            Utils.ptc();
            return;
        }

        List<String> expressionList = new ArrayList<>();
        Map<Integer, Integer> displayNumberToId;
        Map<Integer, PIRInterface> copy = items;
        PIRInterface firstItem = items.values().iterator().next();

        int task_event_flag = firstItem instanceof Task ? 1 : (firstItem instanceof Event ? 2 : 0);

        while (true){
            Utils.cls();
            if (!expressionList.isEmpty()){
                String expressionString = String.join(" ", expressionList);
                System.out.println("Applied Keywords: " + expressionString);
            }

            displayNumberToId = kernel.print(copy.values());
            System.out.println("1. Expand PIR by #");
            System.out.println("2. Narrow down the search by Keyword");
            if (task_event_flag == 1) {System.out.println("3. Search by DueDate");}
            else if(task_event_flag == 2) {System.out.println("3. Search by Starting Time\n4. Search by Alarm");}
            System.out.println("0. Back to home");
            System.out.print("Choose an option: ");

            Scanner scanner = new Scanner(System.in);

            try{
                switch (scanner.nextInt()){
                    case 0 -> {
                        Utils.cls();
                        return;
                    }
                    case 1 -> selectItem(displayNumberToId, copy, type);
                    case 2 -> copy = searchByKeyword(copy, expressionList,  items);
                    case 3 -> {if (task_event_flag > 0) copy = searchByDateTime(copy, task_event_flag, 3);}
                    case 4 -> {if (task_event_flag ==2) copy = searchByDateTime(copy, task_event_flag, 4);}
                    default -> {
                        System.out.println("Invalid choice. Please choose a valid option.");
                        Utils.ptc();
                    }
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a number.");
                Utils.ptc();
            }

        }
    }

    void selectItem(Map<Integer, Integer> displayNumberToId, Map<Integer, PIRInterface> copy, String type) {
        Scanner scanner = new Scanner(System.in);
        PIRInterface selectedItem = null;

        try {
            System.out.print("Enter #: ");
            int displayNumber = scanner.nextInt();
            Integer id = displayNumberToId.get(displayNumber);
            if (id != null) {
                selectedItem = copy.get(id);
            }

            if (selectedItem == null) {
                System.out.println("Invalid #: No such item found.");
                Utils.ptc();
                return;
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            Utils.ptc();
        }

        if (selectedItem != null) {
            Utils.cls();
            Map<String, Integer> titles = selectedItem.getTitles();
            String[] data = selectedItem.getData();

            int i = 0;
            for(String key : titles.keySet()){
                System.out.printf("<%s>%n", key);
                System.out.printf("%s%n\n", data[i++]);
            }

            System.out.println("1. Modify this PIR\n2. Delete this PIR\n0. Go back");
            System.out.println("Choose an option: ");
            try{
                switch (scanner.nextInt()){
                    case 0 -> Utils.cls();
                    case 1 -> modifyPIR(selectedItem,titles,data);
                    case 2 -> deletePIR(selectedItem,type);
                    default -> {
                        System.out.println("Invalid choice. Please choose a valid option.");
                        Utils.ptc();
                    }
                }
            }catch(InputMismatchException e){
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                Utils.cls();
            }
        }
    }

    Map<Integer, PIRInterface> searchByDateTime(Map<Integer, PIRInterface> items, int task_event_flag, int searchOption) {
        Scanner scanner = new Scanner(System.in);
        String DatePattern = task_event_flag == 1 ? "yyyyMMdd" : "yyyyMMdd HHmm";

        System.out.printf("Enter the date by %s: ", DatePattern);
        String inputDate = scanner.nextLine();
        System.out.println("1. Equal\n2. After\n3. Before");
        System.out.print("Choose an option: ");
        int dateOption = scanner.nextInt();
        Date searchDate = null;

        try {
            searchDate = new SimpleDateFormat(DatePattern).parse(inputDate);
        } catch (ParseException e) {
            System.out.printf("Invalid date format. Please enter the date in the format %s.\n",DatePattern);
        }
        return kernel.updateByDate(items, task_event_flag == 1 ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm" , searchDate, dateOption, searchOption);
    }

    static Map<Integer, PIRInterface> searchByKeyword(Map<Integer, PIRInterface> copy, List<String> expressionList, Map<Integer, PIRInterface> items) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        Utils.cls();

        System.out.print("Enter Keyword: ");
        String keyword = scanner.nextLine();

        if(expressionList.isEmpty()){
            expressionList.add(String.format("\"%s\"",keyword));
            copy = kernel.updateByKeyword(copy, items, keyword,"and");
        } else {
            System.out.println("Extend the keyword to the last one by:");
            System.out.println("1. AND\n2. OR\n3. NOT\n0. Go back");
            try{
                switch (scanner.nextInt()) {
                    case 1 -> {
                        expressionList.add("AND");
                        expressionList.add(String.format("\"%s\"", keyword));
                        copy = kernel.updateByKeyword(copy, items, keyword, "and");
                    }
                    case 2 -> {
                        expressionList.add("OR");
                        expressionList.add(String.format("\"%s\"", keyword));
                        copy = kernel.updateByKeyword(copy, items, keyword, "or");
                    }
                    case 3 -> {
                        expressionList.add("NOT");
                        expressionList.add(String.format("\"%s\"", keyword));
                        copy = kernel.updateByKeyword(copy, items, keyword, "not");
                    }
                    case 0 -> {
                    }
                    default -> {System.out.println("Invalid choice. Please choose a valid option.");Utils.ptc();}
                }
            }catch (InputMismatchException e){

            }

        }
        return copy;
    }

    void modifyPIR(PIRInterface selectedItem, Map<String, Integer> titles, String[] data) {
        int i = 0; boolean changedFlag = false;
        Scanner scanner = new Scanner(System.in);

        for (String key : titles.keySet()) {
            System.out.printf("Do you want to modify %s? (Y/N): ", key);
            if (scanner.next().trim().equalsIgnoreCase("Y")) {
                changedFlag = true;
                System.out.printf("Enter the modified %s: ", key);
                scanner.nextLine();  // Move to the next line to capture full string input
                data[i++] = scanner.nextLine();
            }
        }

        Utils.cls();
        if(changedFlag){
            kernel.modifyPIR(selectedItem,data);
            System.out.println("PIR content modified successfully.");
        }
        Utils.ptc();
    }

    void deletePIR(PIRInterface selectedItem, String type) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure to remove this PIR? (Y/N): ");
        if (scanner.next().trim().equalsIgnoreCase("Y")) {
            kernel.deletePIR(selectedItem,type);
            System.out.println("PIR removed successfully.");
            Utils.ptc();
        }
    }



}
