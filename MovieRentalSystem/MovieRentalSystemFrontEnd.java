package MovieRentalSystem;

public class MovieRentalSystemFrontEnd {
    public Movie createMovie(String name, int priceCode){
        return new Movie(name, priceCode);
    }

    public Rental createRental(Movie movie, int daysRented){
        return new Rental(movie, daysRented);
    }

    public Customer createCustomer(String name){
        return new Customer(name);
    }

    public void addRental(Customer customer, Rental rental){
        customer.addRental(rental);
    }

    public String getStringStatement(Customer customer){
        return customer.statement();
    }

    public String getHtmlStatement(Customer customer){
        return customer.htmlStatement();
    }
}
