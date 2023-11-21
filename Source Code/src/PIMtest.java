import PIR.Contact;
import PIR.Event;
import PIR.Task;
import PIR.Text;
import org.junit.Test;

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

}


