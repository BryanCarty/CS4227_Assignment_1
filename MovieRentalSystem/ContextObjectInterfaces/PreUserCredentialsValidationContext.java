package MovieRentalSystem.ContextObjectInterfaces;

public interface PreUserCredentialsValidationContext {
    public String getUsername();
    public String getPassword();
    public void setIllegalCharacterCredentials(boolean illegalCharDetected);
}
