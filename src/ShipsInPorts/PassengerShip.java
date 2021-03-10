// File: PassengerShip.java
// Date: 24 February 2019
// Author: Tom Batchlear
// Purpose: The PassengerShip class contains all the information specific to a 
// passenger ship.  This includes the number of passengers, number of rooms, 
// and number of occupied rooms.  Extends the Ship class.
package ShipsInPorts;

public class PassengerShip extends Ship {

    private int numberOfOccupiedRooms, numberOfPassengers, numberOfRooms;
    
    public PassengerShip(String name, int index, int parent,
     double weight, double length, double width, double draft, 
     int numPassengers, int numRooms, int numOccupied) {
        super(name, index, parent, weight, length, width, draft);
        this.numberOfRooms = numRooms;
        this.numberOfPassengers = numPassengers;
        this.numberOfOccupiedRooms = numOccupied;
    }

    //getter methods
    public int getNumberOfPassengers(){
        return numberOfPassengers;
    }// end getNumberOfPassengers

    public int getNumberOfRooms(){
        return numberOfRooms;
    }// end getNumberOfRooms
    
    public int getNumberOfOccupiedRooms(){
        return numberOfOccupiedRooms;
    }// end getNumberOfOccupiedRooms
    
    //setter methods
    public void setNumberOfPassengers(int p){
        this.numberOfPassengers = p;
    }// end setNumberOfPassengers
    
    public void setNumberOfRooms(int r){
        this.numberOfRooms = r;
    }// end setNumberOfRooms
    
    public void setNumberOfOccupiedRooms(int or){
        this.numberOfOccupiedRooms = or;
    }
    
    //toString method for PassengerShip.java
    @Override
    public String toString() {
        String st = "Passenger ship: " + super.toString();
        return st;
    } // end method toString

}
