package MovieRentalSystem.ContextObjectInterfaces;

public interface PreUserCredentialsValidationContext {
    public String getUsername(); //Client Can Access User Login Name (Prior To Authentication) Via This Method
    public String getPassword(); //Client Can Access User Login Password (Prior To Authentication) Via This Method
    public void setIllegalCharacterCredentials(boolean illegalCharDetected); //Client Can Set A Flag To Prevent The Authentication Process From Continuing
}
