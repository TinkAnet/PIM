import java.io.*;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;


class Tuple {
    private Object[] data;
    public Tuple(Object... data) {
        this.data = data;
    }
    public Object[] getData() {
        return data;
    }
}

// PIMKernel
class PIMKernel {
    private Map<String, Map<Integer, PIMInterface>> pimItems;

    public PIMKernel() {
        pimItems = new HashMap<>();
    }

    public Map<String, Map<Integer, PIMInterface>> getItems(){
        return pimItems;
    }
    private static String types(){
        System.out.println("1. Text Note");
        System.out.println("2. Task");
        System.out.println("3. Event");
        System.out.println("4. Contact");
        System.out.println("0. Back to home");
        /*System.out.print("Choose an option:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        */
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
        //System.out.println("\n\n");
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

    public void modify_PIR(PIMInterface pir) {

        // TODO: Implement this method
    }

    public void search_PIR() {
        //System.out.println("\n\n");
        Utils.cls();

        System.out.println("Which information do you want to search?");
        String type = types();
        if (pimItems.get(type) == null && !Objects.equals(type, "Home")) {System.out.println("No data in the system."); return;}
        switch (type) {
            case "Text": Text.search(pimItems.get("Text")); break;
            case "Task": Task.search(pimItems.get("Task")); break;
            case "Contact": Contact.search(pimItems.get("Contact")); break;
            case "Event": Event.search(pimItems.get("Event")); break;
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
