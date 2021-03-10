// File: PortTime.java
// Date: 24 February 2019
// Author: 
// Purpose: Contains the portTime of each ship at each port.
package ShipsInPorts;

public class PortTime {
    
    int time;
    
    //constructor
    public PortTime(){
        this.time = 0;
    }//end constructor
    
    //getter
    public int getPortTime(){
        return time;
    }//end getter
    
    //setter
    public void setPortTime(int t){
        this.time = t;
    }//end setter
    
}//end class PortTime
