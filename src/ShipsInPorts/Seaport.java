// File: Seaport.java
// Date: 24 February 2019
// Author: 
// Purpose: The Seaport class contains all the various occupants of the seaport.
// This class holds the dock, ships, personnel, and jobs.  Additionally, the 
// seaport class maintains a que of ships waiting for a dock as wel as the 
// types of ships at each dock.  Extends the Thing class.
package ShipsInPorts;

import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Seaport extends Thing {

    ArrayList<Dock> docks = new ArrayList<>();
    ArrayList<Ship> que = new ArrayList<>();//The list of ships waiting to dock
    ArrayList<Ship> ships = new ArrayList<>();//The list of all the ships at this port
    ArrayList<Person> persons = new ArrayList<>();//People with skills at this port
    ArrayList<Ship> cships = new ArrayList<>();
    ArrayList<Ship> pships = new ArrayList<>();
    JPanel jpResources = new JPanel();
    JLabel lblPortName = new JLabel("", SwingConstants.CENTER);

    public Seaport(String name, int index, int parent, JPanel jpPorts) {
        super(name, index, parent);
        jpResources = jpPorts;
        lblPortName.setText(name);
    }

    public void addDock(Dock dock) {
        docks.add(dock);
    }// end addDock

    public void addQue(Ship ship) {
        que.add(ship);
    }//end addQue

    public void addShip(Ship ship) {
        ships.add(ship);
    }//end addShip

    public void addPerson(Person person) {
        persons.add(person);
    }//end addPerson

    public void addCargoShip(Ship cship) {
        cships.add(cship);
    }//end addPerson

    public void addPassengerShip(Ship pship) {
        pships.add(pship);
    }//end addPerson

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<CargoShip> getCargoShips() {
        return cships.stream()
                .map(ship -> (CargoShip) ship)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<PassengerShip> getPassengerShips() {
        return pships.stream()
                .map(ship -> (PassengerShip) ship)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void cycleShips(Ship ship) {
        ship.isDocked = false;
        if (!que.isEmpty()) {
            Ship newShip = que.get(0);
            for (Dock dock : docks) {
                if (dock.getIndex() == ship.parent) {
                    dock.ship = newShip;
                }
            }
            newShip.isDocked = true;
            que.remove(0);
        }
    }

    private JLabel[] nameLabel;
    private JLabel[] dockNameLabel;
    private JLabel[] shipNameLabel;
    private JLabel[] jobNameLabel;
    private JLabel[] workersLabel;
    private JLabel[] queLabel;

    public void setPanel() {

//        JPanel panel = new JPanel();
        nameLabel = new JLabel[docks.size()];
        dockNameLabel = new JLabel[docks.size()];
        shipNameLabel = new JLabel[docks.size()];
        jobNameLabel = new JLabel[docks.size()];
        workersLabel = new JLabel[docks.size()];
        queLabel = new JLabel[docks.size()];

        int countPersons = 0;
        int shipsInQue = 0;

        int i = 0;

        for (Dock dock : docks) {

            nameLabel[i] = new JLabel(this.name, SwingConstants.CENTER);
            jpResources.add(nameLabel[i]);

            dockNameLabel[i] = dock.lblDockName;
            jpResources.add(dockNameLabel[i]);

            shipNameLabel[i] = dock.ship.lblShipName;
            jpResources.add(shipNameLabel[i]);

            if (dock.ship.jobs.isEmpty()) {
                jobNameLabel[i] = new JLabel("No job.  Undocking Ship.", SwingConstants.CENTER);
                jpResources.add(jobNameLabel[i]);

            } else {
//                    if (resources are unavailable)
//                    jpResources.add(new JLabel("No resources.  Undocking Ship.", SwingConstants.CENTER));
//                } else {
                jobNameLabel[i] = dock.ship.jobs.get(0).lblJobName;
                jpResources.add(dock.ship.jobs.get(0).lblJobName);

            }

            workersLabel[i] = new JLabel(Integer.toString(persons.size()), SwingConstants.CENTER);
            jpResources.add(workersLabel[i]);
            queLabel[i] = new JLabel(Integer.toString(que.size()), SwingConstants.CENTER);
            jpResources.add(queLabel[i]);

            i++;

        }

    }

    public void updatePanel() {
        int i = 0;
        int busyWorkers = 0;
        for (Person busy : persons){
                if (busy.busyFlag){
                    busyWorkers++;
                }
            }
        for (Dock dock : docks) {
            shipNameLabel[i].setText(dock.getShip() == null ? " No Ship" : dock.getShip().name);
            jobNameLabel[i].setText(dock.ship.jobs.size() > 0 ? dock.ship.jobs.get(0).lblJobName.getText() : "No job.  Undocking Ship.");
            workersLabel[i].setText(Integer.toString(persons.size()-busyWorkers));
            queLabel[i].setText(Integer.toString(que.size()));
            i++;
        }

    }

    void assignShip(Ship ms, String type) {
        int parent = ms.getParent();
        if (!ms.checkDocked()) {
            ms.setParentPort(this);
            ships.add(ms);
            que.add(ms);
            if (type.equals("cargo")) {
                addCargoShip(ms);
            } else {
                addPassengerShip(ms);
            }

            return;

        } else {
            ms.setParentPort(this);
            getDockByIndex(ms.getParent()).setShip(ms);
            ships.add(ms);
            ms.dockShip();
            if (type.equals("cargo")) {
                addCargoShip(ms);
            } else {
                addPassengerShip(ms);
            }

        }

    } // end method assignShip

    public Dock getDockByIndex(int i) {
        for (Dock dock : docks) {
            if (dock.getIndex() == i) {
                return dock;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String st = "\n\nSeaPort: " + super.toString();
        st = docks.stream().map((md) -> "\n" + md).reduce(st, String::concat);
        st += "\n\n --- List of all ships in que:";
        st = que.stream().map((ms) -> "\n > " + ms).reduce(st, String::concat);
        st += "\n\n --- List of all ships:";
        st = ships.stream().map((ms) -> "\n > " + ms).reduce(st, String::concat);
        st += "\n\n --- List of all persons:";
        st = persons.stream().map((mp) -> "\n > " + mp).reduce(st, String::concat);
        st += "\n\n --- List of all jobs:";
        for (Ship ship : ships) {
            st = ship.jobs.stream().map((job) -> "\n > " + job).reduce(st, String::concat);
        }
        return st;
    } // end method toString

}
