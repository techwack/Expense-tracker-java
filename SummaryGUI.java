import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SummaryGUI extends JFrame {
    private DatabaseManager dbManager;
    private String username;

    public SummaryGUI(DatabaseManager dbManager, String username) {
        this.dbManager = dbManager;
        this.username = username;
        setTitle("Budget Summary");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel summaryPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        add(summaryPanel, BorderLayout.CENTER);

        double totalIncome = dbManager.getTotalIncome(username);
        double totalExpenses = dbManager.getTotalExpenses(username);
        double balance = totalIncome - totalExpenses;

        summaryPanel.add(new JLabel("Total Income:"));
        summaryPanel.add(new JLabel(String.format("₹%.2f", totalIncome)));
        summaryPanel.add(new JLabel("Total Expenses:"));
        summaryPanel.add(new JLabel(String.format("₹%.2f", totalExpenses)));
        summaryPanel.add(new JLabel("Current Balance:"));
        summaryPanel.add(new JLabel(String.format("₹%.2f", balance)));

        summaryPanel.add(new JLabel("Expense Breakdown:"));
        summaryPanel.add(new JLabel(""));

        Map<String, Double> expenseBreakdown = dbManager.getExpenseBreakdown(username);
        for (Map.Entry<String, Double> entry : expenseBreakdown.entrySet()) {
            summaryPanel.add(new JLabel(entry.getKey() + ":"));
            summaryPanel.add(new JLabel(String.format("₹%.2f", entry.getValue())));
        }

        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> dispose());
        add(backButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }
}