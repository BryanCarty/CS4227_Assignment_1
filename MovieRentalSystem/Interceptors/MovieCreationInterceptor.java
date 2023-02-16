package MovieRentalSystem.Interceptors;

import MovieRentalSystem.ContextObjectInterfaces.PostMovieCreationContext;
import MovieRentalSystem.ContextObjectInterfaces.PreMovieCreationContext;

public interface MovieCreationInterceptor {
    public void onPreMovieCreation(PreMovieCreationContext context);
    public void onPostMovieCreation(PostMovieCreationContext context);
}

