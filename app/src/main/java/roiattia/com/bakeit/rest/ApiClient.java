package roiattia.com.bakeit.rest;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static Retrofit retrofit = null;

    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
            new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create())
                    .create());

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
        }
        return retrofit;
    }
}
