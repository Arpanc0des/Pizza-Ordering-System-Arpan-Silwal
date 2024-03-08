import com.example.pizza_ordering_system.LandingController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class LandingControllerTest {

    @Test
    public void testCalculateTotalBill() {
        LandingController controller = new LandingController();

        // Test case 2: Large pizza with 3 toppings
        controller.comboBoxId.setValue("L");
        controller.toppingsField.setText("3");
        double expectedBill2 = Math.round((12.00 + (3 * 1.50)) * 1.15);
        assertEquals(expectedBill2, controller.calculateTotalBill());

        // Test case 3: Extra Large pizza with 5 toppings
        controller.comboBoxId.setValue("XL");
        controller.toppingsField.setText("5");
        double expectedBill3 = Math.round((15.00 + (5 * 1.50)) * 1.15);
        assertEquals(expectedBill3, controller.calculateTotalBill());

        // Test case 4: Invalid pizza size
        controller.comboBoxId.setValue("Invalid");
        controller.toppingsField.setText("2");
        assertEquals(0.0, controller.calculateTotalBill()); // Expected total bill should be 0
    }
}
