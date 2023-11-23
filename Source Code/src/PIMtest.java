import PIR.Contact;
import PIR.Event;
import PIR.Task;
import PIR.Text;
import PIR.PIRInterface;
import org.junit.Test;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class PIMtest {

    @Test
    public void testContactCreation() {
        // 创建一个联系人
        Contact contact = new Contact("John Doe", "john.doe@example.com", "1234567890");

        // 检查数据是否正确设置
        assertEquals("John Doe", contact.getData()[0]);
        assertEquals("john.doe@example.com", contact.getData()[1]);
        assertEquals("1234567890", contact.getData()[2]);

        // ID 应该自动递增，假设它从1开始
        assertEquals(1, contact.getID());
    }

    @Test
    public void testEventCreation() {
        Event event = new Event("Meeting", "Discuss Project", "2023-01-01 10:00", "2023-01-01 09:30");

        assertEquals("Meeting", event.getData()[0]);
        assertEquals("Discuss Project", event.getData()[1]);
        assertEquals("2023-01-01 10:00", event.getData()[2]);
        assertEquals("2023-01-01 09:30", event.getData()[3]);
        assertEquals(1, event.getID());
    }

    @Test
    public void testTaskCreation() {
        Task task = new Task("Complete Report", "Finish the monthly report", "2023-01-10");

        assertEquals("Complete Report", task.getData()[0]);
        assertEquals("Finish the monthly report", task.getData()[1]);
        assertEquals("2023-01-10", task.getData()[2]);
        assertEquals(1, task.getID());
    }

    @Test
    public void testTextCreation() {
        Text text = new Text("Reminder", "Buy groceries");

        assertEquals("Reminder", text.getData()[0]);
        assertEquals("Buy groceries", text.getData()[1]);
        assertEquals(1, text.getID());
    }

    ////////////////////test for PIMKernal/////////////////////

//    @Test
//    public void testCreatePIR() {
//        PIMKernel kernel = new PIMKernel();
//
//        kernel.create_PIR("Text");
//        Map<String, Map<Integer, PIRInterface>> items = kernel.getPimItems();
//        assertFalse("Map should not be empty after adding Text", items.isEmpty());
//        assertTrue("Map should contain Text type", items.containsKey("Text"));
//
//        // Similarly, you can test for other types like "Task", "Event", "Contact"
//    }

    @Test
    public void testModifyPIR() {
        // Assuming you have a method to add or create a PIR
        PIMKernel kernel = new PIMKernel();
        PIRInterface pir = new Text("Sample", "Content");
        kernel.getPimItems().put("Text", new HashMap<>());
        kernel.getPimItems().get("Text").put(pir.getID(), pir);

        String[] newData = {"New Title", "New Content"};
        kernel.modifyPIR(pir, newData);

        PIRInterface modifiedPIR = kernel.getPimItems().get("Text").get(pir.getID());
        assertArrayEquals(newData, modifiedPIR.getData());
    }

    @Test
    public void testDeletePIR() {
        // Create a PIR to delete
        PIMKernel kernel = new PIMKernel();
        PIRInterface pir = new Text("Sample", "Content");
        kernel.getPimItems().put("Text", new HashMap<>());
        kernel.getPimItems().get("Text").put(pir.getID(), pir);

        kernel.deletePIR(pir, "Text");

        assertFalse(kernel.getPimItems().get("Text").containsKey(pir.getID()));
    }

    @Test
    public void testUpdateByKeyword() {
        // Add some PIRs for testing
        PIMKernel kernel = new PIMKernel();
        PIRInterface pir1 = new Text("Sample", "Content");
        PIRInterface pir2 = new Text("Example", "More Content");
        Map<Integer, PIRInterface> allPIRs = new HashMap<>();
        allPIRs.put(pir1.getID(), pir1);
        allPIRs.put(pir2.getID(), pir2);

        Map<Integer, PIRInterface> updatedPIRs = kernel.updateByKeyword(allPIRs, allPIRs, "sample", "and");

        assertTrue(updatedPIRs.containsKey(pir1.getID()));
        assertFalse(updatedPIRs.containsKey(pir2.getID()));
    }

    private PIMKernel createDummyKernel() {
        PIMKernel kernel = new PIMKernel();

        // Create dummy Text PIR
        Text textPIR = new Text();
        textPIR.setData(new String[]{"Text Title", "Text Content"});
        kernel.getPimItems().computeIfAbsent("Text", k -> new HashMap<>()).put(textPIR.getID(), textPIR);

        // Create dummy Task PIR
        Task taskPIR = new Task();
        taskPIR.setData(new String[]{"Task Title", "Task Description", "2023-01-01"});
        kernel.getPimItems().computeIfAbsent("Task", k -> new HashMap<>()).put(taskPIR.getID(), taskPIR);

        // Create dummy Event PIR
        Event eventPIR = new Event();
        eventPIR.setData(new String[]{"Event Title", "Event Description", "2023-01-01 10:00", "2023-01-01 09:00"});
        kernel.getPimItems().computeIfAbsent("Event", k -> new HashMap<>()).put(eventPIR.getID(), eventPIR);

        // Create dummy Contact PIR
        Contact contactPIR = new Contact();
        contactPIR.setData(new String[]{"Contact Name", "contact@email.com", "1234567890"});
        kernel.getPimItems().computeIfAbsent("Contact", k -> new HashMap<>()).put(contactPIR.getID(), contactPIR);

        return kernel;
    }

    @Test
    public void testCompareDates() {
        PIMKernel kernel = new PIMKernel();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = sdf.parse("2023-01-01");
            Date date2 = sdf.parse("2023-01-01");

            // Assuming '1' represents the option for checking if two dates are equal
            boolean result = kernel.compareDates(date1, date2, 1);
            assertTrue("Dates should be equal", result);

            // You can add more assertions here for other cases
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateByDate() {
        PIMKernel kernel = new PIMKernel();
        Map<Integer, PIRInterface> currentTasks = new HashMap<>();

        // Create and add dummy Task objects with due dates
        Task task1 = new Task("Task 1", "Description 1", "2023-01-10");
        currentTasks.put(task1.getID(), task1);

        Task task2 = new Task("Task 2", "Description 2", "2023-02-20");
        currentTasks.put(task2.getID(), task2);

        try {
            // Define the search date and pattern
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date searchDate = sdf.parse("2023-02-01");
            String datePattern = "yyyy-MM-dd";

            // Call updateByDate - Example: Search for tasks due after 2023-02-01
            Map<Integer, PIRInterface> filteredTasks = kernel.updateByDate(currentTasks, datePattern, searchDate, 2, 3);

            // Assertions
            assertTrue(filteredTasks.containsKey(task2.getID())); // Expect task2 to be in the result
            assertFalse(filteredTasks.containsKey(task1.getID())); // Expect task1 to not be in the result
        } catch (ParseException e) {
            fail("Failed to parse date: " + e.getMessage());
        }
    }

//    @Test
//    public void testPrint() {
//        // Create a sample collection of PIRInterface items
//        List<PIRInterface> items = new ArrayList<>();
//
//        // Create some sample PIRInterface objects and add them to the collection
//        Text textItem = new Text();
//        //textItem.setID(1);
//        textItem.setData(new String[]{"1", "Sample Text"});
//        items.add(textItem);
//
//        Task taskItem = new Task();
//        //taskItem.setID(2);
//        taskItem.setData(new String[]{"2", "Task 1", "2023-11-30"});
//        items.add(taskItem);
//
//        // Create a map of titles for the PIRInterface objects
//        Map<String, Integer> titles = new HashMap<>();
//        titles.put("ID", 2);
//        titles.put("Title", 10);
//        titles.put("Due Date", 10);
//
//        // Create an instance of PIMKernel
//        PIMKernel pimKernel = new PIMKernel();
//
//        // Call the print method from the instance
//        Map<Integer, Integer> displayNumberToId = pimKernel.print(items);
//
//        // Verify the result
//        assertEquals(2, displayNumberToId.size());
//
//        // You can add more assertions based on your expected behavior
//    }

    ////////////////////unit test for PIM/////////////////////
    @Test
    public void testTypes() {
        String input = "1\n"; // Simulating user input "1"
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        PIM pim = new PIM();

        // The method 'types()' uses System.in internally, which has been redirected to 'testIn'
        String result = pim.types();

        assertEquals("Text", result);
    }

    @Test
    public void testTypes2() {
        String input = "2\n"; // Simulating user input "1"
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        PIM pim = new PIM();

        // The method 'types()' uses System.in internally, which has been redirected to 'testIn'
        String result = pim.types();

        assertEquals("Task", result);
    }

    @Test
    public void testMoves() {
        String input = "1\n"; // Simulating user input "1"
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        PIM pim = new PIM();
        int result = pim.moves();
        assertEquals(1, result);
    }

    @Test
    public void testHome(){
        String input = "5\n"; // Simulating user input "1"
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        PIM pim = new PIM();
        int result = pim.home();
        assertEquals(-1, result);
    }

    @Test
    public void testSearchPIRWithNullType() {
        PIM pim = new PIM();
        pim.searchPIR(null);
        // Assertions to verify the method returns early
        // This might involve verifying no output to System.out, or checking the state of PIM object
    }

    @Test
    public void testSearchPIRWithHomeType() {
        PIM pim = new PIM();
        pim.searchPIR("home");
        // Similar assertions as above
    }

    @Test
    public void testSearchByDateTime() {
        // Prepare test data
        Map<Integer, PIRInterface> items = new HashMap<>();
        // Add items to the 'items' map

        int task_event_flag = 1; // You can change this based on your test case
        int searchOption = 1; // You can change this based on your test case

        // Mock user input
        String simulatedInput = "20231124\n1\n"; // Enter a date and choose option 1 (Equal)
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Call the function
        Task task = new Task("Complete Report", "Finish the monthly report", "2023-11-24");
        PIM pim = new PIM();
        Map<Integer, PIRInterface> result = pim.searchByDateTime(items, task_event_flag, searchOption);
        System.out.println(result);

        // Verify that the function produced the expected output or result
        // You can assert the contents of 'outputContent' if necessary

    }


    @Test
    public void testSearchByKeyword() {}

}



