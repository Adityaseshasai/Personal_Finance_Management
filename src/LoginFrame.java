import java.sql.Date;
import java.util.*;
import java.sql.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class LoginFrame extends JFrame implements ActionListener
{
    JTextField userName;
    JPasswordField pass;
    JButton createNew;
    JButton login;
    JButton exit;
    Connection con = null;
    public String currentUName;
    public LoginFrame()
    {
        setTitle("Finance Tracker");
        setSize(300,200);
        userName = new JTextField(20);
        pass = new JPasswordField(20);
        createNew = new JButton("New Profile");
        login = new JButton("Log In");
        exit = new JButton("Exit");
        setLayout(new FlowLayout());
        add(new JLabel("Username: "));
        add(userName);
        add(new JLabel("Password: "));
        add(pass);
        add(createNew);
        add(login);
        add(exit);

        createNew.addActionListener(this);
        login.addActionListener(this);
        exit.addActionListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public boolean doesTableExist(String tableName) throws SQLException
    {
        Statement statement = con.createStatement();
        try
        {
            String query = "select * from " + tableName ;
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        }
        catch (SQLException e) {
            return false;
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String username = userName.getText();
        String password = new String(pass.getPassword());
        if (e.getSource() == createNew)
        {
            connectToDatabase();
            try
            {
                if (!searchColumn(username))
                {
                    createUserProfile(username, password);
                    createTableForUser(username);
                    add(new JLabel("Profile Created successfully!"));
                } else
                {
                    add(new JLabel("Username already exists! Please choose a different username."));
                }
            }
            catch (Exception ex)
            {
                add(new JLabel("Error creating profile: " + ex.getMessage()));
            }
        }
        else if (e.getSource() == login)
        {
            connectToDatabase();
            try {
                if (validateUser(username, password)) {
                    currentUName = username;
                    System.out.println("Logged in successfully!");
                    new openUserMenu(currentUName,con);
                    setVisible(false);
                } else {
                    System.out.println("Invalid username or password!");
                }
            } catch (Exception ex) {
                System.out.println("Error during login:"+ex.getMessage());
            }
        } else if (e.getSource() == exit) {
            System.exit(0);
        }
    }
    public void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/java_project";
            String user = "root";
            String pwd = " ";
            con = DriverManager.getConnection(url, user, pwd);
            boolean tableExists = doesTableExist("manushulu");
            if (!tableExists) {
                String createTableQuery = "CREATE TABLE manushulu (username VARCHAR(20), password VARCHAR(20))";
                PreparedStatement createTableStmt = con.prepareStatement(createTableQuery);
                createTableStmt.executeUpdate();
            }
            System.out.println("Connection established");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public boolean searchColumn(String userName) throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM manushulu WHERE username=?");
        stmt.setString(1, userName);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    public void createUserProfile(String username, String password) throws SQLException
    {
        String query = "INSERT INTO manushulu (username, password) VALUES (?, ?)";
        PreparedStatement insertStmt = con.prepareStatement(query);
        insertStmt.setString(1, username);
        insertStmt.setString(2, password);
        insertStmt.executeUpdate();
    }

    public void createTableForUser(String username) throws SQLException {
        String query1 = "CREATE TABLE " + username + "_income (Source VARCHAR(20), Amount INTEGER, Date_of_income DATE)";
        String query2 = "CREATE TABLE " + username + "_expenses (Expense_name VARCHAR(20), Amount INTEGER, Date_of_expense DATE)";
        PreparedStatement stmt1 = con.prepareStatement(query1);
        PreparedStatement stmt2 = con.prepareStatement(query2);
        stmt1.executeUpdate();
        stmt2.executeUpdate();
    }
    public boolean validateUser(String username, String password) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM manushulu WHERE username=? AND password=?");
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
}

