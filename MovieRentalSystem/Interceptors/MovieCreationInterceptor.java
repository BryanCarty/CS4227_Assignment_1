package MovieRentalSystem.Interceptors;

import MovieRentalSystem.ContextObjectInterfaces.PostMovieCreationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreMovieCreationContext;

public interface MovieCreationInterceptor {
    public void onPreMovieCreation(PreMovieCreationContext context); //Prior To Movie Creation
    public void onPostMovieCreation(PostMovieCreationContext context);//After Movie Creation
}

