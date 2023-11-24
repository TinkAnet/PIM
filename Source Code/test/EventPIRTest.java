import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import PIR.Event;
public class EventPIRTest {

    String[] eventData1, eventData2;
    @Before
    public void prepareEvent(){
        eventData1 = new String[]{"Meeting", "Project discussion", "20230110 0900", "15"};
        eventData2 = new String[]{"Conference", "Annual conference", "20230112 1000", "30"};
    }

    @Test
    public void testEventCreation0() {
        Event event = new Event();
        assertNotNull(event.getTitles());
        assertNotNull(event.getData());
        assertTrue(event.getTitles().containsKey("Title") && event.getTitles().containsKey("Description") && event.getTitles().containsKey("Starting Time") && event.getTitles().containsKey("Alarm"));
    }

    @Test
    public void testEventCreation1() {
        Event event = new Event(eventData1[0], eventData1[1], eventData1[2], eventData1[3]);
        assertEquals(eventData1[0], event.getData()[0]);
        assertEquals(eventData1[1], event.getData()[1]);
        assertEquals(eventData1[2], event.getData()[2]);
        assertEquals(eventData1[3], event.getData()[3]);
    }

    @Test
    public void testGetTitles() {
        Event event = new Event();
        assertNotNull(event.getTitles());
        assertTrue(event.getTitles().containsKey("Title") && event.getTitles().containsKey("Description") && event.getTitles().containsKey("Starting Time") && event.getTitles().containsKey("Alarm"));
    }

    @Test
    public void testSetData() {
        Event event = new Event();
        event.setData(eventData1);
        assertArrayEquals(eventData1, event.getData());
    }

    @Test
    public void testSetNextId() {
        Event event = new Event();
        event.setNextId(10);
        assertEquals(Integer.valueOf(10), event.getNexId());
    }

    @Test
    public void testGetDateFormat() {
        assertEquals("yyyyMMdd HHmm", Event.getDateFormat());
    }

    @Test
    public void testBoundaryValueLength() {
        Event event = new Event("", "", "", "");
        assertEquals("", event.getData()[0]);
        assertEquals("", event.getData()[1]);
        assertEquals("", event.getData()[2]);
        assertEquals("", event.getData()[3]);
    }

    @Test
    public void testForHandlingNullInput() {
        Event event = new Event(null, null, null, null);
        assertNull(event.getData()[0]);
        assertNull(event.getData()[1]);
        assertNull(event.getData()[2]);
        assertNull(event.getData()[3]);
    }

    @Test
    public void testRepetitionOfInputs() {
        Event event = new Event("Repeated", "Repeated", "20230101 0000", "5");
        event.setNextId(10);
        event.setNextId(10);
        assertEquals(Integer.valueOf(10), event.getNexId());
    }
}

