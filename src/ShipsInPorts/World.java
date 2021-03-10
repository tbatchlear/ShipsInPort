// File: World.java
// Date: 24 February 2019
// Author: Tom Batchlear
// Purpose: This is where the magic happens.  The World class is called by the 
// GUI in the SeaPortProgram to open a selected file, scan the file, and 
// distribute elements of the file to the various child classes in order to 
// create the appropraite structure.  Additionally, the World class contains all
// the search functions.  Extends the Thing class.
package ShipsInPorts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class World extends Thing {

    ConcurrentHashMap<Integer, Seaport> hmPorts = new ConcurrentHashMap<>();
    PortTime time = new PortTime();
    JPanel jpResources;

    // default constructor
    public World() {
    }// end default constructor

    // method to process selected file
    void process(File selectedFile, JPanel jpJobs, JPanel jpPorts) {
        HashMap<Integer, Ship> hmShip = new HashMap<>();
        HashMap<Integer, Dock> hmDock = new HashMap<>();
        jpResources = jpPorts;
        try {
            Scanner sc1 = new Scanner(selectedFile);
            while (sc1.hasNextLine()) {
                String line = sc1.nextLine();
                line = line.replaceAll("^\\s+", "");
                if (line.length() > 0 && line.charAt(0) != '/') {
                    Scanner sc = new Scanner(line);
                    if (!sc.hasNext()) {
                        sc.close();
                        return;
                    }
                    switch (sc.next()) {
                        case "port":
                            addPort(sc, jpPorts);
                            break;
                        case "dock":
                            addDock(sc, hmDock);
                            break;
                        case "pship":
                            addPassengerShip(sc, hmShip);
                            break;
                        case "cship":
                            addCargoShip(sc, hmShip);
                            break;
                        case "person":
                            addPerson(sc);
                            break;
                        case "job":
                            addJob(sc, hmShip, jpJobs);
                            break;
                    }
                }
            }
            sc1.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occured while processing file: " + e.getMessage());
        }
        getResourcePanel();
    }// end process

    private void addPort(Scanner scan, JPanel jpPorts) {
        String name = scan.next();
        int index = scan.nextInt();
        int parent = scan.nextInt();
        Seaport seaport = new Seaport(name, index, parent, jpPorts);
        hmPorts.put(seaport.getIndex(), seaport);
    }// end addPort

    private void addDock(Scanner scan, HashMap<Integer, Dock> hmDock) {
        String name = scan.next();
        int index = scan.nextInt();
        int parent = scan.nextInt();
        Dock dock = new Dock(name, index, parent);
        hmPorts.get(dock.getParent()).addDock(dock);
    }// end addDock

    private void addPassengerShip(Scanner scan, HashMap<Integer, Ship> hmShip) {
        String name = scan.next();
        int index = scan.nextInt();
        int parent = scan.nextInt();
        double weight = scan.nextDouble();
        double length = scan.nextDouble();
        double width = scan.nextDouble();
        double draft = scan.nextDouble();
        int numPassengers = scan.nextInt();
        int numRooms = scan.nextInt();
        int numOccupied = scan.nextInt();
        PassengerShip pship = new PassengerShip(name, index, parent, weight, length, width, draft, numPassengers,
                numRooms, numOccupied);
        if (pship.checkDocked()) {
            Dock md = getDockByIndex(parent);
            getSeaportByIndex(md.getParent()).assignShip(pship, "passenger");
        } else {
            hmPorts.get(parent).assignShip(pship, "passenger");
        }
        hmShip.put(index, pship);
//        assignShip(pship, "passenger");
    }// end addPassengerShip

    private void addCargoShip(Scanner scan, HashMap<Integer, Ship> hmShip) {
        String name = scan.next();
        int index = scan.nextInt();
        int parent = scan.nextInt();
        double weight = scan.nextDouble();
        double length = scan.nextDouble();
        double width = scan.nextDouble();
        double draft = scan.nextDouble();
        double cargoWeight = scan.nextDouble();
        double cargoVolume = scan.nextDouble();
        double cargoValue = scan.nextDouble();

        CargoShip cship = new CargoShip(name, index, parent, weight, length, width, draft, cargoWeight, cargoVolume,
                cargoValue);
        if (cship.checkDocked()) {
            Dock md = getDockByIndex(parent);
            getSeaportByIndex(md.getParent()).assignShip(cship, "passenger");
        } else {
            hmPorts.get(parent).assignShip(cship, "passenger");
        }
        getSeaportByIndex(parent).addCargoShip(cship);
        hmShip.put(index, cship);
    }// end addCargoShip

    private void addPerson(Scanner scan) {
        String name = scan.next();
        int index = scan.nextInt();
        int parent = scan.nextInt();
        String skill = scan.next();
        Person person = new Person(name, index, parent, skill);
        getSeaportByIndex(parent).addPerson(person);
    }// end addPerson

    private void addJob(Scanner scan, HashMap<Integer, Ship> hmShip, JPanel jp) {
        String name = scan.next();
        int index = scan.nextInt();
        int parent = scan.nextInt();
        double duration = scan.nextDouble();
        ArrayList<String> skills = new ArrayList<>();
        while (scan.hasNext()) {
            skills.add(scan.next());
        }

        Ship ship = hmShip.get(parent);
        Job job = new Job(name, index, parent, duration, skills, jp, ship);
        hmShip.get(parent).addJob(job);
    }// end 

    Seaport getSeaportByIndex(int i) {
        for (Seaport port : hmPorts.values()) {
            if (port.getIndex() == i) {
                return port;
            }
        }
        return null;
    }// end getSeaportByIndex

    Ship getShipByIndex(int i, HashMap<Integer, Ship> hms) {
        if (hms == null) {
            for (Seaport msp : hmPorts.values()) {
                for (Ship ms : msp.ships) {
                    if (ms.getIndex() == i) {
                        return ms;
                    }
                }
            }
        } else {
            return hms.get(i);
        }
        return null;
    } // end getShipByIndex

    private Dock getDockByIndex(int i) {
        for (Seaport port : hmPorts.values()) {
            for (Dock dock : port.docks) {
                if (dock.getIndex() == i) {
                    return dock;
                }
            }
        }
        return null;
    }// end getDockByIndex

    Person getPersonByIndex(int i) {
        for (Seaport port : hmPorts.values()) {
            for (Person person : port.persons) {
                if (person.getIndex() == i) {
                    return person;
                }
            }
        }
        return null;
    }// end getPersonByIndex

    public String sortByWidth() {
        String output = "Sorting by Width\n";
        for (Seaport seaport : hmPorts.values()) {

            Collections.sort(seaport.que, new Comparator<Ship>() {

                @Override
                public int compare(Ship shipA, Ship shipB) {
                    if (shipA.width > shipB.width) {
                        return 1;
                    } else if (shipA.width < shipB.width) {
                        return -1;
                    } else {
                        return 0;
                    }
                }

            });
            output += "Port: ";
            output += seaport.getName() + "\n";
            output += String.format("%-15.30s %-30.30s %-30.30s%n", "", "Ships:", "Width:");
            for (Ship ship : seaport.que) {
                output += String.format("%-15.30s %-30.30s %-30.30s%n", "", ship.getName(), ship.width);
            }
            output += "\n";
        }
        return output;
    }

    public String sortByLength() {
        String output = "Sorting by Length\n";

        for (Seaport seaport : hmPorts.values()) {

            Collections.sort(seaport.que, new Comparator<Ship>() {

                @Override
                public int compare(Ship shipA, Ship shipB) {
                    if (shipA.length > shipB.length) {
                        return 1;
                    } else if (shipA.length < shipB.length) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            output += "Port: ";
            output += seaport.getName() + "\n";
            output += String.format("%-15.30s %-30.30s %-30.30s%n", "", "Ships:", "Length:");
            for (Ship ship : seaport.que) {
                output += String.format("%-15.30s %-30.30s %-30.30s%n", "", ship.getName(), ship.length);
            }
            output += "\n";
        }
        return output;
    }

    public String sortByWeight() {
        String output = "Sorting by Weight\n";
        for (Seaport seaport : hmPorts.values()) {

            Collections.sort(seaport.que, new Comparator<Ship>() {

                @Override
                public int compare(Ship shipA, Ship shipB) {
                    if (shipA.weight > shipB.weight) {
                        return 1;
                    } else if (shipA.weight < shipB.weight) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            output += "Port: ";
            output += seaport.getName() + "\n";
            output += String.format("%-15.30s %-30.30s %-30.30s%n", "", "Ships:", "Weight:");
            for (Ship ship : seaport.que) {
                output += String.format("%-15.30s %-30.30s %-30.30s%n", "", ship.getName(), ship.weight);
            }
            output += "\n";
        }
        return output;
    }

    public String sortByDraft() {
        String output = "Sorting by Draft\n";
        for (Seaport seaport : hmPorts.values()) {

            Collections.sort(seaport.que, new Comparator<Ship>() {

                @Override
                public int compare(Ship shipA, Ship shipB) {
                    if (shipA.draft > shipB.draft) {
                        return 1;
                    } else if (shipA.draft < shipB.draft) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            output += "Port: ";
            output += seaport.getName() + "\n";
            output += String.format("%-15.30s %-30.30s %-30.30s%n", "", "Ships:", "Draft:");
            for (Ship ship : seaport.que) {
                output += String.format("%-15.30s %-30.30s %-30.30s%n", "", ship.getName(), ship.draft);
            }
            output += "\n";
        }
        return output;
    }

    // methods for searching
    // search by Index
    public String searchByIndex(String s) {
        int index = 0;
        try {
            index = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "User must enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Seaport port = getSeaportByIndex(index);
        Person person = getPersonByIndex(index);
        Ship ship = getShipByIndex(index, null);
        Dock dock = getDockByIndex(index);

        String output = "";
        output += (dock != null) ? dock.toString() : "";
        output += (ship != null) ? ship.toString() : "";
        output += (person != null) ? person.toString() : "";
        output += (port != null) ? port.toString() : "";
        if (output.equals("")) {
            output = "Index not found";
        }
        return output;
    }

    // search by skill
    public String searchBySkill(String s) {
        System.out.println("Search By Skill");
        String search = s.toLowerCase();
        String output = "The following personnel have the requested skill (" + s + "):\n";
        for (Seaport port : hmPorts.values()) {
            for (Person person : port.persons) {
                if (person.getSkill().equals(search)) {
                    output += "\t" + person.getName() + "\n";
                }
            }
        }
        return output;
    }

    // search by type
    public String searchByType(String s) {
        String search = s.toLowerCase();
        String output = "";
        if (search.equals("passenger ship")) {
            output = "List of all Passenger Ships:\n\t";
            for (Seaport port : hmPorts.values()) {
                for (Ship ship : port.ships) {
                    if (ship.getShipType().equals("Passenger Ship")) {
                        output += ship.getName() + "\n\t";
                    }
                }
            }
        } else if (search.equals("cargo ship")) {
            output = "List of all Cargo Ships:\n\t";
            for (Seaport port : hmPorts.values()) {
                for (Ship ship : port.ships) {
                    if (ship.getShipType().equals("Cargo Ship")) {
                        if (!output.contains(ship.getName())) {
                            output += ship.getName() + "\n\t";
                        }
                    }
                }
            }
        } else {
            output = "Please type either Passenger Ship or Cargo Ship to search by ship type.";
        }

        return output;
    }

    // search by name
    public String searchByName(String s) {
        String search = s.toLowerCase();
        String output = "Searching for " + search + "\n";
        for (Seaport port : hmPorts.values()) {
            if (port.getName().toLowerCase().equals(search)) {
                output += port.toString();
            }
            for (Dock dock : port.docks) {
                if (dock.getName().toLowerCase().equals(search)) {
                    output += dock.getName() + "\nShips in Dock:\n\t\t" + dock.getShip().getName();
                }
            }

            for (CargoShip cship : port.getCargoShips()) {
                if (cship.getName().toLowerCase().equals(search)) {
                    output += cship.getName() + "\nDetails: " + "\n\tCurrent Port Location: " + port.getName()
                            + "\n\tShip Type: " + cship.getShipType() + "\n\tWeight: " + cship.getWeight()
                            + "\n\tLength: " + cship.getLength() + "\n\tWidth: " + cship.getWidth() + "\n\tDraft: "
                            + cship.getDraft() + "\n\tCargo Value: " + cship.getCargoValue() + "\n\tCargo Volume: "
                            + cship.getCargoVolume() + "\n\tCargo Weight: " + cship.getCargoWeight();
                    break;
                }
            }
            for (PassengerShip pship : port.getPassengerShips()) {
                if (pship.getName().toLowerCase().equals(search)) {
                    output += pship.getName() + "\nDetails: " + "\n\tCurrent Port Location: " + port.getName()
                            + "\n\tShip Type: " + pship.getShipType() + "\n\tWeight: " + pship.getWeight()
                            + "\n\tLength: " + pship.getLength() + "\n\tWidth: " + pship.getWidth() + "\n\tDraft: "
                            + pship.getDraft() + "\n\tTotal Passengers: " + pship.getNumberOfPassengers()
                            + "\n\tTotal Occupied Rooms: " + pship.getNumberOfOccupiedRooms()
                            + "\n\tTotal Number of Rooms: " + pship.getNumberOfRooms();
                    break;
                }
            }
            for (Person person : port.persons) {
                if (person.getName().toLowerCase().equals(search)) {
                    output += person.getName() + "\n\tJob: " + person.getSkill();
                }
            }
        }
        return output;
    }// end searchByName

    public JPanel getResourcePanel() {

        jpResources.add(new JLabel(new String("Port Name"), SwingConstants.CENTER));
        jpResources.add(new JLabel(new String("Dock Name"), SwingConstants.CENTER));
        jpResources.add(new JLabel(new String("Docked Ship"), SwingConstants.CENTER));
        jpResources.add(new JLabel(new String("Job Status"), SwingConstants.CENTER));
        jpResources.add(new JLabel(new String("Workers Available"), SwingConstants.CENTER));
        jpResources.add(new JLabel(new String("Ships in Que"), SwingConstants.CENTER));

        for (Seaport port : hmPorts.values()) {
            // rblP jpResources.add(new JLabel(port., SwingConstants.CENTER))
            port.setPanel();

        }
        return jpResources;
    }

    @Override
    public String toString() {
        String output = ">>>>> The world:";
        for (Seaport port : hmPorts.values()) {
            output += port.toString();
        }
        return output;
    }// end toString
}// end World
