package client;

import both.Command;
import both.Parcel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class ClientConnection extends CWGUI {

    private final Object waitObject = new Object();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");//format for showing server time
    private ObjectOutputStream objectOutputStream; // Output stream to server
    private ObjectInputStream objectInputStream; // Input stream from server
    private ConnectionStatus connectionStatus = ConnectionStatus.Disconnected;
    private Socket socket;

    private boolean isFirst;//only displays a fail message once at a time


    public ClientConnection() {
        super();//makes sure the UI is built first

        connectBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (connectionStatus != ConnectionStatus.Connected)
                    reconnectToServer();
                else {
                    connectionStatus = ConnectionStatus.Disconnected;
                    closeConnection();
                }
            }
        });

        searchBTN.addActionListener(new ActionListener() {//add to other class
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanCurrentTable();
                sendToServer(Command.search, Objects.requireNonNull(searchForBOX.getSelectedItem()).toString(),
                        searchField.getText());
            }
        });

        clearTableBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanCurrentTable();
            }
        });

        getAllBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanCurrentTable();
                sendToServer(Command.getALL, "", "");
            }
        });

        deleteBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (deleteLocallyRadioButton.isSelected()) {

                    switch (currentTableBOX.getSelectedIndex()) {
                        case 0 -> airlinesTable.deleteItem(dataTable.getSelectedRow());
                        case 1 -> airportsTable.deleteItem(dataTable.getSelectedRow());
                        case 2 -> routesTable.deleteItem(dataTable.getSelectedRow());
                    }
                } else {
                    try {
                        if (dataTable.getValueAt(dataTable.getSelectedRow(), 0).toString().matches("N/A"))
                            warningMessage("Selected item is not present at the server", "Element missing");
                        else {
                            sendToServer(Command.delete, "ID", dataTable.getValueAt(dataTable.getSelectedRow(),
                                    0).toString());
                            cleanCurrentTable();
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        Logger.getLogger(ClientConnection.class.getName()).log(SEVERE, null, ex);
                    }
                }
                refreshTables();
            }
        });

        saveBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                sendToServer(Command.add, "", "");
                sendToServer(Command.update, "", "");
                cleanCurrentTable();
            }
        });


        // Window listener to detect when user closes GUI window and closes the connection once it happens
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeConnection();
                System.exit(0); // Exit the program
            }
        });

    }

    /**
     * Displays a popup error message with the message "message" and with the title "title"
     * Also,a message is shown on the terminal.
     */
    public static void warningMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, "InfoBox: " + title, JOptionPane.ERROR_MESSAGE);
        System.out.println(message);
    }

    public static void main(String[] args) {
        ClientConnection clientConnection = new ClientConnection();
        clientConnection.reconnectToServer();
        clientConnection.keepReadingFromServer();
    }

    /**
     * Method for closing connection with the server
     */
    private void closeConnection() {
        if (socket != null) { // Socket is open
            System.out.println("Status: closing connection");
            try {
                socket.close(); // Close socket
                connectionStatus = ConnectionStatus.Disconnected;
                super.setConnectionStatus(connectionStatus);
            } catch (IOException ex) {
                Logger.getLogger(ClientConnection.class.getName()).log(SEVERE, null, ex);
                connectionStatus = ConnectionStatus.Connected;
                super.setConnectionStatus(connectionStatus);
            } finally {
                socket = null;
                try {
                    Thread.sleep(1000); // Pause current thread
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientConnection.class.getName()).log(SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Establish connection with the server
     */
    public void reconnectToServer() {
        closeConnection();
        try {
            System.out.println("Status: Connecting to Server");
            socket = new Socket("127.0.0.1", 2000);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            connectionStatus = ConnectionStatus.Connected;
            super.setConnectionStatus(connectionStatus);

        } catch (IOException ex) {

            Logger.getLogger(CWGUI.class.getName()).log(Level.SEVERE, null, ex);
            connectionStatus = ConnectionStatus.Disconnected;
            super.setConnectionStatus(connectionStatus);

        }
        // notify that the connection is back
        synchronized (waitObject) {
            waitObject.notify();
        }
    }

    /**
     * Method used for sending commands to the server
     * command -> operation to be performed
     * searchTopic -> what the user is looking for
     * searchRequest -> what has been searched by the user/ what item of the database is the target
     */
    protected void sendToServer(Command command, String searchTopic, String searchRequest) {
        if (objectOutputStream != null && objectInputStream != null) {
            String tableSearch = Objects.requireNonNull(currentTableBOX.getSelectedItem()).toString();
            // 2. send data to server
            try {
                objectOutputStream.reset();
                if (command == Command.search) {
                    Parcel sendParcel = new Parcel(searchRequest, searchTopic, tableSearch, command, null, null, null);
                    objectOutputStream.writeObject(sendParcel);
                } else if (command == Command.getALL) {
                    Parcel sendParcel = new Parcel(searchRequest, "", tableSearch, command, null, null, null);
                    objectOutputStream.writeObject(sendParcel);
                } else if (command == Command.delete) {
                    Parcel sendParcel = new Parcel(searchRequest, searchTopic, tableSearch, command, null, null, null);
                    objectOutputStream.writeObject(sendParcel);
                } else if (command == Command.add) {
                    Parcel sendParcel;
                    switch (tableSearch) {
                        case "Airlines":
                            if (airlinesTable.getCreatedElements(true).isEmpty()) {
                                break;
                            } else
                                sendParcel = new Parcel(searchRequest, "", tableSearch, command, airlinesTable.getCreatedElements(true), null, null);
                            objectOutputStream.writeObject(sendParcel);
                            break;
                        case "Airports":
                            if (airportsTable.getCreatedElements(true).isEmpty()) {
                                break;
                            } else {
                                sendParcel = new Parcel(searchRequest, "", tableSearch, command, null, airportsTable.getCreatedElements(true), null);
                            }
                            objectOutputStream.writeObject(sendParcel);
                            break;
                        case "Routes":
                            if (airlinesTable.getCreatedElements(true).isEmpty()) {
                                break;
                            } else
                                sendParcel = new Parcel(searchRequest, "", tableSearch, command, null, null, routesTable.getCreatedElements(true));
                            objectOutputStream.writeObject(sendParcel);
                            break;
                        default:
                            break;
                    }


                } else if (command == Command.update) {

                    Parcel sendParcel;
                    switch (tableSearch) {
                        case "Airlines":
                            if (airlinesTable.getCreatedElements(false).isEmpty()) {
                                break;
                            } else
                                sendParcel = new Parcel(searchRequest, "", tableSearch, command, airlinesTable.getUpdatedAirlinesList(), null, null);
                            objectOutputStream.writeObject(sendParcel);
                            break;
                        case "Airports":
                            if (airportsTable.getCreatedElements(false).isEmpty()) {
                                break;
                            } else {
                                sendParcel = new Parcel(searchRequest, "", tableSearch, command, null, airportsTable.getUpdatedAirportsList(), null);
                            }
                            objectOutputStream.writeObject(sendParcel);
                            break;
                        case "Routes":
                            if (airlinesTable.getCreatedElements(false).isEmpty()) {
                                break;
                            } else
                                sendParcel = new Parcel(searchRequest, "", tableSearch, command, null, null, routesTable.getUpdatedRoutesList());
                            objectOutputStream.writeObject(sendParcel);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CWGUI.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else warningMessage("You must be connected first", "Connection failed");
    }

    /**
     * receives data/commands from server and performs operation on the Client accordingly
     */
    private void keepReadingFromServer() {
        while (true) {

            if (socket == null) {
                System.out.println("Waiting for connection to be reset...");
                synchronized (waitObject) {
                    try {
                        waitObject.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CWGUI.class.getName()).log(Level.SEVERE, null, ex);
                        super.setConnectionStatus(ConnectionStatus.Fail);
                    }
                }
            }

            // 3. receive reply from server
            Parcel reply = null;
            //if (socket != null)serverStatus.setText("Status: Connected and idle");
            try {
                reply = (Parcel) objectInputStream.readObject();
                System.out.println("Status: received reply from server");
                System.out.println("parcel: " + reply.toString());
                isFirst = true;

            } catch (IOException | ClassNotFoundException | ClassCastException | NullPointerException ex) {
                if (isFirst) {
                    Logger.getLogger(CWGUI.class.getName()).log(Level.SEVERE, null, ex);
                    isFirst = false;
                }
                super.setConnectionStatus(ConnectionStatus.Fail);

            }


            if (reply != null) {
                if (reply.getCommand() == Command.searchSuccess) {
                    switch (reply.getTableSearch()) {
                        case "Airlines" -> airlinesTable.addAll(reply.getAirlinesList());
                        case "Airports" -> airportsTable.addAll(reply.getAirportsList());
                        case "Routes" -> routesTable.addAll(reply.getRoutesList());
                    }

                    super.refreshTables();

                } else if (reply.getCommand() == Command.fail) {
                    warningMessage("Operation Failed!", "Fail!");

                } else if (reply.getCommand() == Command.searchFailed) {
                    warningMessage("No results!", "Fail!");

                } else if (reply.getCommand() == Command.time) {
                    serverTimeLBL.setText(timeFormat.format(reply.getTimeOnServer()));
                    serverTimeLBL.revalidate();
                    serverTimeLBL.repaint();
                }
            }
        }
    }


}