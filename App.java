import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        Runtime.getRuntime().addShutdownHook(new Thread(dbManager::saveTransactions));

        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI(dbManager);
            loginGUI.setVisible(true);
        });
    }
}

// Remove the Expense and Transaction classes from here
