package both;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AirportsTable extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Name", "City", "Country", "Code", "ICAO", "Latitude", "Longitude"
            , "Altitude", "Offset", "Dst", "Timezone"};

    private final List<Airports> airportsList = new ArrayList<>();

    private final List<Airports> updatedAirportsList = new ArrayList<>();
    Airports airportsTarget;

    /**
     * Adds row to the table based on the default constructor
     */
    public void addRow() {
        airportsList.add(new Airports());
    }

    /**
     * Adds all the items present on the "getAirportsList" List
     */
    public void addAll(List<Airports> getAirportsList) {
        airportsList.addAll(getAirportsList);
    }

    /**
     * Remove a row from the table located at the line "pos"
     */
    public void deleteItem(int pos) {
        try {
            airportsList.remove(pos);
            fireTableStructureChanged();
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(AirlinesTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * getCreatedElements true -> returns a list of all the items that have been added locally
     * getCreatedElements false -> returns a list of all the items that have have no ID(NOT present at the database yet)
     */
    public List<Airports> getCreatedElements(boolean getCreatedElements) {
        List<Airports> parseElements = new ArrayList<>(airportsList);
        if (getCreatedElements) {
            parseElements.removeIf(airports -> airports.getId() != null);
        } else {
            parseElements.removeIf(airports -> airports.getId() == null);
        }

        return parseElements;
    }

    public void clearTable() {
        airportsList.clear();
        fireTableStructureChanged();
    }

    /**
     * Returns List<Airports> with the updated rows
     */
    public List<Airports> getUpdatedAirportsList() {
        return updatedAirportsList;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return airportsList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        airportsTarget = airportsList.get(rowIndex);

        if (columnIndex == 0) {
            if (airportsTarget.getId() == null) return "N/A";

            else return airportsTarget.getId();
        } else if (columnIndex == 1) {
            return airportsTarget.getName();
        } else if (columnIndex == 2) {
            return airportsTarget.getCity();
        } else if (columnIndex == 3) {
            return airportsTarget.getCountry();
        } else if (columnIndex == 4) {
            return airportsTarget.getCode();
        } else if (columnIndex == 5) {
            return airportsTarget.getIcao();
        } else if (columnIndex == 6) {
            return airportsTarget.getLatitude();
        } else if (columnIndex == 7) {
            return airportsTarget.getLongitude();
        } else if (columnIndex == 8) {
            return airportsTarget.getAltitude();
        } else if (columnIndex == 9) {
            return airportsTarget.getOffset();
        } else if (columnIndex == 10) {
            return airportsTarget.getDst();
        } else if (columnIndex == 11) {
            return airportsTarget.getTimezone();
        } else return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            airportsTarget = airportsList.get(rowIndex);

            if (airportsTarget.getId() != null) updatedAirportsList.add(airportsTarget);

            if (airportsTarget != null) {
                switch (columnIndex) {
                    case 1:
                        airportsTarget.setName(aValue.toString());
                        break;
                    case 2:
                        airportsTarget.setCity(aValue.toString());
                        break;
                    case 3:
                        airportsTarget.setCountry(aValue.toString());
                        break;
                    case 4:
                        airportsTarget.setCode(aValue.toString());
                        break;
                    case 5:
                        airportsTarget.setIcao(aValue.toString());
                        break;
                    case 6:
                        airportsTarget.setLatitude(aValue.toString());
                        break;
                    case 7:
                        airportsTarget.setLongitude(aValue.toString());
                        break;
                    case 8:
                        airportsTarget.setAltitude(aValue.toString());
                        break;
                    case 9:
                        airportsTarget.setOffset(aValue.toString());
                        break;
                    case 10:
                        airportsTarget.setDst(aValue.toString());
                        break;
                    case 11:
                        airportsTarget.setTimezone(aValue.toString());
                        break;
                    default:
                        break;

                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(AirportsTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("user inputted invalid value");
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(AirportsTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Invalid Selection");
        }
    }

}
