package com.example;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.jollydroid.net.Api;
import ru.jollydroid.net.NetClient;
import ru.jollydroid.net.model.User;
import ru.jollydroid.net.model.UserAnswer;
import rx.Observable;

public class MyClass {
    public static void main(String[] args) {
        Retrofit retrofit = NetClient
                .getInstance(true, "https://raw.githubusercontent.com/tseglevskiy/testdata/")
                .getClient();
        Api client = retrofit.create(Api.class);

/*        try {
            Call<UserAnswer> call = client.getUsers();
            Response<UserAnswer> response = call.execute();
            UserAnswer body = response.body();
            System.out.println(body.users.size());
            for (User u: body.users) {
                System.out.println("age: " + u.age);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Observable<UserAnswer> observable = client.getUsersObservable();
        observable
                // На конвеере UserAnswer
                .map(u -> u.users)
                // На конвеере ArrayList<User>
                .concatMap(a -> Observable.from(a))
                // На конвеере User (один за одним)
                .filter(u -> u.age > 25)
                .filter(u -> u.age < 30)
                .map(u -> "age: " + u.age)
                // На конвеере описание пользователей
                .subscribe(o -> System.out.println(o));

    }
}
