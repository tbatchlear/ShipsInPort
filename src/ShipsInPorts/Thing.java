// File: Thing.java
// Date: 24 February 2019
// Author: 
// Purpose: Thing handles the base information for all items within the seaport
//  Every item has a name, and index, and a parent.  Additionally, Thing
// implements the comparable interface.
package ShipsInPorts;

import java.util.HashMap;

public class Thing implements Comparable<Thing> {

    //variables
    private int index;
    protected String name;
    protected int parent;
    Thing thingObj;
    
    //Generic Constructor
    public Thing(){
    }//end generic constructor Thing()

    //scanner Constructor
    public Thing (String name, int index, int parent){
        super();
        this.name = name;
        this.index = index;
        this.parent = parent;
    }//end scanner Constructor Thing()
    
    //getter methods for Thing class
    public int getIndex(){
        return this.index;
    }//end getIndex()
    
    public String getName(){
        return this.name;
    }//end getName()
    
    public int getParent(){
        return this.parent;
    }//end getParent()
    
    //setter methods for Thing class
    public void setIndex(int i){
        this.index = i;
    }//end setIndex()
    
    public void setName(String n){
        this.name = n;
    }//end setName()
    
    public void setParent(int p){
        this.parent = p;
    }//end setParent()
    
    //compareTo method
    @Override
    public int compareTo(Thing other) {
        return this.name.compareTo(other.getName());
    }//end compareTo()
    
    //New Method for HashMap Implementation
    //thing setter
    public void setThing(HashMap parent){
        if(parent.get(parent) != null){
            this.thingObj = (Thing)parent.get(parent);
        }
    }

    //toString method
    @Override
    public String toString(){
        return this.name + " " + this.index;
    }//end toString()
}
