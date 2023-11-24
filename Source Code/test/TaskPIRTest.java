import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import PIR.Task;

public class TaskPIRTest {

    String[] taskData1, taskData2;
    @Before
    public void prepareTask(){
        taskData1 = new String[]{"Complete Report", "Finish the monthly report", "20230110"};
        taskData2 = new String[]{"Meeting with Team", "Weekly sync-up meeting", "20230115"};
    }

    @Test
    public void testTaskCreation0() {
        Task task = new Task();
        assertNotNull(task.getTitles());
        assertNotNull(task.getData());
        assertTrue(task.getTitles().containsKey("Title") && task.getTitles().containsKey("Description") && task.getTitles().containsKey("DueDate"));
    }

    @Test
    public void testTaskCreation1() {
        Task task = new Task(taskData1[0], taskData1[1], taskData1[2]);
        assertEquals(taskData1[0], task.getData()[0]);
        assertEquals(taskData1[1], task.getData()[1]);
        assertEquals(taskData1[2], task.getData()[2]);
    }

    @Test
    public void testGetTitles() {
        Task task = new Task();
        assertNotNull(task.getTitles());
        assertTrue(task.getTitles().containsKey("Title") && task.getTitles().containsKey("Description") && task.getTitles().containsKey("DueDate"));
    }

    @Test
    public void testSetData() {
        Task task = new Task();
        task.setData(taskData1);
        assertArrayEquals(taskData1, task.getData());
    }

    @Test
    public void testIncrementIdOnCreation() {
        Task task1 = new Task(taskData1[0], taskData1[1], taskData1[2]);
        Task task2 = new Task(taskData2[0], taskData2[1], taskData2[2]);
        assertNotEquals("IDs should not be the same after creation of each task.", task1.getID(), task2.getID());
    }

    @Test
    public void testSetNextId() {
        Task task = new Task();
        task.setNextId(10);
        assertEquals(Integer.valueOf(10), task.getNexId());
    }

    @Test
    public void testGetDateFormat() {
        assertEquals("yyyyMMdd", Task.getDateFormat());
    }

    @Test
    public void testBoundaryValueLength() {
        Task task = new Task("", "", "");
        assertEquals("", task.getData()[0]);
        assertEquals("", task.getData()[1]);
        assertEquals("", task.getData()[2]);
    }

    @Test
    public void testForHandlingNullInput() {
        Task task = new Task(null, null, null);
        assertNull(task.getData()[0]);
        assertNull(task.getData()[1]);
        assertNull(task.getData()[2]);
    }

    @Test
    public void testRepetitionOfInputs() {
        Task task = new Task("Repeated", "Repeated", "20230101");
        task.setNextId(10);
        task.setNextId(10);
        assertEquals(Integer.valueOf(10), task.getNexId());
    }

}
