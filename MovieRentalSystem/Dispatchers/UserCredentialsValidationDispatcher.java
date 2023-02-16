package MovieRentalSystem.Dispatchers;

import java.util.Vector;

import MovieRentalSystem.ContextObjectInterfaces.PostUserCredentialsValidationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreUserCredentialsValidationContext;
import MovieRentalSystem.Interceptors.UserCredentialsValidationInterceptor;

public class UserCredentialsValidationDispatcher implements UserCredentialsValidationInterceptor{
    Vector<UserCredentialsValidationInterceptor> InterceptorsStore = new Vector<UserCredentialsValidationInterceptor>();

    synchronized public void registerCredentialValidationInterceptor(UserCredentialsValidationInterceptor i) {
        InterceptorsStore.addElement(i);
    }

    synchronized public void removeCredentialValidationInterceptor(UserCredentialsValidationInterceptor i) {
        InterceptorsStore.removeElement(i);
    }

    public void dispatchPreUserCredentialsValidation(PreUserCredentialsValidationContext context) {
        Vector<UserCredentialsValidationInterceptor> Interceptors = new Vector<UserCredentialsValidationInterceptor>();
        synchronized (this) {
            Interceptors = (Vector<UserCredentialsValidationInterceptor>) InterceptorsStore.clone();
        }
        for(int i = 0; i < Interceptors.size(); i++) {
            UserCredentialsValidationInterceptor ic =(UserCredentialsValidationInterceptor)Interceptors.elementAt(i);
            ic.onPreUserCredentialsValidation(context);
        }  
    }

    public void dispatchPostUserCredentialsValidation(PostUserCredentialsValidationContext context) {
        Vector<UserCredentialsValidationInterceptor> Interceptors = new Vector<UserCredentialsValidationInterceptor>();
        synchronized (this) {
            Interceptors = (Vector<UserCredentialsValidationInterceptor>) InterceptorsStore.clone();
        }
        for(int i = 0; i < Interceptors.size(); i++) {
            UserCredentialsValidationInterceptor ic =(UserCredentialsValidationInterceptor)Interceptors.elementAt(i);
            ic.onPostUserCredentialsValidation(context);
        }  
    }

    @Override
    public void onPreUserCredentialsValidation(PreUserCredentialsValidationContext context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPostUserCredentialsValidation(PostUserCredentialsValidationContext context) {
        // TODO Auto-generated method stub
        
    }
}
