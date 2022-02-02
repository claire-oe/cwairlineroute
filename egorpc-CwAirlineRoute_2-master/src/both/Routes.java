package both;

//Libraries Used
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Routes implements Serializable {
    //Parameters representing Route objects
    private int id;
    private int airline_id;
    private int source_id;
    private int dest_id;
    private String codeshare;
    private int stops;
    private String equipment;

    public Routes(int id, int airline_id, int source_id, int dest_id, String codeshare, int stops, String equipment) {
        this.id = id;
        this.airline_id = airline_id;
        this.source_id = source_id;
        this.dest_id = dest_id;
        this.codeshare = codeshare;
        this.stops = stops;
        this.equipment = equipment;
    }

    public Routes() {
        this.id = Integer.parseInt(null);
        this.airline_id = 0;
        this.source_id = 0;
        this.dest_id = 0;
        this.codeshare = "codeshare";
        this.stops = 0;
        this.equipment = "equipment";
    }

    /**
     * Method returns an Route object based on the database parameters
     */
    public static Routes newRoutesFromResultSet(ResultSet resultSet) throws SQLException {
        return new Routes(
                resultSet.getInt(1),
                resultSet.getInt(2),
                resultSet.getInt(3),
                resultSet.getInt(4),
                resultSet.getString(5),
                resultSet.getInt(6),
                resultSet.getString(7));
    }

    /**
     * Route ID Number getter
     * @return Route ID
     */
    public int getId() {
        return id;
    }

    /**
     * Route ID setter
     * @param id Route ID
     **/
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Airline ID getter
     * @return airline_id
     */
    public int getAirline_id() {
        return airline_id;
    }

    /**
     * Airline ID setter
     * @param airline_id integer
     */
    public void setAirline_id(int airline_id) { this.airline_id = airline_id; }

    /**
     * Source airport ID getter
     * @return source_id integer
     */
    public int getSource_id() { return source_id;}

    /**
     * Source airport ID setter
     * @param source_id integer
     */
    public void setSource_id(int source_id) { this.source_id = source_id; }

    /**
     * Destination ID getter
     * @return dest_id integer
     */
    public int getDest_id() { return dest_id; }

    /**
     *Destination ID setter
     * @param dest_id integer
     */
    public void setDest_id(int dest_id) { this.dest_id = dest_id; }

    /**
     * Codeshare Agreement setter
     * @return codeshare String
     */
    public String getCodeshare() { return codeshare; }

    /**
     * Codeshare Agreement setter
     * @param codeshare String
     */
    public void setCodeshare(String codeshare) { this.codeshare = codeshare; }

    /**
     * Route Stops Number getter
     * (Where 0 Indicates a Direct Flight)
     * @return stops Integer
     */
    public int getStops() { return stops; }

    /**
     * Route Stops Number setter
     * (Where 0 Indicates a Direct Flight)
     * @param stops Integer
     */
    public void setStops(int stops) { this.stops = stops; }

    /**
     *Aircraft Equipment getter
     * @return aircraft equipment string
     */
    public String getEquipment() { return equipment; }

    /**
     * Aircraft Equipment setter
     * @param equipment string
     */
    public void setEquipment(String equipment) { this.equipment = equipment; }
}
