package MovieRentalSystem.ContextObjectInterfaces;

public interface PostUserCredentialsValidationContext {
    public int getNumberOfRentalsSinceLastLogIn(); //On Successful Login, The Client Can Get The Number Of New Rentals Since The Users Last Login Via This Method
    public int getNumberOfNewCustomersSinceLastLogIn(); //On Successful Login, The Client Can Get The Number Of New Customers Since The Users Last Login Via This Method
    public String getDateOfLastLogIn(); //On Successful Login, The Client Can Get The Date Of The Users Last Login Via This Method
}