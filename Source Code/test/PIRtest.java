import PIR.Contact;
import PIR.Event;
import PIR.Task;
import PIR.Text;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class PIRtest {
    Contact contact ;
    String[] c1, c2;



    @Before
    public void prepare(){
        prepareContact();
    }

    public void prepareContact(){
        contact = new Contact("", "", "");
        c1 = new String[]{"John Doe", "john.doe@example.com", "1234567890"};
        c2 = new String[]{"Jan Ashely","jan.ashely@example.com","34395504288"};
    }



    @Test
    public void testContactCreation1() {
        // 创建一个联系人
        Contact contact = new Contact(c1[0], c1[1], c1[2]);

        // 检查数据是否正确设置
        assertArrayEquals(c1, contact.getData());

        // ID 应该自动递增，假设它从1开始
        assertEquals(2, contact.getID());
    }

    @Test
    public void testContactCreation2() {
        String sequence = String.format("%s\r\n%s\r\n%s\r\n",c1[0],c1[1],c1[2]);
        PrintStream printStream = new PrintStream(new ByteArrayOutputStream());
        System.setIn(new ByteArrayInputStream((sequence).getBytes()));
        System.setOut(printStream);

        Contact contact = new Contact();

        assertArrayEquals(c1, contact.getData());

        // ID 应该自动递增，假设它从1开始
        assertEquals(2, contact.getID());
    }

    @Test
    public void testContactSetData() {
        contact.setData(c2);

        assertArrayEquals(c2, contact.getData());
    }

    @Test
    public void testContactSetNextID() {

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




}

