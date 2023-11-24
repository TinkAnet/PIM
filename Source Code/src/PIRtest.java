import PIR.Contact;
import PIR.Event;
import PIR.Task;
import PIR.Text;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class PIRtest {
    Contact contact ;
    String[] c1, c2, c3, c4, c5;

    @Before
    public void prepare(){
        contact = new Contact("", "", "");
        c1 = new String[]{"John Doe", "john.doe@example.com", "1234567890"};
        c2 = new String[]{"Jan Ashely","jan.ashely@example.com","34395504288"};

    }

    @Test
    public void testContactCreation1() {
        // 创建一个联系人
        Contact contact = new Contact(c1[0], c1[1], c1[2]);

        // 检查数据是否正确设置
        assertEquals(c1[0], contact.getData()[0]);
        assertEquals(c1[1], contact.getData()[1]);
        assertEquals(c1[2], contact.getData()[2]);

        // ID 应该自动递增，假设它从1开始
        assertEquals(1, contact.getID());
    }

    @Test
    public void testContactCreation2() {
        String sequence = String.format("%s\r\n%s\r\n%s\r\n",c1[0],c1[1],c1[2]);
        PrintStream printStream = new PrintStream(new ByteArrayOutputStream());
        System.setIn(new ByteArrayInputStream((sequence).getBytes()));
        System.setOut(printStream);

        Contact contact = new Contact();

        assertEquals(c1[0], contact.getData()[0]);
        assertEquals(c1[1], contact.getData()[1]);
        assertEquals(c1[2], contact.getData()[2]);

        // ID 应该自动递增，假设它从1开始
        assertEquals(2, contact.getID());
    }

    @Test
    public void testContactSetData() {
        contact.setData(c2);

        assertEquals(c2[0], contact.getData()[0]);
        assertEquals(c2[1], contact.getData()[1]);
        assertEquals(c2[2], contact.getData()[2]);
    }

    @Test
    public void testContactSetNextID() {
        Contact.setNextId(0);

        assertEquals(new Contact("","","").getID()-1, 0);

        for(int i = 0; i<10; i++){
            new Contact("","","");
        }

        assertEquals(new Contact("","","").getID()-1, 10);
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


