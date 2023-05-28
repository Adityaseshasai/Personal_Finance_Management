import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class addIncome extends JFrame implements ActionListener
{
    public String current_User;
    Connection con=null;
    JLabel sourceLabel;
    JLabel amountLabel;
    JLabel dateLabel;
    JTextField sourceField;
    JTextField amountField;
    JTextField dateField;

    JButton addButton;
    public addIncome(String currentUsername, Connection c)
    {
        con=c;
        current_User=currentUsername;
        setTitle("Add Income");
        setSize(400, 200);
        setLocationRelativeTo(null);

        sourceLabel = new JLabel("Enter the income source:");
        amountLabel = new JLabel("Enter the amount earned:");
        dateLabel = new JLabel("Enter the pay date (YYYY-MM-DD):");

        sourceField = new JTextField();
        amountField = new JTextField();
        dateField = new JTextField();

        addButton = new JButton("Add Income");
        setLayout(new GridLayout(4, 2, 10, 10));
        setVisible(true);
        add(sourceLabel);
        add(sourceField);
        add(amountLabel);
        add(amountField);
        add(dateLabel);
        add(dateField);
        add(addButton);
        addButton.addActionListener(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e) {
        String source = sourceField.getText();
        int amount = Integer.parseInt(amountField.getText());
        String dateInput = dateField.getText();
        try {
            add(source, amount, dateInput);
            System.out.println("Income added successfully!");
            dispose();
            new openUserMenu(current_User,con);
        } catch (Exception ex) {
            System.out.println("Error adding income: " + ex.getMessage());
        }
    }
    public void add(String source, int amount, String dateInput) throws Exception
    {
        String query = "INSERT INTO " + current_User + "_income VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, source);
        stmt.setInt(2, amount);
        stmt.setDate(3, java.sql.Date.valueOf(dateInput));
        stmt.executeUpdate();
    }
}
