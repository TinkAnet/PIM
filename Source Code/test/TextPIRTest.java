import PIR.Text;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TextPIRTest {


    String[] tx1,tx2;
    @Before
    public void prepareText(){
        tx1 = new String[]{"Reminder", "Buy groceries"};
        tx2 = new String[]{"Remember", "To finish your homework"};
    }

    @Test
    public void testDefaultTextCreation() {
        Text text = new Text();
        assertNotNull(text.getTitles());
        assertNotNull(text.getData());
    }

    @Test
    public void testTextCreation1() {
        Text text = new Text(tx1[0], tx1[1]);
        assertEquals(tx1[0], text.getData()[0]);
        assertEquals(tx1[1], text.getData()[1]);
    }


    @Test
    public void testGetTitles() {
        Text text = new Text();
        assertNotNull( text.getTitles());
        assertTrue(text.getTitles().containsKey("Title") && text.getTitles().containsKey("Note"));
    }

    @Test
    public void testSetData() {
        Text text = new Text();
        text.setData(tx1);
        assertArrayEquals(tx1, text.getData());
    }

    @Test
    public void testSetNextId() {
        Text text = new Text();
        text.setNextId(10);
        assertEquals(Integer.valueOf(10), text.getNexId());
    }

    @Test
    public void testGetID() {
        Text text = new Text();
        text.setID(456);
        assertEquals( 456, text.getID());
    }

    @Test
    public void testBoundaryValueLength() {
        Text text = new Text("", "");
        assertEquals("", text.getData()[0]);
        assertEquals("", text.getData()[1]);
    }

    @Test
    public void testForHandlingNullInput() {
        Text text = new Text(null, null);
        assertNull(text.getData()[0]);
        assertNull(text.getData()[1]);
    }

    @Test
    public void testRepetitionOfInputs() {
        Text text = new Text("Repeated", "Repeated");
        text.setNextId(10);
        text.setNextId(10);
        assertEquals(Integer.valueOf(10), text.getNexId());
    }

}
