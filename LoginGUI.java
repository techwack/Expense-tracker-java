import java.awt.*;
import javax.swing.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton goToRegisterButton;
    private DatabaseManager dbManager;

    public LoginGUI(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        setTitle("Budget Tracker Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 5, 5)); // Changed back to 4 rows

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        add(loginButton);

        goToRegisterButton = new JButton("Register");
        goToRegisterButton.addActionListener(e -> openRegistrationGUI());
        add(goToRegisterButton);

        // Calendar button removed

        setLocationRelativeTo(null);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (dbManager.authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            openBudgetGUI(username);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistrationGUI() {
        SwingUtilities.invokeLater(() -> {
            RegistrationGUI registrationGUI = new RegistrationGUI(dbManager);
            registrationGUI.setVisible(true);
            this.dispose();
        });
    }

    private void openBudgetGUI(String username) {
        SwingUtilities.invokeLater(() -> {
            BudgetTracker budgetTracker = new BudgetTracker(dbManager, username);
            BudgetGUI gui = new BudgetGUI(budgetTracker, username);
            gui.setVisible(true);
            this.dispose();
        });
    }
}
