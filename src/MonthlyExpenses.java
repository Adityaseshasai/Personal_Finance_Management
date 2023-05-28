import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.sql.Connection;

public class MonthlyExpenses extends JFrame implements ActionListener
{
    private JTextField monthField;
    private JTextField yearField;
    public String user_name;
    Connection con=null;
    public MonthlyExpenses(String currentUsername,Connection c) {
        con=c;
        setTitle("Monthly Expenses ");
        setSize(600, 300);
        user_name=currentUsername;
        setLayout(new FlowLayout());

        JLabel monthLabel = new JLabel("Enter the month (1-12):");
        monthField = new JTextField(10);
        JLabel yearLabel = new JLabel("Enter the year:");
        yearField = new JTextField(10);

        add(monthLabel);
        add(monthField);
        add(yearLabel);
        add(yearField);

        JButton viewButton = new JButton("View");
        add(viewButton);
        viewButton.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e)
    {
        dispose();
        int month = Integer.parseInt(monthField.getText());
        int year = Integer.parseInt(yearField.getText());
        try {
            JScrollPane scrollPane = viewMonthlyExpenses(month,year);
            JFrame tableFrame = new JFrame("Income Table");
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JButton menu = new JButton("Menu");
            tableFrame.setLayout(new FlowLayout());
            tableFrame.add(scrollPane);
            tableFrame.add(menu);
            tableFrame.pack();
            tableFrame.setVisible(true);
            menu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tableFrame.dispose();
                    new openUserMenu(user_name,con);
                }
            });
        } catch (Exception ex) {
            System.out.println("Error occurred while viewing expense table."+ ex.getMessage());
        }
    }
    public JScrollPane viewMonthlyExpenses(int month, int year) throws Exception {
        String query = "SELECT * FROM " + user_name + "_expenses WHERE MONTH(Date_of_expense) = ? AND YEAR(Date_of_expense) = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, month);
        stmt.setInt(2, year);
        ResultSet rs = stmt.executeQuery();

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = metaData.getColumnLabel(i + 1);
        }

        ArrayList<Object[]> rows = new ArrayList<>();
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                rowData[i] = rs.getObject(i + 1);
            }
            rows.add(rowData);
        }

        Object[][] data = new Object[rows.size()][columnCount];
        rows.toArray(data);

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        return scrollPane;
    }
}
