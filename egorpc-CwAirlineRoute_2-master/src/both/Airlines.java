package both;

//Libraries Used
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Airlines implements Serializable{
//Parameters representing Airline objects
    public Integer id;
    public String name;
    public String alias;
    public String iata;
    public String icao;
    public String callSign;
    public String country;
    public String active;

    public Airlines() {
        this.id = null;
        this.name = "name";
        this.alias = "alias";
        this.iata = "iata";
        this.icao = "icao";
        this.callSign = "callSign";
        this.country = "country";
        this.active = "N";
    }

    public  Airlines(int id, String name, String alias, String iata, String icao, String callSign, String country, String active)
    {
        this.id = id;
        this.name = name;
        this.alias= alias;
        this.iata = iata;
        this.icao = icao;
        this.callSign = callSign;
        this.country = country;
        this.active= active;
    }
    /**
     * Method returns an Airline object based on the database parameters
     * **/
    public static Airlines newAirlinesFromResultSet(ResultSet resultSet) throws SQLException {
        return new both.Airlines(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getString(7),
                resultSet.getString(8));
    }

    /**
     * Airline ID Number getter
     * @return Airline ID
     */
    public int getId() {
        return id;
    }
    /**
     * Airline ID setter
     * @param id Airline ID
     **/
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Airline name getter
     * @return Airline name String
     */
    public String getName() {
        return name;
    }

    /**
     * Airline ID setter
     * @param name Airline ID
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Airline Alias getter
     * @return Airline Alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Airline alias setter
     * @param alias string
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Airline IATA code getter
     * @return iata code string
     */
    public String getIata() {
        return iata;
    }

    /**
     * Airline IATA code setter
     * @param iata code string
     */
    public void setIata(String iata) {
        this.iata = iata;
    }

    /**
     * Airline ICAO  code getter
     * @return icao code string
     */
    public String getIcao() {
        return icao;
    }

    /**
     * Airline ICAO code setter
     * @param icao code string
     */
    public void setIcao(String icao) {
        this.icao = icao;
    }

    /**
     * Airline Callsign getter
     * @return Callsign string
     */
    public String getCallsign() {
        return callSign;
    }

    /**
     * Airline Callsign setter
     * @param callSign String
     */
    public void setCallsign(String callSign) {
        this.callSign = callSign;
    }

    /**
     * Airline Home Country getter
     * @return country String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Airline Home Country setter
     * @param country String
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Airline Active State getter
     * @return Active String
     */
    public String getActive() {
        return active;
    }

    /**
     * Airline Active State setter
     * @param active String
     */
    public void setActive(String active) {
        this.active = active;
    }
}

