package ClientApplication;
import MovieRentalSystem.Customer;
import MovieRentalSystem.Movie;
import MovieRentalSystem.MovieRentalSystemFrontEnd;
import MovieRentalSystem.Rental;
import javafx.util.Pair;

public class Main {
    public static void main(String[] args){
        MovieRentalSystemFrontEnd movieRentalSystem = new MovieRentalSystem.MovieRentalSystemFrontEnd();
        
        Pair<Pair<Boolean, String>, String> login = movieRentalSystem.login("Mathew", "pass2");
        System.out.println(login);
        Pair<Pair<Boolean, String>, Movie> movie = movieRentalSystem.createMovie("HarryPotter", 0, login.getRight());
        System.out.println(movie);
        Pair<Pair<Boolean, String>, Rental> rental = movieRentalSystem.createRental(movie.getRight(), 3, login.getRight());
        System.out.println(rental);
        Pair<Pair<Boolean, String>, Customer> customer = movieRentalSystem.createCustomer("tommy", login.getRight());
        System.out.println(customer.getRight());  
        System.out.println(rental.getRight()); 
        Pair<Boolean, String> addRental = movieRentalSystem.addRental(customer.getRight(), rental.getRight(), login.getRight());
        System.out.println(addRental);
        System.out.println(movieRentalSystem.getStringStatement(customer.getRight(), login.getRight()));
        System.out.println(movieRentalSystem.getHtmlStatement(customer.getRight(), login.getRight()));
        System.out.println(movieRentalSystem.logout(login.getRight()));

        Pair<Pair<Boolean, String>, String> login2 = movieRentalSystem.login("Mathew", "pass2");
        System.out.println(login2);
        Pair<Pair<Boolean, String>, Movie> movie2 = movieRentalSystem.createMovie("HarryPotter", 0, login2.getRight());
        System.out.println(movie2);
        Pair<Pair<Boolean, String>, Rental> rental2 = movieRentalSystem.createRental(movie2.getRight(), 3, login2.getRight());
        System.out.println(rental2);
        Pair<Pair<Boolean, String>, Customer> customer2 = movieRentalSystem.createCustomer("tommy", login2.getRight());
        System.out.println(customer2.getRight());  
        System.out.println(rental2.getRight()); 
        Pair<Boolean, String> addRental2 = movieRentalSystem.addRental(customer2.getRight(), rental2.getRight(), login2.getRight());
        System.out.println(addRental2);
        System.out.println(movieRentalSystem.getStringStatement(customer2.getRight(), login2.getRight()));
        System.out.println(movieRentalSystem.getHtmlStatement(customer2.getRight(), login2.getRight()));
        System.out.println(movieRentalSystem.logout(login2.getRight()));
        
/**
        Pair<Boolean, String> addRental = movieRentalSystem.addRental(customer2.getRight(), rental2.getRight(), login3.getRight());
        System.out.println(addRental.getRight());

        
        Pair<Pair<Boolean, String>, Movie> movie1 = movieRentalSystem.createMovie("HarryPotter", 0, login.getRight());
        System.out.println(movie1);
        Pair<Boolean, String> login2 = movieRentalSystem.login("Mathew", "pass2");
        System.out.println(login2);
        Pair<Pair<Boolean, String>, Movie> movie = movieRentalSystem.createMovie("HarryPotter", 0, login.getRight());
        System.out.println(movie);
        Pair<Boolean, String> logout = movieRentalSystem.logout(login.getRight());
        System.out.println(logout);
        Pair<Pair<Boolean, String>, Movie> movie1 = movieRentalSystem.createMovie("HarryPotter", 0, login.getRight());
        System.out.println(movie1);
        //System.out.println(movieRentalSystem.logout("?b<?q?"));
        Customer johnny = movieRentalSystem.createCustomer("Johnny");
        Movie movie = movieRentalSystem.createMovie("HarryPotter", 0);
        Rental rental = movieRentalSystem.createRental(movie, 3);
        movieRentalSystem.addRental(johnny, rental);
        System.out.println(movieRentalSystem.getStringStatement(johnny));
        System.out.println(movieRentalSystem.getHtmlStatement(johnny));
        **/

    }
}