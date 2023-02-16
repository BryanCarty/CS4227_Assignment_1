package MovieRentalSystem.Dispatchers;

import java.util.Vector;

import MovieRentalSystem.ContextObjectInterfaces.PostMovieCreationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreMovieCreationContext;
import MovieRentalSystem.Interceptors.MovieCreationInterceptor;

public class MovieCreationDispatcher implements MovieCreationInterceptor{
    Vector<MovieCreationInterceptor> InterceptorsStore = new Vector<MovieCreationInterceptor>();

    synchronized public void registerMovieCreationInterceptor(MovieCreationInterceptor i) {
        InterceptorsStore.addElement(i);
    }

    synchronized public void removeMovieCreationInterceptor(MovieCreationInterceptor i) {
        InterceptorsStore.removeElement(i);
    }

    public void dispatchPreMovieCreation(PreMovieCreationContext context) {
        Vector<MovieCreationInterceptor> Interceptors = new Vector<MovieCreationInterceptor>();
        synchronized (this) {
            Interceptors = (Vector<MovieCreationInterceptor>) InterceptorsStore.clone();
        }
        for(int i = 0; i < Interceptors.size(); i++) {
            MovieCreationInterceptor ic =(MovieCreationInterceptor)Interceptors.elementAt(i);
            ic.onPreMovieCreation(context);
        }  
    }

    public void dispatchPostMovieCreation(PostMovieCreationContext context) {
        Vector<MovieCreationInterceptor> Interceptors = new Vector<MovieCreationInterceptor>();
        synchronized (this) {
            Interceptors = (Vector<MovieCreationInterceptor>) InterceptorsStore.clone();
        }
        for(int i = 0; i < Interceptors.size(); i++) {
            MovieCreationInterceptor ic =(MovieCreationInterceptor)Interceptors.elementAt(i);
            ic.onPostMovieCreation(context);
        }  
    }

    @Override
    public void onPreMovieCreation(PreMovieCreationContext context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPostMovieCreation(PostMovieCreationContext context) {
        // TODO Auto-generated method stub
        
    }
}
