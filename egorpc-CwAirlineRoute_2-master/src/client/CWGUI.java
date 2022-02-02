package client;

import both.AirlinesTable;
import both.AirportsTable;
import both.RoutesTable;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class CWGUI extends JFrame  {
    public String getSearchBox;
    protected JTable dataTable;
    protected JTextField searchField;
    protected JComboBox<String> searchForBOX;
    protected JButton searchBTN;
    protected JButton saveBTN;
    protected JButton deleteBTN;
    protected JButton connectBTN;
    protected JComboBox<String> currentTableBOX;
    protected JButton getAllBTN;
    protected JRadioButton deleteLocallyRadioButton;
    protected JRadioButton deleteOnServerRadioButton;
    protected JButton clearTableBTN;
    protected JLabel serverTimeLBL;
    AirlinesTable airlinesTable = new AirlinesTable();
    AirportsTable airportsTable = new AirportsTable();
    RoutesTable routesTable = new RoutesTable();
    private JPanel mainPanel;
    private JButton addBTN;
    private JScrollPane bodyPanel;
    private JLabel serverStatusLBL;
    private JLabel headerTitle;
    private JPanel footerPanel;
    private JScrollPane tableScroll;
    private JPanel topPanel;


    // Objects for interaction with client object
    private final ClientConnection guiClient;

    public CWGUI() {
        this.add(mainPanel);

        // Display and Re-Size Contents:
        this.setMinimumSize(new Dimension(850, 650));//set the window minimum size
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //section used for getting each table model
        dataTable.setModel(airlinesTable); //default table

        JComboBox<String> topicAirlinesTable = new JComboBox<>(airlinesTable.getColumnNames());
        JComboBox<String> topicAirportsTable = new JComboBox<>(airportsTable.getColumnNames());
        JComboBox<String> topicRoutesTable = new JComboBox<>(routesTable.getColumnNames());

        searchForBOX.setModel(topicAirlinesTable.getModel());

        String[] tablesAvailable = {"Airlines", "Airports", "Routes"};

        //all the tables available are shown on the dropdown box
        JComboBox<String> tablesAvailableBOX = new JComboBox<>(tablesAvailable);
        currentTableBOX.setModel(tablesAvailableBOX.getModel());

        //button group for the delete operation
        ButtonGroup deleteOpGroup = new ButtonGroup();
        deleteOpGroup.add(deleteLocallyRadioButton);
        deleteOpGroup.add(deleteOnServerRadioButton);
        deleteLocallyRadioButton.setSelected(true);

        addBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //add row using default constructor
                int selectedIndex = currentTableBOX.getSelectedIndex();
                if (selectedIndex == 0) {
                    airlinesTable.addRow();
                } else if (selectedIndex == 1) {
                    airportsTable.addRow();
                } else if (selectedIndex == 2) {
                    routesTable.addRow();
                }
                refreshTables();
            }
        });

        //Depending on what table is currently selected on the tables dropdown box, it will show the correspondent table
        currentTableBOX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(Objects.requireNonNull(currentTableBOX.getSelectedItem()).toString());

                switch (currentTableBOX.getSelectedIndex()) {
                    case 0 -> {
                        dataTable.setModel(airlinesTable); //default table
                        searchForBOX.setModel(topicAirlinesTable.getModel());
                    }
                    case 1 -> {
                        dataTable.setModel(airportsTable); //default table
                        searchForBOX.setModel(topicAirportsTable.getModel());
                    }
                    case 2 -> {
                        dataTable.setModel(routesTable); //default table
                        searchForBOX.setModel(topicRoutesTable.getModel());
                    }
                }
                searchField.setText("");
                refreshTables();
            }
        });


    }

    /**
     * shows the correct connection Status on the Client
     */
    public void setConnectionStatus(ConnectionStatus status) {
        serverStatusLBL.setText("Status: " + status.toString());
        if (status == ConnectionStatus.Connected) {
            connectBTN.setText("Disconnect");
        } else {
            connectBTN.setText("Connect");
        }
    }

    /**
     * Method deletes all the elements on the table that is currently being shown
     */
    public void cleanCurrentTable() {
        int selectedIndex = currentTableBOX.getSelectedIndex();
        if (selectedIndex == 0) {
            airlinesTable.clearTable();
        } else if (selectedIndex == 1) {
            airportsTable.clearTable();
        } else if (selectedIndex == 2) {
            routesTable.clearTable();
        }

        refreshTables();

    }

    /**
     * Update the tables for showing the latest data
     */
    public void refreshTables() {
        dataTable.revalidate();
        dataTable.repaint();
    }




}
