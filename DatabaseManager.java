import java.io.*;
import java.util.*;

public class DatabaseManager {
    private Map<String, List<Transaction>> userTransactions;
    private Map<String, Double> userTotalIncome;
    private Map<String, Double> userTotalExpense;
    private Map<String, String> userCredentials;
    private static final String TRANSACTIONS_FILE = "user_transactions.dat";
    private static final String CREDENTIALS_FILE = "user_credentials.dat";

    public DatabaseManager() {
        this.userTransactions = new HashMap<>();
        this.userTotalIncome = new HashMap<>();
        this.userTotalExpense = new HashMap<>();
        this.userCredentials = new HashMap<>();
        loadTransactions();
        loadCredentials();
    }

    public void addTransaction(String username, Transaction transaction) {
        userTransactions.computeIfAbsent(username, k -> new ArrayList<>()).add(transaction);
        if (transaction.getAmount() > 0) {
            userTotalIncome.merge(username, transaction.getAmount(), Double::sum);
        } else {
            userTotalExpense.merge(username, transaction.getAmount(), Double::sum);
        }
        saveTransactions();
    }

    public List<Transaction> getTransactions(String username) {
        return new ArrayList<>(userTransactions.getOrDefault(username, new ArrayList<>()));
    }

    public double getTotalIncome(String username) {
        return userTotalIncome.getOrDefault(username, 0.0);
    }

    public double getTotalExpense(String username) {
        return userTotalExpense.getOrDefault(username, 0.0);
    }

    public double getTotalExpenses(String username) {
        return Math.abs(userTotalExpense.getOrDefault(username, 0.0));
    }

    public Map<String, Double> getExpenseBreakdown(String username) {
        Map<String, Double> breakdown = new HashMap<>();
        List<Transaction> transactions = userTransactions.getOrDefault(username, new ArrayList<>());
        
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) { // Only consider expenses
                breakdown.merge(t.getCategory(), Math.abs(t.getAmount()), Double::sum);
            }
        }
        
        return breakdown;
    }

    public boolean authenticateUser(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    public boolean registerUser(String username, String password) {
        if (userCredentials.containsKey(username)) {
            return false;
        }
        userCredentials.put(username, password);
        saveCredentials();
        return true;
    }

    private void saveCredentials() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CREDENTIALS_FILE))) {
            oos.writeObject(userCredentials);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCredentials() {
        File file = new File(CREDENTIALS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                userCredentials = (Map<String, String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveTransactions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TRANSACTIONS_FILE))) {
            oos.writeObject(userTransactions);
            oos.writeObject(userTotalIncome);
            oos.writeObject(userTotalExpense);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTransactions() {
        File file = new File(TRANSACTIONS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                userTransactions = (Map<String, List<Transaction>>) ois.readObject();
                userTotalIncome = (Map<String, Double>) ois.readObject();
                userTotalExpense = (Map<String, Double>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
