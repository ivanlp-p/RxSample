package com.example;


import org.javatuples.Pair;

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
        // На конвеере UserAnswer
        observable
                // На конвеере ArrayList<User>
                .map(u -> u.users)
                // На конвеере User (один за одним)
                .concatMap(a -> Observable.from(a))
                .toSortedList((u1, u2) -> Integer.valueOf(u1.age).compareTo(u2.age))
                .concatMap(a -> Observable.from(a))
                //.filter(u -> u.age > 25)
                //.filter(u -> u.age < 30)
                //.map(u -> "id: " + u.id + "|   age: " + u.age)
               // .map(user -> user.age)
                .reduce(
                        Pair.with(0,0),
                        (Pair<Integer, Integer> p, User u) -> Pair.with(p.getValue0() + u.age, p.getValue1() + 1))
                .map(p -> Double.valueOf(p.getValue0()/p.getValue1()))
                // На конвеере описание пользователей
                .subscribe(o -> System.out.println(o));



    }
}
