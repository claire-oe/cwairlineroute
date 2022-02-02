package both;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AirlinesTable extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Name", "Alias", "IATA", "ICAO", "Call sign", "Country", "Active"};

    private final List<Airlines> airlinesList = new ArrayList<>();

    private final List<Airlines> updatedAirlinesList = new ArrayList<>();

    Airlines airlineTarget;

    /**
     * getCreatedElements true -> returns a list of all the items that have been added locally
     * getCreatedElements false -> returns a list of all the items that have have no ID(NOT present at the database yet)
     * */
    public List<Airlines> getCreatedElements(boolean getCreatedElements) {
        List<Airlines> parseElements = new ArrayList<>(airlinesList);
        if (getCreatedElements) {
            parseElements.removeIf(airlines -> airlines.getId() != null);
        } else {
            parseElements.removeIf(airlines -> airlines.getId() == null);
        }

        return parseElements;
    }
    /**
     * Adds row to the table based on the default constructor
     * */
    public void addRow() {
        airlinesList.add(new Airlines());
    }

    /**
     * Adds all the items present on the "getAirlinesList" List
     * */
    public void addAll(List<Airlines> getAirlinesList) {
        airlinesList.addAll(getAirlinesList);
    }

    /**
     * Remove a row from the table located at the line "pos"
     * */
    public void deleteItem(int pos) {
        try {
            airlinesList.remove(pos);
            fireTableStructureChanged();
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(AirlinesTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearTable() {
        airlinesList.clear();
        fireTableStructureChanged();
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
        return airlinesList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        airlineTarget = airlinesList.get(rowIndex);

        if (columnIndex == 0) {
            if (airlineTarget.getId() == null) return "N/A";
            else return airlineTarget.getId();
        } else if (columnIndex == 1) {
            return airlineTarget.getName();
        } else if (columnIndex == 2) {
            return airlineTarget.getAlias();
        } else if (columnIndex == 3) {
            return airlineTarget.getIata();
        } else if (columnIndex == 4) {
            return airlineTarget.getIcao();
        } else if (columnIndex == 5) {
            return airlineTarget.getCallsign();
        } else if (columnIndex == 6) {
            return airlineTarget.getCountry();
        } else if (columnIndex == 7) {
            return airlineTarget.getActive();
        } else return null;
    }

    /**
     * Returns List<Airlines> with the updated rows
     * */
    public List<Airlines> getUpdatedAirlinesList() {
        return updatedAirlinesList;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            airlineTarget = airlinesList.get(rowIndex);
            if (airlineTarget.getId() != null) updatedAirlinesList.add(airlineTarget);
            if (airlineTarget != null) {
                switch (columnIndex) {
                    case 1:
                        airlineTarget.setName(aValue.toString());
                        break;
                    case 2:
                        airlineTarget.setAlias(aValue.toString());
                        break;
                    case 3:
                        airlineTarget.setIata(aValue.toString());
                        break;
                    case 4:
                        airlineTarget.setIcao(aValue.toString());
                        break;
                    case 5:
                        airlineTarget.setCallsign(aValue.toString());
                        break;
                    case 6:
                        airlineTarget.setCountry(aValue.toString());
                        break;
                    case 7:
                        airlineTarget.setActive(aValue.toString());
                        break;
                    default:
                        break;

                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(AirlinesTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("user inputted invalid value");
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(AirlinesTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Invalid Selection");
        }


    }
}
