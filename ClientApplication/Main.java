package ClientApplication;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import MovieRentalSystem.Customer;
import MovieRentalSystem.Movie;
import MovieRentalSystem.MovieRentalSystemFrontEnd;
import MovieRentalSystem.Rental;
import MovieRentalSystem.ContextObjectInterfaces.PostMovieCreationContext;
import MovieRentalSystem.ContextObjectInterfaces.PostUserCredentialsValidationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreMovieCreationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreUserCredentialsValidationContext;
import MovieRentalSystem.Interceptors.MovieCreationInterceptor;
import MovieRentalSystem.Interceptors.UserCredentialsValidationInterceptor;
import javafx.util.Pair;

public class Main {
    public static void main(String[] args){
        MovieRentalSystemFrontEnd movieRentalSystem = new MovieRentalSystem.MovieRentalSystemFrontEnd();

        UserCredentialsValidationInterceptor clientCredentialsInterceptor = new UserCredentialsValidationInterceptor(){

            @Override
            public void onPreUserCredentialsValidation(PreUserCredentialsValidationContext context) {
                // Check for illegal characters in client credentials - avoid SQL injection
                Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
                Matcher passwordHasSpecial = special.matcher(context.getPassword());
                Matcher usernameHasSpecial = special.matcher(context.getUsername());
                if (passwordHasSpecial.find() || usernameHasSpecial.find()){
                    context.setIllegalCharacterCredentials(true);
                    return;
                }
                context.setIllegalCharacterCredentials(false);
            }

            @Override
            public void onPostUserCredentialsValidation(PostUserCredentialsValidationContext context) {
                //Log updates since last login
                System.out.println("INFO: Since you last logged in on the "+context.getTimeSinceLastLogIn()+", there has been "+context.getNumberOfNewCustomersSinceLastLogIn()+" new customer accounts created, and "+context.getNumberOfSalesSinceLastLogIn()+" new transactions");         
            }

        };

        movieRentalSystem.getUserCredDispatcherInstance().registerCredentialValidationInterceptor(clientCredentialsInterceptor);
        
        Pair<Pair<Boolean, String>, String> login = movieRentalSystem.login("Mathew", "pass2");
        System.out.println(login);


        MovieCreationInterceptor movieCreationInterceptor = new MovieCreationInterceptor() {

            @Override
            public void onPreMovieCreation(PreMovieCreationContext context) {
                context.startTimer();
                
            }

            @Override
            public void onPostMovieCreation(PostMovieCreationContext context) {
                System.out.println("INFO: It took "+context.stopTimer()+" milliseconds to create a movie");
                
            }
            
        };

        movieRentalSystem.getMovieCreationDispatcherInstance().registerMovieCreationInterceptor(movieCreationInterceptor);
        Pair<Pair<Boolean, String>, Movie> movie = movieRentalSystem.createMovie("HarryPotter", 0, login.getRight());
        System.out.println(movie);
        Pair<Pair<Boolean, String>, Rental> rental = movieRentalSystem.createRental(movie.getRight(), 3, login.getRight());
        System.out.println(rental);
        Pair<Pair<Boolean, String>, Customer> customer = movieRentalSystem.createCustomer("tommy", login.getRight());
        System.out.println(customer.getRight());  
        Pair<Boolean, String> addRental = movieRentalSystem.addRental(customer.getRight(), rental.getRight(), login.getRight());
        System.out.println(addRental);
        System.out.println(movieRentalSystem.getStringStatement(customer.getRight(), login.getRight()));
        System.out.println(movieRentalSystem.getHtmlStatement(customer.getRight(), login.getRight()));
        System.out.println(movieRentalSystem.logout(login.getRight()));
    }
}