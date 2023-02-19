package MovieRentalSystem.Interceptors;

import MovieRentalSystem.ContextObjectInterfaces.PreUserCredentialsValidationContext;
import MovieRentalSystem.ContextObjectInterfaces.PostUserCredentialsValidationContext;


public interface UserCredentialsValidationInterceptor {
    public void onPreUserCredentialsValidation(PreUserCredentialsValidationContext context); //Prior To User Credential Validation
    public void onPostUserCredentialsValidation(PostUserCredentialsValidationContext context); //After User Credential Validation
}
