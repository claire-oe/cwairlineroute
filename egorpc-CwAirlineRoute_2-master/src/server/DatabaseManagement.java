package server;

import both.Airlines;
import both.Airports;
import both.Command;
import both.Routes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//add here the sqlite commands
public class DatabaseManagement {

    /**
     * Method gets all the elements from the Airlines table if "getAllItems" is set
     * Otherwise, it will return a list of airlines that fulfills the search parameters
     *
     * @return airport ArrayList
     */
    public synchronized List<Airlines> readAirlineTable(String search, String searchTopic, boolean getAllItems) {
        ArrayList<Airlines> airlinesList = new ArrayList<>();
        String selectSQL;
        if (getAllItems) selectSQL = "SELECT * FROM Airlines";
        else selectSQL = "SELECT * FROM Airlines WHERE " + searchTopic + " = \"" + search + "\"";

        try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
             PreparedStatement prep = conn.prepareStatement(selectSQL)) {

            ResultSet resultSet = prep.executeQuery();

            while (resultSet.next()) {
                airlinesList.add(Airlines.newAirlinesFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return airlinesList;
    }
    /**
     * Method gets all the elements from the Airports table if "getAllItems" is set
     * Otherwise, it will return a list of Airports that fulfills the search parameters
     *
     * @return Airports ArrayList
     */
    public synchronized List<Airports> readAirportsTable(String search, String searchTopic, boolean getAllItems) {
        ArrayList<Airports> airportsList = new ArrayList<>();
        String selectSQL;
        if (getAllItems) selectSQL = "SELECT * FROM Airports";
        else selectSQL = "SELECT * FROM Airports WHERE " + searchTopic + " = \"" + search + "\"";


        try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
             PreparedStatement prep = conn.prepareStatement(selectSQL)) {

            ResultSet resultSet = prep.executeQuery();

            while (resultSet.next()) {
                airportsList.add(Airports.newAirportsFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return airportsList;
    }

    /**
     * Method gets all the elements from the Routes table if "getAllItems" is set
     * Otherwise, it will return a list of Routes that fulfills the search parameters
     *
     * @return Routes ArrayList
     */
    public synchronized List<Routes> readRoutesTable(String search, String searchTopic, boolean getAllItems) {
        ArrayList<Routes> routesList = new ArrayList<>();
        String selectSQL;
        if (getAllItems) selectSQL = "SELECT * FROM Routes";
        else selectSQL = "SELECT * FROM Routes WHERE " + searchTopic + " = \"" + search + "\"";

        try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
             PreparedStatement prep = conn.prepareStatement(selectSQL)) {

            ResultSet resultSet = prep.executeQuery();

            while (resultSet.next()) {
                routesList.add(Routes.newRoutesFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return routesList;
    }

    /**
     * Method deletes a element from the database which its table is "tableSearch" nad the ID "search"
     */
    public synchronized Command deleteElement(String tableSearch, String search) {
        Command result = Command.successful;

        String selectSQL = "DELETE FROM " + tableSearch + " WHERE ID = ?";
        System.out.println(selectSQL);

        try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
             PreparedStatement prep = conn.prepareStatement(selectSQL)) {

            prep.setString(1, search);

            // execute the prepared statement
            prep.execute();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
            result = Command.fail;
        }
        return result;
    }
    /**
     * adds all the elements on the "airlinesList" to the airlines table from the database and returns the outcome
     *
     * @return Command
     */
    public synchronized Command addAirline(List<Airlines> airlinesList) {
        Command outcome = Command.successful;
        for (Airlines airlines : airlinesList) {
            String insertSQL = "INSERT INTO airlines (name,alias,iata,icao,callsign,country,active)" +
                    "VALUES (?,?,?,?,?,?,?)";


            System.out.println(insertSQL);
            try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
                 PreparedStatement prep = conn.prepareStatement(insertSQL)) {

                prep.setString(1, airlines.getName());
                prep.setString(2, airlines.getAlias());
                prep.setString(3, airlines.getIata());
                prep.setString(4, airlines.getIcao());
                prep.setString(5, airlines.getCallsign());
                prep.setString(6, airlines.getCountry());
                prep.setString(7, airlines.getActive());

                prep.execute();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
                outcome = Command.fail;
            }
        }
        return outcome;
    }

    /**
     * adds all the elements on the "airportsList" to the airports table from the database and returns the outcome
     *
     * @return Command
     */
    public synchronized Command addAirports(List<Airports> airportsList) {
        Command outcome = Command.successful;
        for (Airports airports : airportsList) {
            String insertSQL = "INSERT INTO airports (name,city,country,code,icao,latitude,longitude,altitude,offset,dst,timezone)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";


            System.out.println(insertSQL);
            try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
                 PreparedStatement prep = conn.prepareStatement(insertSQL)) {

                prep.setString(1, airports.getName());
                prep.setString(2, airports.getCity());
                prep.setString(3, airports.getCountry());
                prep.setString(4, airports.getCode());
                prep.setString(5, airports.getIcao());
                prep.setString(6, airports.getLatitude());
                prep.setString(7, airports.getLongitude());
                prep.setString(8, airports.getAltitude());
                prep.setString(9, airports.getOffset());
                prep.setString(10, airports.getDst());
                prep.setString(11, airports.getTimezone());

                prep.execute();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
                outcome = Command.fail;
            }
        }
        return outcome;
    }


    /**
     * adds all the elements on the "routesList" to the routes table from the database and returns the outcome
     *
     * @return Command
     */
    public synchronized Command addRoutes(List<Routes> routesList) {
        Command outcome = Command.successful;
        for (Routes routes : routesList) {
            String insertSQL = "INSERT INTO routes (airline_id,source_id,dest_id,codeshare,stops,equipment)" +
                    "VALUES (?,?,?,?,?,?)";

            System.out.println(insertSQL);
            try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
                 PreparedStatement prep = conn.prepareStatement(insertSQL)) {

                prep.setInt(1, routes.getAirline_id());
                prep.setInt(2, routes.getSource_id());
                prep.setInt(3, routes.getDest_id());
                prep.setString(4, routes.getCodeshare());
                prep.setInt(5, routes.getStops());
                prep.setString(6, routes.getEquipment());

                prep.execute();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
                outcome = Command.fail;
            }
        }
        return outcome;
    }

    /**
     * updates all the elements on the airlines table from the database according to the airlinesList items
     * and returns the outcome
     *
     * @return Command
     */
    public synchronized Command updateAirlines(List<Airlines> airlinesList) {
        for (Airlines airlines : airlinesList) {
            if (readAirlineTable(String.valueOf(airlines.getId()), "ID", false).size() == 0)
                return Command.fail;
        }
        Command result = Command.successful;
        for (Airlines airlines : airlinesList) {
            String insertSQL = "UPDATE airlines" +
                    " SET name = ? " +
                    ",alias = ? " +
                    ",iata = ? " +
                    ",icao = ? " +
                    ",callsign = ? " +
                    ",country = ? " +
                    ",active = ? " +
                    "WHERE id = ?";

            System.out.println(insertSQL);
            try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
                 PreparedStatement prep = conn.prepareStatement(insertSQL)) {

                prep.setString(1, airlines.getName());
                prep.setString(2, airlines.getAlias());
                prep.setString(3, airlines.getIata());
                prep.setString(4, airlines.getIcao());
                prep.setString(5, airlines.getCallsign());
                prep.setString(6, airlines.getCountry());
                prep.setString(7, airlines.getActive());
                prep.setInt(8, airlines.getId());

                prep.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
                result = Command.fail;
            }
        }
        return result;
    }



    /**
     * updates all the elements on the airports table from the database according to the airportsList items
     * and returns the outcome
     *
     * @return Command
     */
    public synchronized Command updateAirports(List<Airports> airportsList) {
        for (Airports airports : airportsList) {
            if (readAirlineTable(String.valueOf(airports.getId()), "ID", false).size() == 0)
                return Command.fail;
        }
        Command result = Command.successful;
        for (Airports airports : airportsList) {
            String insertSQL = "UPDATE  airports " +
                    "SET name = ? " +
                    ",city = ? " +
                    ",country = ? " +
                    ",code = ? " +
                    ",icao = ? " +
                    ",latitude = ? " +
                    ",longitude = ? " +
                    ",altitude = ? " +
                    ",offset = ? " +
                    ",dst = ? " +
                    ",timezone = ? " +
                    "WHERE id = ?";

            System.out.println(insertSQL);
            try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
                 PreparedStatement prep = conn.prepareStatement(insertSQL)) {

                prep.setString(1, airports.getName());
                prep.setString(2, airports.getCity());
                prep.setString(3, airports.getCountry());
                prep.setString(4, airports.getCode());
                prep.setString(5, airports.getIcao());
                prep.setString(6, airports.getLatitude());
                prep.setString(7, airports.getLongitude());
                prep.setString(8, airports.getAltitude());
                prep.setString(9, airports.getOffset());
                prep.setString(10, airports.getDst());
                prep.setString(11, airports.getTimezone());
                prep.setInt(12, airports.getId());

                prep.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
                result = Command.fail;
            }
        }
        return result;
    }

