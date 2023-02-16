package MovieRentalSystem.ContextObjectInterfaces;

public interface PostUserCredentialsValidationContext {
    public int getNumberOfSalesSinceLastLogIn();
    public int getNumberOfNewCustomersSinceLastLogIn();
    public String getTimeSinceLastLogIn();
}
