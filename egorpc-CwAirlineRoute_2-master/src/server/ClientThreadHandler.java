package server;

import both.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientThreadHandler implements Runnable {

    private static int numConnections = 0;
    private final Socket socket;
    private final int connectionNum;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    DatabaseManagement databaseManagement = new DatabaseManagement();


    public ClientThreadHandler(Socket socket) throws IOException {
        this.socket = socket;

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        numConnections++;
        connectionNum = numConnections;
        threadSays("Connection " + connectionNum + " established");
    }

    @Override
    public void run() {
        try {
            //method is responsible for managing the reception and the sending of parcels/data to each thread/client
            threadSays("Server: Waiting for Client's Parcel...");
            //parcel for communication
            Parcel parcel;

            while ((parcel = (Parcel) objectInputStream.readObject()) != null) {
                threadSays("Server: Read data from client.");
                threadSays("Parcel:" + parcel.toString());

                Parcel reply = null;

                if (parcel.getCommand() == Command.search || parcel.getCommand() == Command.getALL) {
                    threadSays("search command received");

                    if (parcel.getTableSearch().matches("Airlines")) {
                        List<Airlines> airlinesList = databaseManagement.readAirlineTable(parcel.getSearchFor(),
                                parcel.getSearchTopic(), parcel.getCommand() == Command.getALL);

                        if (airlinesList.size() == 0) {
                            reply = new Parcel(Command.searchFailed);
                        } else {
                            reply = new Parcel("", parcel.getSearchTopic(), parcel.getTableSearch(),
                                    Command.searchSuccess,
                                    airlinesList, null, null);
                        }
                    } else if (parcel.getTableSearch().matches("Airports")) {

                        List<Airports> airportsList = databaseManagement.readAirportsTable(parcel.getSearchFor(),
                                parcel.getSearchTopic(), parcel.getCommand() == Command.getALL);

                        if (airportsList.size() == 0) {
                            reply = new Parcel(Command.searchFailed);
                        } else {
                            reply = new Parcel("", parcel.getSearchTopic(), parcel.getTableSearch(),
                                    Command.searchSuccess,
                                    null, airportsList, null);
                        }

                    } else if (parcel.getTableSearch().matches("Routes")) {
                        List<Routes> routesList = databaseManagement.readRoutesTable(parcel.getSearchFor(),
                                parcel.getSearchTopic(), parcel.getCommand() == Command.getALL);

                        if (routesList.size() == 0) {
                            reply = new Parcel(Command.searchFailed);
                        } else {
                            reply = new Parcel("", parcel.getSearchTopic(), parcel.getTableSearch(),
                                    Command.searchSuccess,
                                    null, null, routesList);
                        }
                    }
                } else if (parcel.getCommand() == Command.delete) {
                    threadSays("search command received");
                    if (parcel.getTableSearch().matches("Airlines")) {
                        List<Airlines> airlinesList = databaseManagement.readAirlineTable(parcel.getSearchFor(),
                                parcel.getSearchTopic(), false);

                        if (airlinesList.size() == 0) {
                            reply = new Parcel(Command.fail);
                        } else {
                            reply = new Parcel("", parcel.getSearchTopic(), parcel.getTableSearch(),
                                    databaseManagement.deleteElement(parcel.getTableSearch(), parcel.getSearchFor()),
                                    airlinesList, null, null);
                        }
                    } else if (parcel.getTableSearch().matches("Airports")) {

                        List<Airports> airportsList = databaseManagement.readAirportsTable(parcel.getSearchFor(),
                                parcel.getSearchTopic(), false);

                        if (airportsList.size() == 0) {
                            reply = new Parcel(Command.fail);

                        } else {
                            reply = new Parcel("", parcel.getSearchTopic(), parcel.getTableSearch(),
                                    databaseManagement.deleteElement(parcel.getTableSearch(), parcel.getSearchFor()),
                                    null, null, null);
                        }
                    } else if (parcel.getTableSearch().matches("Routes")) {
                        List<Routes> routesList = databaseManagement.readRoutesTable(parcel.getSearchFor(),
                                parcel.getSearchTopic(), false);

                        if (routesList.size() == 0) {
                            reply = new Parcel(Command.fail);
                        } else {
                            reply = new Parcel("", parcel.getSearchTopic(), parcel.getTableSearch(),
                                    databaseManagement.deleteElement(parcel.getTableSearch(), parcel.getSearchFor()),
                                    null, null, null);
                        }
                    }

                } else if (parcel.getCommand() == Command.add) {
                    reply = switch (parcel.getTableSearch()) {
                        case "Airlines" -> new Parcel(databaseManagement.addAirline(parcel.getAirlinesList()));
                        case "Airports" -> new Parcel(databaseManagement.addAirports(parcel.getAirportsList()));
                        case "Routes" -> new Parcel(databaseManagement.addRoutes(parcel.getRoutesList()));
                        default -> throw new IllegalStateException("Unexpected value: " + parcel.getTableSearch());
                    };

                } else if (parcel.getCommand() == Command.update) {
                    reply = switch (parcel.getTableSearch()) {
                        case "Airlines" -> new Parcel(databaseManagement.updateAirlines(parcel.getAirlinesList()));
                        case "Airports" -> new Parcel(databaseManagement.updateAirports(parcel.getAirportsList()));
                        case "Routes" -> new Parcel(databaseManagement.updateRoutes(parcel.getRoutesList()));
                        default -> throw new IllegalStateException("Unexpected value: " + parcel.getTableSearch());
                    };
                }
                //else if for add and delete and update
                objectOutputStream.writeObject(reply);

            }

        } catch (SocketException | EOFException | ClassNotFoundException ex) {
            Logger.getLogger(ClientThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                threadSays("Server: We have lost connection to client " + connectionNum + ".");
                Server.removeThread(this);
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //broadcast feature is currently being used for displaying the server time
    public void sendBroadcast() throws IOException {
        threadSays("Broadcasting to client " + connectionNum + ".");
        Parcel reply = new Parcel(Command.time, new Date());
        objectOutputStream.writeObject(reply);
    }

    /**display a message(say) on the terminal and the connection number */
    private void threadSays(String say) {
        System.out.println("ThreadedClientHandler" + connectionNum + ": " + say);
    }
}