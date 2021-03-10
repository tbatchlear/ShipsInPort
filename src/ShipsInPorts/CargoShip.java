// File: CargoShip.java
// Date: 21 January 2019
// Author:
// Purpose: The CargoShip class contains all the information specific to a 
// cargo ship.  This includes the volume, value, and weight of the cargo.
// Extends the Ship class.
package ShipsInPorts;

public class CargoShip extends Ship {

    private double cargoValue, cargoVolume, cargoWeight;

    public CargoShip(String name, int index, int parent,
            double weight, double length, double width, double draft,
            double cargoWeight, double cargoVolume, double cargoValue) {
        super(name, index, parent, weight, length, width, draft);
        this.cargoVolume = cargoVolume;
        this.cargoValue = cargoValue;
        this.cargoWeight = cargoWeight;
    }

    //getter methods
    public double getCargoValue() {
        return cargoValue;
    }//end getCargoValue

    public double getCargoVolume() {
        return cargoVolume;
    }//end getCargoVolume

    public double getCargoWeight() {
        return cargoWeight;
    }//end getCargoWeight

    //setter methods
    public void setCargoVolume(double cv) {
        this.cargoVolume = cv;
    }//end setCargoVolume

    public void setCargoValue(double cv) {
        this.cargoValue = cv;
    }//end setCargoValue

    public void setCargoWeight(double cw) {
        this.cargoWeight = cw;
    }//end setCargoWeight

    //toString method for PassengerShip.java
    @Override
    public String toString() {
        String st = "Cargo ship: " + super.toString();
        return st;
    } // end method toString
}
