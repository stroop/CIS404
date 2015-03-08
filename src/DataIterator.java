/*
  DataIterator.java
  Jacob Stroop
  Bellevue University
  CIS 404 - Assignment 2.2
  March 2, 2015

  Application displays a GUI with address information,
  which is obtained from an outside data source.
  Demonstrates the use of a database connection to
  step through results returned from a query
*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataIterator extends JFrame {

    // Constants for database connection
    static final String CONNECT_STRING = "jdbc:oracle:thin:@192.168.1.106:1521:XE";
    static final String USER_ID = "student1";
    static final String PASSWORD = "pass";
    static final String QUERY = "SELECT * FROM address";

    // Establish GUI components:
    private JButton buttonPrev = new JButton("Prev");
    private JButton buttonReset = new JButton("Reset");
    private JButton buttonNext = new JButton("Next");

    private JLabel labelHeader = new JLabel("Database Browser", JLabel.CENTER);
    private JLabel labelName = new JLabel("Name");
    private JLabel labelAddress = new JLabel("Address");
    private JLabel labelCity = new JLabel("City");
    private JLabel labelState = new JLabel("State");
    private JLabel labelZip = new JLabel("Zip");

    private JTextField textFieldName = new JTextField();
    private JTextField textFieldAddress = new JTextField();
    private JTextField textFieldCity = new JTextField();
    private JTextField textFieldState = new JTextField();
    private JTextField textFieldZip = new JTextField();

    // Declare array
    DataClass[] DataClassArray;
    // Pointer used to determine current array position
    int arrayPointer = 0;

    // Replace default constructor
    public DataIterator() {
        super();
    }

    // Constructor with title argument
    public DataIterator(String title) {
        super(title);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPanel cp = (JPanel) getContentPane();
        labelHeader.setFont(new Font("TimesRoman", Font.BOLD, 24));

        // Setting bounds on GUI components is an alternative to using a LayoutManager
        // It's a lot of work, but gives total control over where components appear
        labelHeader.setBounds(40, 10, 300, 50);

        buttonPrev.setBounds(30, 250, 80, 25);
        buttonReset.setBounds(150, 250, 80, 25);
        buttonNext.setBounds(270, 250, 80, 25);

        labelName.setBounds(10, 80, 80, 25);
        labelAddress.setBounds(10, 110, 80, 25);
        labelCity.setBounds(10, 140, 80, 25);
        labelState.setBounds(10, 170, 80, 25);
        labelZip.setBounds(10, 200, 80, 25);

        textFieldName.setBounds(120, 80, 250, 25);
        textFieldAddress.setBounds(120, 110, 250, 25);
        textFieldCity.setBounds(120, 140, 250, 25);
        textFieldState.setBounds(120, 170, 250, 25);
        textFieldZip.setBounds(120, 200, 250, 25);

        // Setting Layout to null.  If this is not done, a BorderLayout is automatically
        // provided for the container. Since we're setting bounds, it must be overridden
        cp.setLayout(null);

        // Add the GUI components
        cp.add(labelHeader);
        cp.add(buttonPrev);
        cp.add(buttonReset);
        cp.add(buttonNext);
        cp.add(labelName);
        cp.add(textFieldName);
        cp.add(labelAddress);
        cp.add(textFieldAddress);
        cp.add(labelCity);
        cp.add(textFieldCity);
        cp.add(labelState);
        cp.add(textFieldState);
        cp.add(labelZip);
        cp.add(textFieldZip);

        // addWindowListener is a method of JFrame. This catches the event when you
        // click the X icon in the upper right corner of the frame, enter an Alt-F4
        // or otherwise kill the window.

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                shutDown();
            }
        });

        // ActionListener for "previous" button
        buttonPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // if the pointer is not at the first position
                // in the array, decrement by one
                if (arrayPointer > 0) {
                    --arrayPointer;
                }

                // else, if the pointer is at the first position
                // of the array, go to the last position
                else {
                    arrayPointer = DataClassArray.length - 1;
                }
                setFields(arrayPointer);
            }
        });

        // Added ActionListener for Next Button
        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // if the pointer is not at the last position
                // in the array, increment by one
                if (arrayPointer < DataClassArray.length - 1) {
                    ++arrayPointer;
                }

                // else, if the pointer is at the last position
                // in the array, go to the first position
                else {
                    arrayPointer = 0;
                }

                // Call method to update fields according to position of pointer
                setFields(arrayPointer);
            }
        });

        // Added ActionListener for Reset button
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Set pointer to 0
                arrayPointer = 0;

                // Update fields
                setFields(arrayPointer);
            }
        });
    }

    // Method used to fill DataClassArray from database
    private void fillArray() {

        try {

            // Register Oracle Driver
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

            // Connect to database using connection string, user id, and password according to constants
            Connection conn = DriverManager.getConnection(CONNECT_STRING, USER_ID, PASSWORD);

            // Statement with arguments to allow moving backwards through results
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Query database using QUERY provided as constant
            ResultSet rs = stmt.executeQuery(QUERY);

            // int for total rows returned by query
            int rowCount = 0;

            // while loop iterates through all rows returned
            // and increments rowCount
            while (rs.next()) {
                rowCount++;
            }

            // Initialize size of DataClassArray using rowCount
            DataClassArray = new DataClass[rowCount];

            // set cursor back to beginning of result set
            rs.beforeFirst();

            // While loop through all records from query
            for (int i = 0; rs.next(); i++) {

                // For each record, get assign the field data to String variables
                String name = rs.getString("FIRSTNAME") + " " + rs.getString("LASTNAME");
                String address = rs.getString("STREET");
                String city = rs.getString("CITY");
                String state = rs.getString("STATE");
                String zip = rs.getString("ZIP");

                // Create new DataClass object using variables from record and add to array
                DataClassArray[i] = new DataClass(name, address, city, state, zip);

            }

            // Close connection
            stmt.close();
            conn.close();
        }

        catch (Exception e) {

            // Using method from example to print stack trace to dialog if exception is thrown
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            JOptionPane.showConfirmDialog(null, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    // Method assigns text fields from DataClass object properties,
    // depending on the position of the array, as provided by actionlistener call
    private void setFields(int position) {

        textFieldName.setText(DataClassArray[position].getName());
        textFieldAddress.setText(DataClassArray[position].getAddress());
        textFieldCity.setText(DataClassArray[position].getCity());
        textFieldState.setText(DataClassArray[position].getState());
        textFieldZip.setText(DataClassArray[position].getZip());
    }

    // Method to run to quit. You could also get here via an "Exit" button, if
    // you wanted to add one.
    private void shutDown() {
        int returnVal = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?");
        if (returnVal == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Method handles creating GUI and calling appropriate methos
    public static void createAndShowGUI() {
        // Intantiate new DataIterator object
        DataIterator a2 = new DataIterator("Database Browser");

        // Set JFrame size and visibility
        a2.setSize(400, 350);
        a2.setVisible(true);

        // Call method to fill array from database
        a2.fillArray();

        // Call method on start to fill fields from initial position in database
        a2.setFields(0);
    }

    /* Main method
     * This is Oracle's standard way of starting a GUI application
     */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}