import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class addExpense extends JFrame implements ActionListener
{
    private JTextField expenseNameField;
    private JTextField amountField;
    private JTextField dateField;
    public String currentUser;
    Connection con=null;
    public addExpense(String currentUsername,Connection c)
    {
        con=c;
        currentUser=currentUsername;
        setTitle("Add Expense");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));
        setVisible(true);
        JLabel expenseNameLabel = new JLabel("Enter the expense name:");
        JLabel amountLabel = new JLabel("Enter the amount spent:");
        JLabel dateLabel = new JLabel("Enter the date of expense (YYYY-MM-DD):");

        expenseNameField = new JTextField();
        amountField = new JTextField();
        dateField = new JTextField();

        add(expenseNameLabel);
        add(expenseNameField);
        add(amountLabel);
        add(amountField);
        add(dateLabel);
        add(dateField);

        JButton addButton = new JButton("Add");
        add(addButton);
        addButton.addActionListener(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        String expenseName = expenseNameField.getText();
        int amount = Integer.parseInt(amountField.getText());
        String dateInput = dateField.getText();
        try {
            add(expenseName, amount, dateInput);
            System.out.println("Expense added successfully!");
            dispose();
            new openUserMenu(currentUser,con);
        } catch (Exception ex) {
            System.out.println("Error adding expense: " + ex.getMessage());
        }
    }
    public void add(String expenseName, int amount, String dateInput) throws Exception
    {
        String query = "INSERT INTO " + currentUser + "_expenses VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, expenseName);
        stmt.setInt(2, amount);
        stmt.setDate(3, java.sql.Date.valueOf(dateInput));
        stmt.executeUpdate();
    }
}
