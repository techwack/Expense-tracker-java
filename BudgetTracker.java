import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BudgetTracker {
	private DatabaseManager dbManager;
	private String currentUser;
	private JTextArea transactionList;
	private JTextArea summaryArea;
	private JPanel transactionsPanel;
	private JPanel summaryPanel;

	private final Color BACKGROUND_COLOR = new Color(240, 240, 240);
	private final Color TEXT_COLOR = new Color(70, 130, 180);

	public BudgetTracker(DatabaseManager dbManager, String username) {
		this.dbManager = dbManager;
		this.currentUser = username;
		this.transactionList = new JTextArea(20, 40);
		this.summaryArea = new JTextArea(10, 40);
		this.transactionsPanel = createTransactionsPanel();
		this.summaryPanel = createSummaryPanel();
		updateTransactionList();
		updateSummary();
		styleComponents();
	}

	// Add this method to get the DatabaseManager
	public DatabaseManager getDatabaseManager() {
		return dbManager;
	}

	public void addTransaction(Transaction transaction) {
		dbManager.addTransaction(currentUser, transaction);
		updateTransactionList();
		updateSummary();
	}

	public JPanel getTransactionsPanel() {
		return transactionsPanel;
	}

	public JPanel getSummaryPanel() {
		return summaryPanel;
	}

	private void styleComponents() {
		transactionList.setBackground(BACKGROUND_COLOR);
		transactionList.setForeground(TEXT_COLOR);
		transactionList.setFont(new Font("Arial", Font.PLAIN, 14));
		
		summaryArea.setBackground(BACKGROUND_COLOR);
		summaryArea.setForeground(TEXT_COLOR);
		summaryArea.setFont(new Font("Arial", Font.BOLD, 14));
	}

	private JPanel createTransactionsPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(BACKGROUND_COLOR);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		transactionList.setEditable(false);
		panel.add(new JScrollPane(transactionList), BorderLayout.CENTER);
		return panel;
	}

	private JPanel createSummaryPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(BACKGROUND_COLOR);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		summaryArea.setEditable(false);
		panel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
		return panel;
	}

	public void updateTransactionList() {
		transactionList.setText("");
		List<Transaction> transactions = dbManager.getTransactions(currentUser);
		for (Transaction t : transactions) {
			transactionList.append(t.toString() + "\n");
		}
	}

	public void updateSummary() {
		summaryArea.setText("");
		double totalIncome = dbManager.getTotalIncome(currentUser);
		double totalExpense = dbManager.getTotalExpense(currentUser);
		double balance = totalIncome + totalExpense;

		summaryArea.append("User: " + currentUser + "\n");
		summaryArea.append("Total Income: ₹" + String.format("%.2f", totalIncome) + "\n");
		summaryArea.append("Total Expense: ₹" + String.format("%.2f", Math.abs(totalExpense)) + "\n");
		summaryArea.append("Balance: ₹" + String.format("%.2f", balance) + "\n");
	}

	public List<Transaction> getTransactionsForDate(String date) {
		List<Transaction> transactionsForDate = new ArrayList<>();
		List<Transaction> allTransactions = dbManager.getTransactions(currentUser);
		for (Transaction t : allTransactions) {
			if (t.getDate().equals(date)) {
				transactionsForDate.add(t);
			}
		}
		return transactionsForDate;
	}
}
