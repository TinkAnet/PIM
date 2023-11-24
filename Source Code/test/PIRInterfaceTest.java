import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import PIR.*;
public class PIRInterfaceTest {
    @Test(expected = InstantiationException.class)
    public void testPIRInterfaceInstantiation() throws Exception {
        PIRInterface.class.newInstance();
    }
    @Test
    public void testPolymorphismWithSubclasses() {
        PIRInterface pir = new Contact("Alice", "alice@example.com", "1234567890");
        assertEquals("Alice", pir.getData()[0]);

        pir = new Event("Meeting", "Project discussion", "20230110 0900", "15");
        assertEquals("Meeting", pir.getData()[0]);

        pir = new Task("Complete Report", "Finish the monthly report", "20230110");
        assertEquals("Complete Report", pir.getData()[0]);

        pir = new Text("Reminder", "Buy groceries");
        assertEquals("Reminder", pir.getData()[0]);
        // Test polymorphic behavior
    }
}

