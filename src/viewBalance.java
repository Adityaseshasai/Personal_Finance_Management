import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class viewBalance extends JFrame
{
    String current_User;
    Connection con=null;
    public viewBalance(String currentUsername, Connection c)
    {
        con=c;
        current_User=currentUsername;
        setTitle("Balance");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        int balance=0;
        try
        {
            balance = calculate();
        }
        catch(Exception e)
        {
            System.out.println("error");
        }
        finally
        {
            JLabel balanceLabel = new JLabel("Your total net balance is: " + balance);

            add(balanceLabel);
            JButton menu = new JButton("Menu");
            add(menu);
            menu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new openUserMenu(current_User,con);
                }
            });
            setVisible(true);
        }

    }
    public int calculate() throws Exception
    {
        String incomeQuery = "SELECT SUM(Amount) FROM " + current_User + "_income";
        String expenseQuery = "SELECT SUM(Amount) FROM " + current_User + "_expenses";
        PreparedStatement stmt1 = con.prepareStatement(incomeQuery);
        PreparedStatement stmt2 = con.prepareStatement(expenseQuery);
        ResultSet rs1 = stmt1.executeQuery();
        ResultSet rs2 = stmt2.executeQuery();
        rs1.next();
        rs2.next();
        int totalIncome = rs1.getInt("SUM(Amount)");
        int totalExpense = rs2.getInt("SUM(Amount)");
        return totalIncome - totalExpense;
    }
}
