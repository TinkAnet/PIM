import PIR.Text;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import PIR.Contact;
public class ContactPIRTest {

    String[] contact1, contact2;
    @Before
    public void prepareContact(){
        contact1 = new String[]{"Alice", "alice@example.com", "1234567890"};
        contact2 = new String[]{"Bob", "bob@example.com", "0987654321"};
    }

    @Test
    public void testContactCreation0() {
        Contact contact = new Contact();
        assertNotNull(contact.getTitles());
        assertNotNull(contact.getData());
        assertTrue(contact.getTitles().containsKey("Name") && contact.getTitles().containsKey("Email") && contact.getTitles().containsKey("Phone Number"));
    }

    @Test
    public void testContactCreation1() {
        Contact contact = new Contact(contact1[0], contact1[1], contact1[2]);
        assertEquals(contact1[0], contact.getData()[0]);
        assertEquals(contact1[1], contact.getData()[1]);
        assertEquals(contact1[2], contact.getData()[2]);
    }

    @Test
    public void testGetTitles() {
        Contact contact = new Contact();
        assertNotNull(contact.getTitles());
        assertTrue(contact.getTitles().containsKey("Name") && contact.getTitles().containsKey("Email") && contact.getTitles().containsKey("Phone Number"));
    }

    @Test
    public void testSetData() {
        Contact contact = new Contact();
        contact.setData(contact1);
        assertArrayEquals(contact1, contact.getData());
    }

    @Test
    public void testSetNextId() {
        Contact contact = new Contact();
        contact.setNextId(10);
        assertEquals(Integer.valueOf(10), contact.getNexId());
    }

    @Test
    public void testGetID() {
        Contact contact = new Contact();
        contact.setID(456);
        assertEquals(456, contact.getID());
    }

    @Test
    public void testBoundaryValueLength() {
        Contact contact = new Contact("", "", "");
        assertEquals("", contact.getData()[0]);
        assertEquals("", contact.getData()[1]);
        assertEquals("", contact.getData()[2]);
    }

    @Test
    public void testForHandlingNullInput() {
        Contact contact = new Contact(null, null, null);
        assertNull(contact.getData()[0]);
        assertNull(contact.getData()[1]);
        assertNull(contact.getData()[2]);
    }

    @Test
    public void testRepetitionOfInputs() {
        Contact contact = new Contact("Repeated", "repeated@example.com", "1234567890");
        contact.setNextId(10);
        contact.setNextId(10);
        assertEquals(Integer.valueOf(10), contact.getNexId());
    }

}

