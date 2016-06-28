package ru.jollydroid.net;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.jollydroid.net.model.UserAnswer;
import rx.Observable;

/**
 * Created by User on 28.06.2016.
 */
public interface  Api {
    @GET("master/test1.json")
    Call<UserAnswer> getUsers();

    @GET("master/test1.json")
    Observable<UserAnswer> getUsersObservable();
}
