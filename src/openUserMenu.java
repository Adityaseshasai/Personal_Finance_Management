import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class openUserMenu extends JFrame implements ActionListener
{
    JButton addExpenses;
    JButton addIncomes;
    JButton viewIncome;
    JButton viewExpense;
    JButton viewBalance;
    JButton viewMonthlyExpense;
    JButton exit;
    Connection con=null;
    public String currentUsername;
    public openUserMenu(String currentUName,Connection c)
    {
        con=c;
        currentUsername=currentUName;
        setLayout(new GridLayout(4, 2, 10, 10));

        setTitle("User Menu");
        setSize(400, 200);
        setLocationRelativeTo(null);

        addExpenses = new JButton("Add Expense");
        addIncomes = new JButton("Add Income");
        viewIncome = new JButton("View Income Table");
        viewExpense = new JButton("View Expense Table");
        viewBalance = new JButton("View Balance");
        viewMonthlyExpense = new JButton("View Monthly Expenses");
        exit = new JButton("Exit");

        setLayout(new FlowLayout());

        add(addExpenses);
        add(addIncomes);
        add(viewIncome);
        add(viewExpense);
        add(viewBalance);
        add(viewMonthlyExpense);
        add(exit);
        addExpenses.addActionListener(this);
        addIncomes.addActionListener(this);
        viewBalance.addActionListener(this);
        viewExpense.addActionListener(this);
        viewIncome.addActionListener(this);
        viewMonthlyExpense.addActionListener(this);
        exit.addActionListener(this);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==addExpenses)
        {
            new addExpense(currentUsername,con);
        }
        else if(e.getSource()==addIncomes)
        {
            new addIncome(currentUsername,con);
        }
        else if(e.getSource()==viewIncome)
        {
            try {
                JScrollPane scrollPane = viewIncomeTable();
                JFrame tableFrame = new JFrame("Income Table");
                tableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JButton menu = new JButton("Menu");
                tableFrame.setLayout(new FlowLayout());
                tableFrame.add(scrollPane);
                tableFrame.add(menu);
                tableFrame.pack();
                tableFrame.setVisible(true);
                menu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        tableFrame.dispose();
                        new openUserMenu(currentUsername,con);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error occurred while viewing income table."+ex.getMessage());
            }
        }
        else if(e.getSource()==viewExpense)
        {
            try {
                JScrollPane scrollPane = viewExpenseTable();
                JFrame tableFrame = new JFrame("Income Table");
                tableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JButton menu = new JButton("Menu");
                tableFrame.setLayout(new FlowLayout());
                tableFrame.add(scrollPane);
                tableFrame.add(menu);
                tableFrame.pack();
                tableFrame.setVisible(true);
                menu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        tableFrame.dispose();
                        new openUserMenu(currentUsername,con);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error occurred while viewing expense table."+ex.getMessage());
            }
        }
        else if(e.getSource()==viewBalance)
        {
            new viewBalance(currentUsername,con);
        }
        else if(e.getSource()==viewMonthlyExpense)
        {
            try{
                MonthlyExpenses obj=new MonthlyExpenses(currentUsername,con);
                obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                obj.setVisible(true);
            } catch(Exception ex){
                System.out.println("Error viewing monthly expenses: " + ex.getMessage());
            }
        }
        else if(e.getSource()==exit)
        {
            System.exit(0);
        }
        dispose();
    }
    public JScrollPane viewIncomeTable() throws Exception
    {
        String query = "SELECT * FROM " + currentUsername + "_income";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        Vector<String> columnNames = new Vector<String>();
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnLabel(i));
        }

        while (rs.next())
        {
            Vector<Object> row = new Vector<Object>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }
    public JScrollPane viewExpenseTable() throws Exception {
        String query = "SELECT * FROM " + currentUsername + "_expenses";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<String>();
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();

        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnLabel(i));
        }

        while (rs.next()) {
            Vector<Object> row = new Vector<Object>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }
}
