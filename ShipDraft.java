/*
 * CS121 A'11
 * HW 5: 
 *    Does a very simple model of a ship sink?
 *    If not, report the level at which it floats (draft).
 *    
 *    Arin Schwartz 
 */


public class ShipDraft {
    static double weight = 90000000;    //  in kilograms
    static double height = 22.5;        // in meters
    static double tol = height * .005;
    //Added a LB variable to keep track of the lower bound on interval
    static double LB = 0;

    //Returns the volume of the hull at a specific height
    public static double hullVolume(Ship hull, double LB, double height){
    	double hullVolume = Integral.atrap(0, height, hull, tol);
    	return hullVolume;
    }
    
    //Returns the weight of the water as a function of the ship's height
    public static double waterWeight(Ship hull, double LB, double height){
    	double waterVolume = hullVolume(hull, LB, height);
    	double waterWeight = 1027*waterVolume;
    	return waterWeight;
    }
    
    //Boolean method to determine whether our value is within 1/2 of a percent of the height
    public static boolean isMatch(Ship hull, double LB, double midpoint, double height){
    	
    	double halfpercent = tol;
    	double weightUB = waterWeight(hull, LB, height);
    	double weightLB = waterWeight(hull, LB, midpoint);
    	
    	if(Math.abs(weightUB - weightLB) < halfpercent){
    		return true;
    	}
    	return false;
    	
    }
    
    //Computes draft of ship at a given height
    public static double draft(Ship hull, double height){
    	double midpoint = (height + LB)/2;
    	if(isMatch(hull, LB, midpoint, height)){
    		return height;
    	}
    	else if(waterWeight(hull, LB, midpoint) < weight){
    		LB = midpoint;
    		return draft(hull, height);
    	}
    	else{
    		return draft(hull, midpoint);
    	}
    }
    

    public static void main(String[] arg) {
        Ship hull = new Ship(height);
        
        /*Turns out I didn't need to test all the auxilary functions, as they were quite simple.
        *They just helped me compartmentalize the thing a little better.
        *
        *Tested the draft function below. Ran into one problem.
        *Needed to add a lower bound variable to assist in the calculation of midpoints.
        *
        *Also, the wording in the assignment, "it is sufficient to get the draft to one half of one percent
        *of the height of the object." Seemed a little ambiguous, so I had to think for a while
        *about what it actually meant. In the end (as seen in the isMatch function) i said they matched
        *if the interval between the last two tested water weights at a given height was less than
        *half a percent of the height. Seemed to give an appropriate level of approximation.
        *Ok sorry for the rant. Think I got it though.
        */
        double z = draft(hull, height);
        System.out.println("The draft for this ship is " + z + " meters, somewhat quite exactly indeed.");
        

    }
}
