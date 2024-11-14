import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BudgetGUI extends JFrame {
    private BudgetTracker budgetTracker;
    private String currentUser;
    private JTabbedPane tabbedPane;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<String> categoryComboBox;
    private JTextField dateField;
    private JPanel inputPanel;
    private JButton addButton;
    private JButton logoutButton;
    private JPanel calendarPanel;
    private JLabel monthYearLabel;
    private JPanel daysPanel;
    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    // Define colors
    private final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private final Color HEADER_COLOR = new Color(70, 130, 180);
    private final Color BUTTON_COLOR = new Color(100, 149, 237);
    private final Color TEXT_COLOR = Color.WHITE;

    private JComboBox<String> dateSelector;
    private JComboBox<String> daySelector;
    private JComboBox<String> monthSelector;
    private JComboBox<String> yearSelector;

    public BudgetGUI(BudgetTracker budgetTracker, String username) {
        this.budgetTracker = budgetTracker;
        this.currentUser = username;
        setTitle("Budget Tracker - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(HEADER_COLOR);
        tabbedPane.addTab("Add Transaction", createAddTransactionPanel());
        tabbedPane.addTab("View Transactions", budgetTracker.getTransactionsPanel());
        tabbedPane.addTab("Summary", budgetTracker.getSummaryPanel());
        tabbedPane.addTab("Calendar", createCalendarPanel());

        logoutButton = new JButton("Logout");
        logoutButton.setBackground(BUTTON_COLOR);
        logoutButton.setForeground(TEXT_COLOR);
        logoutButton.addActionListener(e -> logout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createAddTransactionPanel() {
        inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        addLabelAndField("Description:", descriptionField = new JTextField(10));
        addLabelAndField("Amount:", amountField = new JTextField(10));
        addLabelAndField("Category:", categoryComboBox = new JComboBox<>(new String[]{"Income", "Expense"}));
        addLabelAndField("Date (dd-mm-yyyy):", dateField = new JTextField(10));

        addButton = new JButton("Add Transaction");
        addButton.setBackground(BUTTON_COLOR);
        addButton.setForeground(TEXT_COLOR);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(addButton);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addLabelAndField(String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setForeground(HEADER_COLOR);
        inputPanel.add(label);
        inputPanel.add(field);
    }

    private void addTransaction() {
        try {
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = (String) categoryComboBox.getSelectedItem();
            String dateStr = dateField.getText();

            if (description.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date date = dateFormat.parse(dateStr);

            if (category.equals("Expense")) {
                amount = -amount;
            }

            budgetTracker.addTransaction(new Transaction(0, amount, description, category, dateFormat.format(date)));
            budgetTracker.updateTransactionList();
            budgetTracker.updateSummary();
            clearInputFields();
            JOptionPane.showMessageDialog(this, "Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in dd-mm-yyyy format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputFields() {
        descriptionField.setText("");
        amountField.setText("");
        categoryComboBox.setSelectedIndex(0);
        dateField.setText("");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                LoginGUI loginGUI = new LoginGUI(budgetTracker.getDatabaseManager());
                loginGUI.setVisible(true);
                this.dispose();
            });
        }
    }

    private JPanel createCalendarPanel() {
        calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel calendarLabel = new JLabel("Calendar View", SwingConstants.CENTER);
        calendarLabel.setForeground(HEADER_COLOR);
        calendarLabel.setFont(new Font("Arial", Font.BOLD, 18));
        calendarPanel.add(calendarLabel, BorderLayout.NORTH);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2024);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("<<");
        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        JButton nextButton = new JButton(">>");

        controlPanel.add(prevButton);
        controlPanel.add(monthYearLabel);
        controlPanel.add(nextButton);

        // Add date selectors
        daySelector = new JComboBox<>(generateDayList());
        monthSelector = new JComboBox<>(generateMonthList());
        yearSelector = new JComboBox<>(generateYearList());

        daySelector.setPreferredSize(new Dimension(50, 25));
        monthSelector.setPreferredSize(new Dimension(100, 25));
        yearSelector.setPreferredSize(new Dimension(70, 25));

        controlPanel.add(new JLabel("Select Date: "));
        controlPanel.add(daySelector);
        controlPanel.add(monthSelector);
        controlPanel.add(yearSelector);

        // Add OK button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            updateSelectedDate(false);
            showTransactionsForDate(calendar.getTime());
        });
        controlPanel.add(okButton);

        daysPanel = new JPanel(new GridLayout(0, 7));

        calendarPanel.add(controlPanel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);

        prevButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar(false);
        });

        nextButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar(false);
        });

        updateCalendar(false);

        return calendarPanel;
    }

    private String[] generateDayList() {
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i-1] = String.format("%02d", i);
        }
        return days;
    }

    private String[] generateMonthList() {
        return new String[]{"January", "February", "March", "April", "May", "June", 
                            "July", "August", "September", "October", "November", "December"};
    }

    private String[] generateYearList() {
        String[] years = new String[11];
        for (int i = 2024; i <= 2034; i++) {
            years[i-2024] = String.valueOf(i);
        }
        return years;
    }

    private void updateSelectedDate(boolean showTransactions) {
        int day = Integer.parseInt((String) daySelector.getSelectedItem());
        int month = monthSelector.getSelectedIndex();
        int year = Integer.parseInt((String) yearSelector.getSelectedItem());

        calendar.set(year, month, 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        if (day > maxDay) {
            day = maxDay;
            daySelector.setSelectedItem(String.format("%02d", day));
        }

        calendar.set(year, month, day);
        updateCalendar(false);
    }

    private void updateCalendar(boolean showTransactions) {
        daysPanel.removeAll();
        
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        monthYearLabel.setText(new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));

        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : dayNames) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            daysPanel.add(dayLabel);
        }

        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            final int currentDay = day;
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.addActionListener(e -> {
                calendar.set(Calendar.DAY_OF_MONTH, currentDay);
                updateDateSelectors();
                showTransactionsForDate(calendar.getTime());
            });
            daysPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();

        updateDateSelectors();

        // Remove the automatic showing of transactions
        // if (showTransactions) {
        //     showTransactionsForDate(calendar.getTime());
        // }
    }

    private void updateDateSelectors() {
        daySelector.setSelectedItem(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
        monthSelector.setSelectedIndex(calendar.get(Calendar.MONTH));
        yearSelector.setSelectedItem(String.valueOf(calendar.get(Calendar.YEAR)));
    }

    private void showTransactionsForDate(Date date) {
        String dateString = dateFormat.format(date);
        java.util.List<Transaction> transactions = budgetTracker.getTransactionsForDate(dateString);
        
        if (!transactions.isEmpty()) {
            StringBuilder transactionDetails = new StringBuilder();
            transactionDetails.append("Transactions for ").append(dateString).append(":\n\n");
            
            double totalIncome = 0;
            double totalExpense = 0;
            
            for (Transaction t : transactions) {
                if (t.getAmount() > 0) {
                    totalIncome += t.getAmount();
                    transactionDetails.append("Income: ");
                } else {
                    totalExpense += Math.abs(t.getAmount());
                    transactionDetails.append("Expense: ");
                }
                transactionDetails.append(t.getDescription())
                                  .append(" - ₹")
                                  .append(String.format("%.2f", Math.abs(t.getAmount())))
                                  .append("\n");
            }
            
            transactionDetails.append("\nTotal Income: ₹").append(String.format("%.2f", totalIncome));
            transactionDetails.append("\nTotal Expense: ₹").append(String.format("%.2f", totalExpense));
            
            JOptionPane.showMessageDialog(this, transactionDetails.toString(), "Transactions for " + dateString, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
