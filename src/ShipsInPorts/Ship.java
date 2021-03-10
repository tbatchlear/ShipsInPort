// File: Ship.java
// Date: 24 February 2019
// Author: 
// Purpose: The Ship class contains all the base information that every ship has.
// This includes draft, length, width and weight.  Extends the Thing class.
package ShipsInPorts;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Ship extends Thing {

    private PortTime arrivalTime = new PortTime();
    private PortTime dockTime = new PortTime();
    protected double draft, length, weight, width;
    protected ArrayList<Job> jobs = new ArrayList<>();
    protected boolean isDocked = false;
    JLabel lblShipName = new JLabel("", SwingConstants.CENTER);
    Seaport parentPort;

    public Ship(String name, int index, int parent, double weight, double length, double width, double draft) {
        super(name, index, parent);
        this.draft = draft;
        this.length = length;
        this.width = width;
        this.weight = weight;

        lblShipName.setText(name);
    }

    public void setParentPort(Seaport port) {
        this.parentPort = port;
    }

    public boolean checkDocked() {
        if (new Integer(this.getParent()).toString().startsWith("1")) {
            isDocked = false;

        } else if (new Integer(this.getParent()).toString().startsWith("2")) {
            isDocked = true;
        }
        return isDocked;
    }

    public void dockShip() {
        isDocked = true;
    }

    public Dock getDock() {
        if (isDocked) {
            for (Dock dock : parentPort.docks) {
                if (dock.ship == this) {
                    return dock;
                }
            }
        }
        return null;
    }

    public Seaport getParentPort() {
        return parentPort;
    }

    public double getDraft() {
        return this.draft;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getLength() {
        return this.length;
    }

    public double getWidth() {
        return this.width;
    }

    public String getShipType() {
        String output = "";
        output = (this instanceof PassengerShip ? "Passenger Ship" : "Cargo Ship");
        return output;
    }

    public void addJob(Job j) {
        this.jobs.add(j);
    }

    public ArrayList<Person> requestWorkers(ArrayList<String> requirements) {

        synchronized (parentPort.persons) {
            ArrayList<Person> workers = new ArrayList<>();
            for (String req : requirements) {
                for (Person worker : parentPort.persons) {

                    if (worker.skill.equals(req) && !worker.busyFlag) {
                        workers.add(worker);
                        worker.busyFlag = true;
                        break;
                    }
                }
            }
            return workers;
        }
    }

    public boolean skillExists(String requirement) {
        for (Person p : this.parentPort.persons) {
            if (p.getSkill().equals(requirement)) {
                return true;
            }
        }
        return false;
    }

    //toString method for Ship.java
    public String toString() {
        String output = super.toString();

        return output;
    }//end toString

}//end Ship.java
