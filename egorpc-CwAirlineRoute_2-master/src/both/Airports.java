package both;

//Libraries Used

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Airports implements Serializable {
    //Parameters representing Airport objects
    private final Integer id;
    private String name;
    private String city;
    private String country;
    private String code;
    private String icao;
    private String latitude;
    private String longitude;
    private String altitude;
    private String offset;
    private String dst;
    private String timezone;

    public Airports() {
        this.id = null;
        this.name = "name";
        this.city = "city";
        this.country = "country";
        this.code = "code";
        this.icao = "icao";
        this.latitude = "latitude";
        this.longitude = "longitude";
        this.altitude = "altitude";
        this.offset = "offset";
        this.dst = "dst";
        this.timezone = "timezone";
    }

    public Airports(int id, String name, String city, String country, String code, String icao, String latitude,
                    String longitude, String altitude, String offset, String dst, String timezone) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.code = code;
        this.icao = icao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.offset = offset;
        this.dst = dst;
        this.timezone = timezone;
    }

    /**
     * Method returns an Airline object based on the database parameters
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static Airports newAirportsFromResultSet(ResultSet resultSet) throws SQLException {
        return new both.Airports(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getString(7),
                resultSet.getString(8),
                resultSet.getString(9),
                resultSet.getString(10),
                resultSet.getString(11),
                resultSet.getString(12));
    }

    /**
     * Airport ID Number getter
     * @return Airline ID
     */
    public Integer getId() { return id; }

    /**
     * Airport name getter
     * @return Airline name String
     */
    public String getName() { return name; }

    /**
     * Airport name setter
     * @param name String
     */
    public void setName(String name) { this.name = name;}

    /**
     * Airport City getter
     * @return City String
     */
    public String getCity() {return city; }

    /**
     * Airport City setter
     * @param city String
     */
    public void setCity(String city) { this.city = city;}

    /**
     * Airport Country getter
     * @return Country String
     */
    public String getCountry() { return country;}

    /**
     * Airport Country setter
     * @param country String
     */
    public void setCountry(String country) { this.country = country; }

    /**
     *Airport iata Code getter
     * @return iata Code
     */
    public String getCode() { return code; }

    /**
     * Airport iata Code setter
     * @param code String
     */
    public void setCode(String code) { this.code = code;}

    /**
     * Airport icao Code getter
     * @return icao code String
     */
    public String getIcao() { return icao; }

    /**
     * Airport icao Code setter
     * @param icao String
     */
    public void setIcao(String icao) { this.icao = icao; }

    /**
     * Airport Latitude Coordinates getter
     * @return latitude String
     */
    public String getLatitude() { return latitude;}

    /**
     * Airport Latitude Coordinates setter
     * @param latitude String
     */
    public void setLatitude(String latitude) { this.latitude = latitude; }

    /**
     * Airport longitude Coordinates getter
     * @return longitude String
     */
    public String getLongitude() { return longitude;}

    /**
     * Airport longitude Coordinates setter
     * @param longitude String
     */
    public void setLongitude(String longitude) { this.longitude = longitude; }

    /**
     * Airport altitude Coordinates getter
     * @return altitude String
     */
    public String getAltitude() { return altitude; }

    /**
     *Airport altitude Coordinates setter
     * @param altitude String
     */
    public void setAltitude(String altitude) { this.altitude = altitude; }

    /**
     * Airport Timezone Offset from UTC getter
     * @return  Hours String
     */
    public String getOffset() { return offset; }

    /**
     * Airport Timezone Offset from UTC setter
     * @param offset Airport Timezone Offset String
     */
    public void setOffset(String offset) { this.offset = offset; }

    /**
     * Airport Daylights Saving Time getter
     * @return Airport Daylights Saving Time String
     */
    public String getDst() { return dst;}

    /**
     *  Airport Daylights Saving Time setter
     * @param dst Airport Daylights Saving Time String
     */
    public void setDst(String dst) { this.dst = dst; }

    /**
     * Airport Timezone Region getter
     * @return Timezone string
     */
    public String getTimezone() { return timezone; }

    /**
     * Airport Timezone Region setter
     * @param timezone String
     */
    public void setTimezone(String timezone) { this.timezone = timezone; }

}
