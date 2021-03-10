// File: Person.java
// Date: 24 February 2019
// Author: Tom Batchlear
// Purpose: Contains the skills of the person at each port.  Extends the Thing 
// class
package ShipsInPorts;

import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Person extends Thing {

    String skill;
    boolean busyFlag = false;
    JLabel lblPersonName = new JLabel("", SwingConstants.CENTER);

    public Person(String name, int index, int parent, String skill) {
        super(name, index, parent);
        this.skill = skill;
        lblPersonName.setText(name);
        
    }

    //getter method
    public String getSkill() {
        return skill;
    }//end getSkill

    //setter method
    public void setSkill(String s) {
        this.skill = s;
    }//end setSkill

    public String toString() {
        String st = "Person: " + super.toString() + " " + skill;
        return st;
    } // end method toString

}
