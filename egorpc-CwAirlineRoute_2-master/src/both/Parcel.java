package both;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

    //The parcel class in responsible for the communication between the server and the Client
public class Parcel implements Serializable {
        //Parameters representing Parcel objects
        private final Command command;
        private Date timeOnServer;
        private String searchTopic;
        private String searchFor;
        private String tableSearch;
        private List<Airlines> airlinesList;
        private List<Airports> airportsList;
        private List<Routes> routesList;

        public Parcel(Command command) {
            this.command = command;
        }

        public Parcel(String search, String searchTopic, String tableSearch, Command command, List<Airlines> airlinesList, List<Airports> airportsList, List<Routes> routesList) {
            this.command = command;
            this.searchTopic = searchTopic;
            this.tableSearch = tableSearch;
            this.searchFor = search;
            this.airlinesList = airlinesList;
            this.airportsList = airportsList;
            this.routesList = routesList;
        }

        public Parcel(Command command, Date time) {
            this.command = command;
            this.timeOnServer = time;

        }

        //Parameter Getters and Setters
        public Command getCommand() {
            return command;
        }

        public String getSearchFor() {
            return searchFor;
        }

        public List<Airlines> getAirlinesList() {
            return airlinesList;
        }

        public List<Airports> getAirportsList() {
            return airportsList;
        }

        public List<Routes> getRoutesList() {
            return routesList;
        }

        public String getSearchTopic() {
            return searchTopic;
        }

        public String getTableSearch() {
            return tableSearch;
        }

        public void String(String tableSearch) {
            this.tableSearch = tableSearch;
        }

        public Date getTimeOnServer() {
            return timeOnServer;
        }

        @Override
        public String toString() {

            String commandString = command != null ? command.toString() : "null";
            String searchTopicString = searchTopic != null ? searchTopic : "null";
            String searchForString = searchFor != null ? searchFor : "null";
            String tableSearchString = tableSearch != null ? tableSearch : "null";
            String airlineString = airlinesList != null ? airlinesList.toString() : "null";
            String airportsString = airportsList != null ? airportsList.toString() : "null";
            String routesString = routesList != null ? routesList.toString() : "null";


            return "Parcel{" +
                    "command=" + commandString +
                    ", searchTopic='" + searchTopicString + '\'' +
                    ", searchFor='" + searchForString + '\'' +
                    ", tableSearch=" + tableSearchString +
                    ", airlinesList=" + airlineString +
                    ", airportsList=" + airportsString +
                    ", routesList=" + routesString +
                    '}';
        }
    }





