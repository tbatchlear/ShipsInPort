// File: Dock.java
// Date: 24 February 2019
// Author: Tom Batchlear
// Purpose: The Dock class holds all information abouut the dock to include 
// which ship is in dock.  Extends the Thing class
package ShipsInPorts;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Dock extends Thing {

    protected Ship ship;
    JLabel lblDockName = new JLabel("", SwingConstants.CENTER);
    
    public Dock(String name, int index, int parent) {
        super(name, index, parent);
        lblDockName.setText(name);
    }

    //getter method
    public Ship getShip() {
        return ship;
    }//end getShip

    //setter method
    public void setShip(Ship ship) {
        this.ship = ship;
    }//end setShip

    //toString method
    @Override
    public String toString() {
        if (ship != null) {
            return "\n Dock: " + super.toString() + "\n Ship: " + this.ship.toString();
        } else {
            return "\n Dock: " + super.toString();
        }
    }//end toString Method

}//end class Dock.java
