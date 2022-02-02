package both;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoutesTable extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Airline ID", "source_id", "dest_id", "codeshare", "stops",
            "equipment"};

    private final List<Routes> routesList = new ArrayList<>();

    /**
     * Returns List<Routes> with the updated rows
     * */
    private final List<Routes> updatedRoutesList = new ArrayList<>();
    Routes routesTarget;

    /**
     * Adds row to the table based on the default constructor
     * */
    public void addRow() {
        routesList.add(new Routes());
    }

    /**
     * Adds all the items present on the "getRoutesList" List
     * */
    public void addAll(List<Routes> getRoutesList) {
        routesList.addAll(getRoutesList);
    }

    /**
     * getCreatedElements true -> returns a list of all the items that have been added locally
     * getCreatedElements false -> returns a list of all the items that have have no ID(NOT present at the database yet)
     * */
    public List<Routes> getCreatedElements(boolean getCreatedElements) {
        List<Routes> parseElements = new ArrayList<>(routesList);
        if (getCreatedElements) {
            parseElements.removeIf(routes -> routes.getId() != null);
        } else {
            parseElements.removeIf(routes -> routes.getId() == null);
        }

        return parseElements;
    }

    /**
     * Remove a row from the table located at the line "pos"
     * */
    public void deleteItem(int pos) {
        try {
            routesList.remove(pos);
            fireTableStructureChanged();
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(AirlinesTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearTable() {
        routesList.clear();
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
        return routesList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        routesTarget = routesList.get(rowIndex);

        if (columnIndex == 0) {
            if (routesTarget.getId() == null) return "N/A";
            else return routesTarget.getId();
        } else if (columnIndex == 1) {
            return routesTarget.getAirline_id();
        } else if (columnIndex == 2) {
            return routesTarget.getSource_id();
        } else if (columnIndex == 3) {
            return routesTarget.getDest_id();
        } else if (columnIndex == 4) {
            return routesTarget.getCodeshare();
        } else if (columnIndex == 5) {
            return routesTarget.getStops();
        } else if (columnIndex == 6) {
            return routesTarget.getEquipment();
        } else return null;
    }

    public List<Routes> getUpdatedRoutesList() {
        return updatedRoutesList;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            routesTarget = routesList.get(rowIndex);
            if (routesTarget.getId() != null) updatedRoutesList.add(routesTarget);

            if (routesTarget != null) {
                switch (columnIndex) {
                    case 1:
                        routesTarget.setAirline_id(Integer.parseInt(aValue.toString()));
                        break;
                    case 2:
                        routesTarget.setSource_id(Integer.parseInt(aValue.toString()));
                        break;
                    case 3:
                        routesTarget.setDest_id(Integer.parseInt(aValue.toString()));
                        break;
                    case 4:
                        routesTarget.setCodeshare(aValue.toString());
                        break;
                    case 5:
                        routesTarget.setStops(Integer.parseInt(aValue.toString()));
                        break;
                    case 6:
                        routesTarget.setEquipment(aValue.toString());
                        break;
                    default:
                        break;

                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(RoutesTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("user inputted invalid value");
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(RoutesTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Invalid Selection");
        }
    }
}
