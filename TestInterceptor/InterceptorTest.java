package TestInterceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import MovieRentalSystem.MovieRentalSystemFrontEnd;
import MovieRentalSystem.ContextObjectInterfaces.PostUserCredentialsValidationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreUserCredentialsValidationContext;
import MovieRentalSystem.Interceptors.UserCredentialsValidationInterceptor;
import javafx.util.Pair;

public class InterceptorTest {
    @Test
    public void TestPreUserCredentialValidationIllegalCharacters(){
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
        Pair<Pair<Boolean, String>, String> login = movieRentalSystem.login("Select * from Users", "pass2");
        assertEquals(false, login.getLeft().getLeft());
        assertTrue("401 Unauthorized: Illegal Character detected in Credentials".equals( login.getLeft().getRight()));
        assertNull(login.getRight());
    }
}
