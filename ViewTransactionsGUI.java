import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewTransactionsGUI extends JFrame {
    private JTable transactionTable;
    private DatabaseManager dbManager;
    private String username;

    public ViewTransactionsGUI(DatabaseManager dbManager, String username) {
        this.dbManager = dbManager;
        this.username = username;
        setTitle("View Transactions");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"Date", "Category", "Amount (₹)", "Type"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> dispose());
        add(backButton, BorderLayout.SOUTH);

        loadTransactions();
        setLocationRelativeTo(null);
    }

    private void loadTransactions() {
        List<Transaction> transactions = dbManager.getTransactions(username);
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0);
        for (Transaction t : transactions) {
            model.addRow(new Object[]{t.getDate(), t.getCategory(), String.format("₹%.2f", Math.abs(t.getAmount())), t.getAmount() > 0 ? "Income" : "Expense"});
        }
    }
}