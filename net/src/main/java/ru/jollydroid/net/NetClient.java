package ru.jollydroid.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 28.06.2016.
 */
public class NetClient {

    private static NetClient instance;
    private final OkHttpClient genericClient;
    private final Gson gson;
    private final Retrofit retrofit;

    private HttpLoggingInterceptor fullLogInterceptor;

    public static NetClient getInstance(boolean isDebug, String baseUrl) {
        if (instance == null) {
            instance = new NetClient(isDebug, baseUrl);
        }

        return instance;
    }

    public static NetClient getInstance() {
        return instance;
    }

    public NetClient(boolean isDebug, String baseUrl) {
        OkHttpClient.Builder genericClientBuilder = UnsafeOkHttpClient.getUnsafeOkHttpClientBuilder()
                .followRedirects(false);

        if (isDebug) {
            fullLogInterceptor = new HttpLoggingInterceptor();
            fullLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            genericClientBuilder.addInterceptor(fullLogInterceptor);
        }

        genericClient = genericClientBuilder.build();

        gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(genericClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getClient() {
        return retrofit;
    }

    public OkHttpClient getHttpClient() {
        return genericClient;
    }
}
