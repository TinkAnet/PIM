import PIR.Task;
import PIR.Text;
import org.junit.Before;
import org.junit.Test;

import java.nio.BufferOverflowException;
import java.util.Optional;

import static org.junit.Assert.*;

public class TextPIRTest {
    @Test
    public void testTaskCreation() {
        Task task = new Task("Complete Report", "Finish the monthly report", "2023-01-10");

        assertEquals("Complete Report", task.getData()[0]);
        assertEquals("Finish the monthly report", task.getData()[1]);
        assertEquals("2023-01-10", task.getData()[2]);
        assertEquals(1, task.getID());
    }

    String[] tx1,tx2;
    @Before
    public void prepareText(){
        tx1 = new String[]{"Reminder", "Buy groceries"};
        tx2 = new String[]{"Remember", "To finish your homework"};
    }

    @Test
    public void testTextCreation0() {
        Text text = new Text();
        assertNotNull(text.getTitles());
        assertNotNull( text.getData());
    }

    @Test
    public void testTextCreation1() {
        Text text = new Text(tx1[0], tx1[1]);
        assertEquals(tx1[0], text.getData()[0]);
        assertEquals(tx1[1], text.getData()[1]);
    }

    @Test
    public void testTextCreation2() {
        Text text = new Text();
        text.setData(tx1);
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
        String[] newData = {"NewTitle", "NewNote"};
        text.setData(newData);
        assertArrayEquals(newData, text.getData());
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
    public void testErrorMessagesForNull() {
        new Text(null, null);
    }

    @Test(expected = OutOfMemoryError.class)
    public void testInputBufferOverflow() {
        String largeString = new String(new char[Integer.MAX_VALUE]).replace('\0', 'A');
        new Text(largeString, largeString);
    }

    @Test
    public void testRepetitionOfInputs() {
        Text text = new Text("Repeated", "Repeated");
        text.setNextId(10);
        text.setNextId(10);
        assertEquals(Integer.valueOf(10), text.getNexId());
    }

    @Test
    public void testComputationResultsTooLarge() {
        Text text = new Text();
        text.setNextId(Integer.MAX_VALUE);
        text.setNextId(text.getNexId() + 1);
        assertEquals(Integer.valueOf(-Integer.MAX_VALUE-1), text.getNexId());
    }

    @Test
    public void testStressTextObjectsCreation() {
        final int numberOfObjects = 10000; // Adjust as needed for stress level
        for (int i = 0; i < numberOfObjects; i++) {
            Text text = new Text("Title" + i, "Note" + i);
            assertNotNull(text);
        }
    }

}
