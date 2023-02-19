package MovieRentalSystem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Random;

import org.json.*;
import org.json.simple.parser.ParseException;

import MovieRentalSystem.ContextObjectInterfaces.PostMovieCreationContext;
import MovieRentalSystem.ContextObjectInterfaces.PostUserCredentialsValidationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreMovieCreationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreUserCredentialsValidationContext;
import MovieRentalSystem.Dispatchers.MovieCreationDispatcher;
import MovieRentalSystem.Dispatchers.UserCredentialsValidationDispatcher;
import javafx.util.Pair;
public class MovieRentalSystemFrontEnd {
    private static final UserCredentialsValidationDispatcher userCredDispatcher =  new UserCredentialsValidationDispatcher();
    private static final MovieCreationDispatcher movieCreationDispatcher =  new MovieCreationDispatcher();

    private boolean illegalCharacterDetected;
    private long timeInMilliseconds;

    public UserCredentialsValidationDispatcher getUserCredDispatcherInstance(){
        return userCredDispatcher;
    }

    public MovieCreationDispatcher getMovieCreationDispatcherInstance(){
        return movieCreationDispatcher;
    }

    public Pair<Pair<Boolean, String>, String> login(String name, String password){
        //Pre User Credential Validation Context
        PreUserCredentialsValidationContext preCredValContext = new PreUserCredentialsValidationContext() {

            @Override
            public String getUsername() {
                return name;
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public void setIllegalCharacterCredentials(boolean illegalCharDetected) {
                illegalCharacterDetected = illegalCharDetected;
            }
            
        };
        
        try {
            //PRE USER CREDENTIAL VALIDATION
            userCredDispatcher.dispatchPreUserCredentialsValidation(preCredValContext);
            if (illegalCharacterDetected){
                return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(false, "401 Unauthorized: Illegal Character detected in Credentials"), null);
            }
            Pair<Boolean, Integer> isValidUserPair = isValidUser(name, password);
            boolean invalidLogin = !isValidUserPair.getLeft();
            if (invalidLogin){
                return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(false, "401 Unauthorized: Invalid Credentials"), null);
            }
            //Post User Credential Validation Context
            PostUserCredentialsValidationContext postUserCredentialsValidationContext = new PostUserCredentialsValidationContext() {
                @Override
                public int getNumberOfRentalsSinceLastLogIn() {
                    int lastLogin = getUserLastLogin(isValidUserPair.getRight());
                    int numAdditionalSales = getNumSalesSince(lastLogin);
                    return numAdditionalSales;
                }

                @Override
                public int getNumberOfNewCustomersSinceLastLogIn() {
                    int lastLogin = getUserLastLogin(isValidUserPair.getRight());
                    int numAdditionalCust = getNumNewCustSince(lastLogin);
                    return numAdditionalCust;
                }

                @Override
                public String getDateOfLastLogIn() {
                    int lastLogin = getUserLastLogin(isValidUserPair.getRight());
                    return unixToDate(lastLogin);
                }                
            };
            //Post User Credential Validation
            userCredDispatcher.dispatchPostUserCredentialsValidation(postUserCredentialsValidationContext);
            return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(true, "200 Success: Successfully logged in"), generateUserToken(isValidUserPair.getRight()));
        } catch (Exception e) {
            return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(false, "500 Internal Server Error: An error occured while processing your request: "+e.getMessage()), null);
        }
    }

    public Pair<Pair<Boolean, String>, Movie> createMovie(String name, int priceCode, String token){
        try {
            if (!isLoggedIn(token)){
                return new Pair<Pair<Boolean,String>,Movie>(new Pair<Boolean,String>(false, "401 Invalid Login Credentials: You are not logged in"), null);
            }
        } catch (Exception e) {
            return new Pair<Pair<Boolean,String>,Movie>(new Pair<Boolean,String>(false, "500 Internal Server Error: An error occured while processing your request: "+e.getMessage()), null);
        }
        //Pre Movie Creation Context
        PreMovieCreationContext preMovieCreationContext = new PreMovieCreationContext() {

            @Override
            public void startTimer() {
                timeInMilliseconds = System.currentTimeMillis();
            }
            
        };
        //PRE MOVIE CREATION
        movieCreationDispatcher.dispatchPreMovieCreation(preMovieCreationContext);
        Movie newMovie = new Movie(name, priceCode);
        //POST MOVIE CREATION CONTEXT
        PostMovieCreationContext postMovieCreationContext = new PostMovieCreationContext() {

            @Override
            public long stopTimer() {
                return System.currentTimeMillis() - timeInMilliseconds;
            }
            
        };
        //POST MOVIE CREATION
        movieCreationDispatcher.dispatchPostMovieCreation(postMovieCreationContext);
        return new Pair<Pair<Boolean,String>,Movie>(new Pair<Boolean,String>(true, "200 Success: Movie created successfully"), newMovie);
    }

    public Pair<Pair<Boolean, String>, Rental> createRental(Movie movie, int daysRented, String token){
        try {
            if (!isLoggedIn(token)){
                return new Pair<Pair<Boolean,String>,Rental>(new Pair<Boolean,String>(false, "401 Invalid Login Credentials: You are not logged in"), null);
            }
        } catch (Exception e) {
            return new Pair<Pair<Boolean,String>,Rental>(new Pair<Boolean,String>(false, "500 Internal Server Error: An error occured while processing your request: "+e.getMessage()), null);
        }
        return new Pair<Pair<Boolean,String>,Rental>(new Pair<Boolean,String>(true, "200 Success: Rental created successfully"), new Rental(movie, daysRented));
    }

    public Pair<Pair<Boolean, String>, Customer> createCustomer(String name, String token){
        try {
            if (!isLoggedIn(token)){
                return new Pair<Pair<Boolean,String>,Customer>(new Pair<Boolean,String>(false, "401 Invalid Login Credentials: You are not logged in"), null);
            }
        } catch (Exception e) {
            return new Pair<Pair<Boolean,String>,Customer>(new Pair<Boolean,String>(false, "500 Internal Server Error: An error occured while processing your request: "+e.getMessage()), null);
        }
        Customer returnCust = new Customer(name);
        return new Pair<Pair<Boolean,String>,Customer>(new Pair<Boolean,String>(true, "200 Success: Customer created successfully"), returnCust);
    }

    public Pair<Boolean, String> addRental(Customer customer, Rental rental, String token){
        try {
            if (!isLoggedIn(token)){
                return new Pair<Boolean,String>(false, "401 Invalid Login Credentials: You are not logged in");
            }
        } catch (Exception e) {
            return new Pair<Boolean,String>(false, "500 Internal Server Error: An error occured while processing your request: "+e.getMessage());
        }
        customer.addRental(rental);
        return new Pair<Boolean,String>(true, "200 Success: Rental added for customer");
        
    }

    public Pair<Pair<Boolean, String>, String> getStringStatement(Customer customer, String token){
        try {
            if (!isLoggedIn(token)){
                return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(false, "401 Invalid Login Credentials: You are not logged in"), null);
            }
        } catch (Exception e) {
            return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(false, "500 Internal Server Error: An error occured while processing your request: "+e.getMessage()), null);
        }
        String newStatement = customer.statement();
        return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(true, "200 Success: Customer created successfully"), newStatement);
    }

    public Pair<Pair<Boolean, String>, String> getHtmlStatement(Customer customer, String token){
        try {
            if (!isLoggedIn(token)){
                return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(false, "401 Invalid Login Credentials: You are not logged in"), null);
            }
        } catch (Exception e) {
            return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(false, "500 Internal Server Error: An error occured while processing your request: "+e.getMessage()), null);
        }
        
        return new Pair<Pair<Boolean,String>,String>(new Pair<Boolean,String>(true, "200 Success: Customer created successfully"), customer.htmlStatement());
    }

    public Pair<Boolean, String> logout(String token){
        try{
            JSONArray usersDbAsArray = readInDatabase("UsersDatabase.json");
            boolean userFound = false;
            for(int i = 0; i < usersDbAsArray.length(); i++) {
                JSONObject user = (JSONObject) usersDbAsArray.get(i);
                if(user.getString("login_token").equals(token)){
                    user.put("login_token", "");
                    usersDbAsArray.put(user.getInt("id")-1, user);
                    userFound = true;
                    break;
                }
            }
            if (!userFound){
                return new Pair<Boolean,String>(false, "No user exists for the provided token");
            }
            //write jsonArray back to file
            Writer writer = new FileWriter("MovieRentalSystem/UsersDatabase.json");
            writer.write(usersDbAsArray.toString());
            writer.close();
            return new Pair<Boolean,String>(true, "200 Success: Successfully logged out");
        }catch(Exception e){
            return new Pair<Boolean,String>(false, "500 Internal Server Error: An error occured while processing your request: "+e.getMessage());
        }
    }

    private Pair<Boolean, Integer> isValidUser(String name, String password) throws IOException, ParseException, JSONException, URISyntaxException{
        JSONArray usersDbAsArray = readInDatabase("UsersDatabase.json");
        boolean validUserCredentials = false;
        int userId = 0;
        for(int i = 0; i < usersDbAsArray.length(); i++) {
            JSONObject user = (JSONObject) usersDbAsArray.get(i);
            if(((String)user.get("name")).equals(name) && ((String)user.get("password")).equals(password)){
                validUserCredentials = true;
                userId = user.getInt("id");
                break;
            }
        }
        return new Pair<Boolean, Integer>(validUserCredentials, userId);
    }

    private String generateUserToken(int id) throws IOException, ParseException, JSONException, URISyntaxException{
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String stringToken = buffer.toString();
        //read in database
        JSONArray usersDbAsArray = readInDatabase("UsersDatabase.json");
        for(int i = 0; i < usersDbAsArray.length(); i++) {
            JSONObject user = (JSONObject) usersDbAsArray.get(i);
            if(user.getInt("id")==id){
                user.put("login_token", stringToken);
                usersDbAsArray.put(id-1, user);
                break;
            }
        }
        //write jsonArray back to file
        Writer writer = new FileWriter("MovieRentalSystem/UsersDatabase.json");
        writer.write(usersDbAsArray.toString());
        writer.close();
        return stringToken;

    }

    private JSONArray readInDatabase(String databaseLocation) throws IOException, ParseException, URISyntaxException, JSONException{
        //read in database
        BufferedReader reader = new BufferedReader(new FileReader("MovieRentalSystem/"+databaseLocation));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        // delete the last new line separator
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();
        String content = stringBuilder.toString();
        // convert to json array
        JSONArray json = new JSONArray(content);
        reader.close();
        return json;
    }

    private boolean isLoggedIn(String token) throws IOException, ParseException, URISyntaxException, JSONException{
        JSONArray usersDbAsArray = readInDatabase("UsersDatabase.json");
        for(int i = 0; i < usersDbAsArray.length(); i++) {
            JSONObject user = (JSONObject) usersDbAsArray.get(i);
            String usertoken = user.getString("login_token");
            if((token.equals(usertoken))){
                return true;
            }
        }
        return false;
    }


    private int getUserLastLogin(Integer integer){
        //This method would query a database if this were a real world application. For now it returns a dummy unix timestamp
        return 1676491902;
    }

    
    private int getNumSalesSince(Integer integer){
        //This method would query a database if this were a real world application. For now it returns a dummy number of sales
        return 3;
    }

    private int getNumNewCustSince(Integer integer){
        //This method would query a database if this were a real world application. For now it returns a dummy number of new customers
        return 2;
    }

    private String unixToDate(int t){
        //This method would get the date that corresponds to the passed unix time if it were a real world application. For now it returns a dummy date
        return "15-02-22";
    }
}
