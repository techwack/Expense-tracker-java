import java.awt.*;
import javax.swing.*;

public class RegistrationGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField initialBalanceField;
    private JButton registerButton;
    private JButton backToLoginButton;
    private DatabaseManager dbManager;

    public RegistrationGUI(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        setTitle("Budget Tracker Registration");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);

        add(new JLabel("Initial Balance (₹):"));
        initialBalanceField = new JTextField();
        add(initialBalanceField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());
        add(registerButton);

        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.addActionListener(e -> openLoginGUI());
        add(backToLoginButton);

        setLocationRelativeTo(null);
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String initialBalanceStr = initialBalanceField.getText();

        if (username.isEmpty() || password.isEmpty() || initialBalanceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double initialBalance;
        try {
            initialBalance = Double.parseDouble(initialBalanceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid initial balance", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dbManager.registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Initial balance: ₹" + initialBalance);
            openLoginGUI();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openLoginGUI() {
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI(dbManager);
            loginGUI.setVisible(true);
            this.dispose();
        });
    }
}