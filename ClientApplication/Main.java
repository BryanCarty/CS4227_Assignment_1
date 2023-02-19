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

        //Create Movie Rental System Front End
        MovieRentalSystemFrontEnd movieRentalSystem = new MovieRentalSystem.MovieRentalSystemFrontEnd();

        //Define Concrete User Credential Validation Interceptor
        UserCredentialsValidationInterceptor clientCredentialsInterceptor = new UserCredentialsValidationInterceptor(){

            @Override
            public void onPreUserCredentialsValidation(PreUserCredentialsValidationContext context) {
                // Check For Illegal Characters And Avoid SQL Injection
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
                //Log Updates Since Last Login
                System.out.println("INFO: Since you last logged in on the "+context.getDateOfLastLogIn()+", there has been "+context.getNumberOfNewCustomersSinceLastLogIn()+" new customer accounts created, and "+context.getNumberOfRentalsSinceLastLogIn()+" new rental transactions");         
            }

        };

        //Register Credential Validation Interceptor With Dispatcher
        movieRentalSystem.getUserCredDispatcherInstance().registerCredentialValidationInterceptor(clientCredentialsInterceptor);
        
        //Login
        Pair<Pair<Boolean, String>, String> login = movieRentalSystem.login("Mathew", "pass2");
        System.out.println(login);



        //Define Concrete MovieCreation Interceptor
        MovieCreationInterceptor movieCreationInterceptor = new MovieCreationInterceptor() {

            @Override
            public void onPreMovieCreation(PreMovieCreationContext context) {
                //Start Timer To Measure Performance
                context.startTimer();
                
            }

            @Override
            public void onPostMovieCreation(PostMovieCreationContext context) {
                //Stop Timer To Measure Performance
                System.out.println("INFO: It took "+context.stopTimer()+" milliseconds to create a movie");
                
            }
            
        };

        //Register MovieCreation Interceptor With Dispatcher
        movieRentalSystem.getMovieCreationDispatcherInstance().registerMovieCreationInterceptor(movieCreationInterceptor);

        //Create A Movie Using Login Token
        Pair<Pair<Boolean, String>, Movie> movie = movieRentalSystem.createMovie("Harry Potter", 0, login.getRight());
        System.out.println(movie);

        //Create A Rental Using Movie And Login Token
        Pair<Pair<Boolean, String>, Rental> rental = movieRentalSystem.createRental(movie.getRight(), 3, login.getRight());
        System.out.println(rental);

        //Create A Customer Using Login Token
        Pair<Pair<Boolean, String>, Customer> customer = movieRentalSystem.createCustomer("tommy", login.getRight());
        System.out.println(customer.getRight());  

        //Add A Rental To That Customers Account
        Pair<Boolean, String> addRental = movieRentalSystem.addRental(customer.getRight(), rental.getRight(), login.getRight());
        System.out.println(addRental);

        //Get String Statement For That Customer
        System.out.println(movieRentalSystem.getStringStatement(customer.getRight(), login.getRight()));

        //Get Html Statment For That Customer
        System.out.println(movieRentalSystem.getHtmlStatement(customer.getRight(), login.getRight()));

        //Logout Using Login Token
        System.out.println(movieRentalSystem.logout(login.getRight()));
    }
}