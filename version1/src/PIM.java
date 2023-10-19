import java.util.*;

//MODIFICATIONS: try creating and searching PIR.Text Note
// this class Tuple is just a sample
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
    private Map<String, List<PIMInterface>> pimItems;

    public PIMKernel() {
        pimItems = new HashMap<>();
    }

    public void create_PIR(PIMInterface data) {
        String type = data.getType();
        pimItems.computeIfAbsent(type, k -> new ArrayList<>()).add(data);
    }

    public void modify_PIR(PIMInterface pir) {
        // TODO: Implement this method
    }

    public List<PIMInterface> search_PIR(Object ... criteria) {
        // assuming no criteria
        String type = (String) criteria[0];
        List<PIMInterface> list = pimItems.get(type);
        String partitionLine = null;
        if (type == "PIR.Text"){
            System.out.printf("%-2s | %-10s | %-30s%n", "ID", "Title", "Content");
            partitionLine = new String(new char[46]).replace('\0', '_');
        }
        else if (type == "PIR.Task"){
            System.out.printf("%-2s | %-10s | %-30s | %-10s%n", "ID", "Title", "Description","DueDate");
            partitionLine = new String(new char[46]).replace('\0', '_');
        }
        // TODO: Implement contact and event

        for (PIMInterface tuple : list) {
            if (type == "PIR.Text"){
                Text text = (Text) tuple;
                int id = (int) text.getID();
                String title = (String) text.getTitle();
                String content = (String) text.getContent();
                System.out.println(partitionLine);
                System.out.printf("%-2d | %-10s | %-30s%n", id, title, content);
            }
            else if(type == "PIR.Task"){
                Task task = (Task) tuple;
                int id = (int) task.getID();
                String title = (String) task.getTitle();
                String description = (String) task.getDescription();
                Date date = (Date) task.getDueDate();
                System.out.println(partitionLine);
                System.out.printf("%-2s | %-10s | %-30s | %-10s%n", id, title, description, date);
            }
            // TODO: Implement contact and event
        }
        return list;
    }

    public void delete_PIR(PIMInterface pir) {
        String type = pir.getType();
        int id = pir.getID();

        List<PIMInterface> items = pimItems.get(type);
        if (items != null) {
            // delete PIM by iterator
            Iterator<PIMInterface> iterator = items.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getID() == id) {
                    iterator.remove();
                    System.out.println("Item deleted successfully.");
                    return;
                }
            }
        }
        System.out.println("No item found with the given type and ID.");
    }


    public void export(String pathway) {
        // TODO: Implement this method
    }

    public void load(String pathway) {
        // TODO: Implement this method
    }
}
// PIM
public class PIM {

    private static PIMKernel kernel = new PIMKernel();

    private static int moves(){
        System.out.println("1. Create");
        System.out.println("2. Search (Modify & Delete)");
        System.out.println("3. Exit the System");
        System.out.println("4. Delete");
        System.out.print("Choose an option:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        return choice;
    }

    private static String types(){
        System.out.println("1. PIR.Text Note");
        System.out.println("2. PIR.Task");
        System.out.println("3. PIR.Event");
        System.out.println("4. PIR.Contact");
        System.out.print("Choose an option:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                return "PIR.Text";
            case 2:
                return "PIR.Task";
            case 3:
                return "PIR.Event";
            case 4:
                return "PIR.Contact";
            default:
                System.out.println("Invalid choice. Please choose a valid option.");
        }
        return null;
    }

    private static int home(){
        System.out.println("Welcome to the Personal Information Management System!");
        int choice = moves();

        String type;
        switch (choice) {
            case 1:
                System.out.println("Which information do you want to create?");
                type = types();
                PIMInterface data = null;
                switch (type) {
                    case "PIR.Text":
                        data = (PIMInterface) Text.create();
                        break;
                    case "PIR.Task":
                        data = (PIMInterface) Task.create();
                        break;
                    default:
                        break;
                }
                kernel.create_PIR(data);
                break;
            case 2:
                System.out.println("Which information do you want to search?");
                type = types();
                kernel.search_PIR(type);
//                switch (type) {
//                    case "PIR.Text":
//
//                        System.out.println("Provide a keyword to narrow down the search.");
//                    default:
//                        break;
//                }
                break;
            case 3:
                System.out.println("System Ended.");
                return -1;
            case 4:
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
                break;
            default:
                System.out.println("Invalid choice. Please choose a valid option.");
        }

        return 1;
    }
    public static void main(String[] args) {
        int cmd = 0;
        while(cmd != -1){
            cmd = home();
        }
        //Tuple textData = new Tuple(1, "Sample Title", "This is a sample content.");
        //PIR.PIMInterface text = kernel.create_PIR("PIR.Text", textData);

    }
}
