package MovieRentalSystem;
import java.util.Enumeration;
import java.util.Vector;

public class Customer {    
    private String _name;    
    private Vector<Rental> _rentals = new Vector<Rental>();    
    
    Customer (String name){        
        _name = name;    
    }   
    
    void addRental(Rental arg) {      
        _rentals.addElement(arg);    
    }
    
    String getName (){        
        return _name;    
    }

    private double getTotalCharge() {          
        double result = 0;          
        Enumeration<Rental> rentals = _rentals.elements();          
        while (rentals.hasMoreElements()) {              
            Rental aRental = (Rental) rentals.nextElement();              
            result += aRental.getCharge();          
        }          
        return result;      
    } 

    private int getTotalFrequentRenterPoints(){           
        int result = 0;          
        Enumeration<Rental> rentals = _rentals.elements();          
        while (rentals.hasMoreElements()) {              
            Rental aRental = (Rental) rentals.nextElement();  
            result += aRental.getFrequentRenterPoints();          
        }          
        return result;      
    }

    String statement() {         
        Enumeration<Rental> rentals = _rentals.elements();         
        String result = "Rental Record for " + getName() + "\n";         
        while (rentals.hasMoreElements()) {                         
            Rental aRental = (Rental) rentals.nextElement();                           
            result += "\t" + aRental.getMovie().getTitle()+ "\t" + String.valueOf(aRental.getCharge()) + "\n";                    
        }         
        //add footer lines         
        result +=  "Amount owed is " + String.valueOf(getTotalCharge()) + "\n"; 
        result += "You earned " + String.valueOf(getTotalFrequentRenterPoints()) +" frequent renter points";         
        return result;     
    }

    String htmlStatement() {       
        Enumeration<Rental> rentals = _rentals.elements();       
        String result = "<H1>Rentals for <EM>" + getName() + "</EM></ H1><P>\n";       
        while (rentals.hasMoreElements()) {           
            Rental aRental = (Rental) rentals.nextElement(); 
            //show figures for each rental           
            result += aRental.getMovie().getTitle()+ ": " + String.valueOf(aRental.getCharge()) + "<BR>\n";       
        }    
        //add footer lines       
        result +=  "<P>You owe <EM>" + String.valueOf(getTotalCharge()) + "</EM><P>\n";       
        result += "On this rental you earned <EM>" + String.valueOf(getTotalFrequentRenterPoints()) + "</EM> frequent renter points<P>";       
        return result;   
    } 
}