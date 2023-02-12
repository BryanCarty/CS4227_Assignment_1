package ClientApplication;
import MovieRentalSystem.Customer;
import MovieRentalSystem.Movie;
import MovieRentalSystem.MovieRentalSystemFrontEnd;
import MovieRentalSystem.Rental;

public class Main {
    public static void main(String[] args){
        MovieRentalSystemFrontEnd movieRentalSystem = new MovieRentalSystemFrontEnd();
        Customer johnny = movieRentalSystem.createCustomer("Johnny");
        Movie movie = movieRentalSystem.createMovie("HarryPotter", 0);
        Rental rental = movieRentalSystem.createRental(movie, 3);
        movieRentalSystem.addRental(johnny, rental);
        System.out.println(movieRentalSystem.getStringStatement(johnny));
        System.out.println(movieRentalSystem.getHtmlStatement(johnny));

    }
}