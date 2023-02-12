package MovieRentalSystem;
public class Movie {    
    static final int  CHILDRENS = 2;    
    static final int  REGULAR = 0;    
    static final int  NEW_RELEASE = 1;    
    private String _title;    
    private Price _price;   
    
    Movie(String title, int priceCode) {   
        _title = title;        
        setPriceCode(priceCode);    
    }    
    
    int getPriceCode() {       
        return _price.getPriceCode();    
    }    
    
    void setPriceCode(int arg) {        
        switch (arg) {        
            case REGULAR:          
                _price = new RegularPrice();           
                break;           
            case CHILDRENS:           
                _price = new ChildrensPrice();           
                break;           
            case NEW_RELEASE:           
                _price = new NewReleasePrice();           
                break;        
            default:       
                throw new IllegalArgumentException("Incorrect Price Code");        
        }    
    }        
    
    String getTitle (){       
        return _title;    
    };  

    double getCharge(int daysRented) {  
        return _price.getCharge(daysRented);     
    } 

    int getFrequentRenterPoints(int daysRented) {          
        return _price.getFrequentRenterPoints(daysRented);    
    }
} 
