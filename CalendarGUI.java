import javax.swing.*;

public class CalendarGUI extends JFrame {
    private BudgetTracker budgetTracker;
    private String username;

    public CalendarGUI(BudgetTracker budgetTracker, String username) {
        this.budgetTracker = budgetTracker;
        this.username = username;

        setTitle("Calendar View - " + username);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add calendar components here
        // You can use JCalendar library or implement your own calendar view

        setLocationRelativeTo(null);
    }
}