    /**
     * updates all the elements on the routes table from the database according to the routesList items
     * and returns the outcome
     *
     * @return Command
     */
    public synchronized Command updateRoutes(List<Routes> routesList) {
        for (Routes routes : routesList) {
            if (readAirlineTable(String.valueOf(routes.getId()), "ID", false).size() == 0)
                return Command.fail;
        }
        Command result = Command.successful;
        for (Routes routes : routesList) {
            String insertSQL = "UPDATE routes" +
                    " SET airline_id = ? " +
                    ",source_id = ? " +
                    ",dest_id = ? " +
                    ",codeshare = ? " +
                    ",stops = ? " +
                    ",equipment = ? " +
                    "WHERE id = ?";

            System.out.println(insertSQL);
            try (Connection conn = AccessToDatabase.getConnection(); // auto close the connection object after try
                 PreparedStatement prep = conn.prepareStatement(insertSQL)) {

                prep.setInt(1, routes.getAirline_id());
                prep.setInt(2, routes.getSource_id());
                prep.setInt(3, routes.getDest_id());
                prep.setString(4, routes.getCodeshare());
                prep.setInt(5, routes.getStops());
                prep.setString(6, routes.getEquipment());
                prep.setInt(7, routes.getId());

                prep.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
                result = Command.fail;
            }
        }
        return result;
    }



}
